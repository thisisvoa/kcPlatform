package com.kcp.platform.base.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.kcp.platform.base.job.constant.JobConstants;
import com.kcp.platform.base.job.core.JobRunLogger;
import com.kcp.platform.base.job.entity.JobCommand;
import com.kcp.platform.base.job.entity.JobScheduler;
import com.kcp.platform.base.job.service.JobService;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.OrderableComparator;

/**
 * 任务调度管理服务器，它装配了Quartz的Scheduler，通过Spring的自动注入机制，自动完成任务，触发器的注册，再通过任务全
 * 限定名和触发器指定任务名相匹配的机制完成触发器对任务的调度。此外SchedulerManager还自动为需要任务执行日志的任务注册执行
 * 日志监听器，为任务高度管理器注册任务管理的监听器。
 * <pre>
 *    概念：
 *     1.任务全限定名：即<i>任务分组名.任务名称</i>，SchedulerManager根据任务全限定名和触发器所指定任务的全限定名匹配的
 *       规则自动进行触发器和任务的映射。
 *     2.全局任务：其执行逻辑的影响是全局性的，会被其它服务节点"感知"，如每日生成报表任务，定时批量发送邮件的任务。
 *     3.本地任务：其执行逻辑的影响是局部性的，不会被其它服务节点“感知”，如本地缓存刷新任务。
 * </pre>
 * SchedulerManager在系统启动时，按以下流程进行工作：
 * <pre>
 *   1.自动将实现了{@link Job}接口的Bean注册到{@link #jobMap}的任务注册表中,如果还任务还实现了
 * {@link BindTrigger}接口，则将注册到{@link #allJobTriggerMap}的任务注册表中。
 *   2.系统自动加载T_JOB表中配置的任务调度逻辑,构造成相应的{@link org.quartz.Trigger}注册到{@link #allJobTriggerMap}
 * 的触发器注册表中。
 *   3.SchedulerManager根据触发器和任务全限定名的映射规则进行匹配，使触发器调度相应的任务。如果任务为实现{@link BindTrigger}
 * 接口的任务，即任务本身带了触发器（通过{@link BindTrigger#getTriggers()}附带触发器），此时，还在{@link #allJobTriggerMap}
 * 拥有和任务匹配的触发器，如果{@link com.kcp.platform.base.job.BindTrigger#isTopPriority()}返回<code>true</code>，则使用任务自带的触发器，否则使用
 * {@link #allJobTriggerMap}中匹配的触发器。
 * </pre>
 * <p>此外，SchedulerManager还提供了若干方法用于对任务调度管理器中的任务进行控制。
 *
 */
@SuppressWarnings("unchecked")
@Component
public class JobSchedulerManager {
    
    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 任务管理Service
     */
    @Autowired
    private JobService jobService;

    /**
     * 任务调度器
     */
    @Autowired
    @Qualifier("scheduler")
    private Scheduler scheduler;

    /**
     * 负责记录任务运行情况的日志记录器
     */
    @Autowired
    @Qualifier("jobRunLogger")
    private JobListener jobRunLogger;
    
    /**
     * 分隔符
     */
    private static final char SPLITOR = '#';
    
    /**
     * 将job以JOB_ATTRIBUTE_NAME的名称设置到jobDetail的属性列表中
     */
    private static final String JOB_ATTR_NAME = "job";
    
    /**
     * 将jobService以JOB_ATTRIBUTE_NAME的名称设置到jobDetail的属性列表中
     */
    private static final String JOB_SERVICE_ATTR_NAME = "jobService";
    
    /**
     * 任务调度管理器会在系统启动时自动启动，
     * 可以将该属性设置为true让其不随系统启动（在单元测试时有用）
     */
    private static boolean stopJobScheduler = !JobConstants.START_JOB_SCHEDULER_WHEN_APP_START;
    
    /**
     * 系统中所有的被调度的任务
     */
    private static Map<String, Job> jobMap = new LinkedHashMap<String, Job>();

    /**
     * 在T_JOB表中所配置的触发器
     */
    private static Map<String, List<Trigger>> allJobTriggerMap = new HashMap<String, List<Trigger>>();

    /**
     * 任务运行状态
     */
    private static Map<String, Boolean> jobStatusMap = new HashMap<String, Boolean>();

    /**
     * 自动绑定到任务中去的任务执行监听器,其key监听器的名称，格式为<jobGroupName>.<jobName>
     * 对于默认的任务分组，key为<jobName>
     */
    private static Map<String, BeanJobListener> beanJobListenerMap = new HashMap<String, BeanJobListener>();
    
    /**
     * 最后一次更新获取任务控制命令的时间，初始值为5年前
     */
    public static Date jobCommandLastTime = new Date(System.currentTimeMillis() - 5 * 400 * 24 * 60 * 60 * 1000L);

    /**
     * 是否当前的全局任务调度服务节点
     */
    private static boolean currentGlobalJobScheduler = false;

    /**
     * 是否已经装配
     */
    private static boolean mounted = false;

    /**
     * 服务器地址，
     */
    private static String hostUrl;

    /**
     * 对于80商品的用户来说，
     *  http://127.0.0.1是 http://127.0.0.1:80的别名
     */
    private static String hostUrlAlias;

    /**
     * 启动的时间
     */
    private static long mountTime;

    

    /**
     * 是否是全局任务调度服务
     * @return
     */
    public static boolean isCurrentGlobalJobScheduler() {
        return currentGlobalJobScheduler;
    }

    /**
     * 设置为全局任务调度服务
     * @param currentGlobalJobScheduler
     */
    public static void setCurrentGlobalJobScheduler(boolean currentGlobalJobScheduler) {
        JobSchedulerManager.currentGlobalJobScheduler = currentGlobalJobScheduler;
    }

    /**
     * 任务调度服务是否已经装载
     * @return
     */
    public static boolean isMounted() {
        return mounted;
    }

    /**
     * 设置任务调度服务为装载状态
     * @param mounted
     */
    public static void setMounted(boolean mounted) {
        JobSchedulerManager.mountTime = System.currentTimeMillis();
        JobSchedulerManager.mounted = mounted;
    }

    public static String getHostUrlAlias() {
        return hostUrlAlias;
    }

    public static void setHostUrlAlias(String hostUrlAlias) {
        JobSchedulerManager.hostUrlAlias = hostUrlAlias;
    }

    public static String getHostUrl() {
        return hostUrl;
    }

    public static void setHostUrl(String hostUrl) {
        JobSchedulerManager.hostUrl = hostUrl;
    }

    /**
     * 停止所有的任务
     * @param stopJobScheduler
     */
    public static void setStopJobScheduler(boolean stopJobScheduler) {
        JobSchedulerManager.stopJobScheduler = stopJobScheduler;
    }

    /**
     * 从JobExecutionContext中获取Job
     * @param jobExecutionContext
     * @return
     */
    public static Job getJob(JobExecutionContext jobExecutionContext) {
        return (Job) jobExecutionContext.getJobDetail().getJobDataMap().get(JOB_ATTR_NAME);
    }

    /**
     * 判断job是否会在本任务调度服务节点运行
     * @param job
     * @return
     */
    public static boolean isJobCanRunInThisScheduler(Job job) {
        return !JobSchedulerManager.stopJobScheduler &&
                (JobSchedulerManager.jobStatusMap.get(getJobFullName(job.getJobGroupName(), job.getJobName())) == null ||
                        JobSchedulerManager.jobStatusMap.get(getJobFullName(job.getJobGroupName(), job.getJobName()))) &&
                (!job.isGlobal() ||
                        (job.isGlobal() &&
                                isCurrentGlobalJobScheduler()));
    }

    /**
     * 判断本任务调度服务管理器是否是currJobScheduler对应的
     * @param jobScheduler
     * @return
     */
    public static boolean isManagerOf(JobScheduler jobScheduler) {
        if (JobSchedulerManager.hostUrl != null) {
            return JobSchedulerManager.hostUrl.equalsIgnoreCase(jobScheduler.getHostUrl());
        }
        if (JobSchedulerManager.hostUrlAlias != null) {
            return JobSchedulerManager.hostUrlAlias.equalsIgnoreCase(jobScheduler.getHostUrl());
        }
        return false;
    }

    /**
     * 获取启动的时间
     * @return
     */
    public static long getMountTime() {
        return mountTime;
    }
    
    /**
     *  将实现了{@link Job}的Bean注册到相应的注册表中。
     * @param jobs
     */
    private void registerJobs(List<Job> jobs) {
        if (jobs != null) {
            OrderableComparator.sort(jobs);
            for (Job job : jobs) {
                if (jobMap.get(getJobFullName(job)) != null) {
                    reportExistSameNameJobs(jobs, job);
                } else {
                    jobMap.put(getJobFullName(job), job);
                }
            }
        }
    }

    /**
     * 如果存在多个同名的Job，给出警告信息
     * @param jobs
     * @param job
     */
    private void reportExistSameNameJobs(List<Job> jobs, Job job) {
        List<Job> sameNameJobs = new ArrayList<Job>();
        for (Job tempJob : jobs) {
            if (getJobFullName(job).equals(getJobFullName(tempJob))) {
                sameNameJobs.add(tempJob);
            }
        }
        StringBuilder sb = new StringBuilder("\n系统以下Job的名称重复：\n");
        sb.append("----------------------------------------------\n");
        String splitor = "";
        for (Job sameNameJob : sameNameJobs) {
            sb.append(splitor);
            sb.append(sameNameJob.getClass().getCanonicalName());
            splitor = ",\n";
        }
        sb.append("\n----------------------------------------------\n");
        sb.append("系统只会调度一个任务，其余的任务将被忽略.\n\n");
        logger.warn(sb.toString());
    }
    
    /**
     * 将{@link com.epgis.platform.module.job.BeanJobListener}类型的Bean注册到相应的注册表中。
     * @param beanJobListeners
     */
    private void setJobListeners(List<BeanJobListener> beanJobListeners) {
        for (BeanJobListener beanJobListener : beanJobListeners) {
            beanJobListenerMap.put(beanJobListener.getName(), beanJobListener);
        }
    }

    /**
     * 初始化任务调度管理器及任务的上下文信息
     *  在Schedulermanager的Bean实例化并完成依赖注入后调用该方法，
     *  将<code>jobs</code>和<code>bindTriggerJobs</code> 转化成Quertz的JobDetail对象。
     */
    public void initialize() {
        if (!stopJobScheduler) {
            //将实现了{@link Job}的Bean注册到相应的注册表中。
            List<Job> jobs = (List<Job>)SpringContextHolder.getBeanListByType(Job.class);
            if(null!=jobs){
                registerJobs(jobs);
            }
            //将{@link com.epgis.platform.module.job.BeanJobListener}类型的Bean注册到相应的注册表中
            List<BeanJobListener> jobListenerList = (List<BeanJobListener>)SpringContextHolder.getBeanListByType(BeanJobListener.class);
            if(null!=jobListenerList){
                setJobListeners(jobListenerList);
            }
            //从数据库中加载任务配置信息
            loadJobs();
            
            //注册任务运行日志记录器（JobListener）
            registerJobRunLogger();

            //注册任务监听器
            registerJobListeners();

            //装配任务
            mountJobs();

            //启动任务
            startJobs();
        }else{
           logger.info("任务调度管理器未启用>>................");
        }
    }
    
    /**
     * 从数据库中加载任务配置信息
     */
    private void loadJobs() {
        List<com.kcp.platform.base.job.entity.Job> jobs = jobService.getReleasedJobs();
        for (com.kcp.platform.base.job.entity.Job job : jobs) {
            mountJobTriggers(job.getJobGroupName(), job.getJobName(), job.getTriggers());
            setJobStatus(job.getJobGroupName(), job.getJobName(), job.isActive());
        }
        jobCommandLastTime = jobService.getCurrDbDate();
    }
    
    /**
     * 设置任务调度管理器所有任务的触发器
     * @param jobGroupName
     * @param jobName
     * @param triggers
     */
    private void mountJobTriggers(String jobGroupName, String jobName, List<Trigger> triggers) {
        allJobTriggerMap.put(getJobFullName(jobGroupName, jobName), triggers);
    }
    
    /**
     * 初始化任务的运行状态
     * @param jobGroupName
     * @param jobName
     * @param start
     */
    private void setJobStatus(String jobGroupName, String jobName, boolean start) {
        jobStatusMap.put(getJobFullName(jobGroupName, jobName), start);
    }
    
    /**
     * 注册任务事件监听器
     */
    private void registerJobListeners() {
        try {
            for (JobListener jobListener : beanJobListenerMap.values()) {
                this.scheduler.addJobListener(jobListener);
            }
        } catch (SchedulerException e) {
            logger.error("注册任务监听器时发生异常，异常信息为：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 注册任务运行日志记录器的任务监听器
     */
    private void registerJobRunLogger() {
        try {
            this.scheduler.addJobListener(this.jobRunLogger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 装配本任务调度管理器中的任务
     */
    private void mountJobs() {
        if (jobMap != null) {
            for (Job job : jobMap.values()) {
                mountJob(job);
            }
        }
    }
    
    /**
     * 将任务装载到任务调度管理服务器中
     * @param job
     */
    private void mountJob(Job job) {
        try {
            //获取任务触发器
            JobDetail jobDetail = buildJobDetail(job);

            if (job.isNeedLog()) {
                //添加任务执行日志监听器
                jobDetail.addJobListener(JobRunLogger.JOB_RUN_LOGGER);
            }

            //添加和任务匹配的任务监听器
            if (beanJobListenerMap.containsKey(getJobFullName(job))) {
                jobDetail.addJobListener(getJobFullName(job));
            }

            //对任务进行调度
            this.scheduler.addJob(jobDetail, true);
        } catch (SchedulerException e) {
            logger.error("任务调度管理器装载[" + getJobFullName(job) + "]时发生异常，异常信息为：" + e.getMessage());
        }
    }
    
    /**
     * 根据Job构造JobDetail对象
     * @param job
     */
    private JobDetail buildJobDetail(Job job) {
        Class bindJobClass = BindJobBean.class;
        JobDetail jobDetail = new JobDetail(job.getJobName(), job.getJobGroupName(), bindJobClass);
        jobDetail.setDurability(true);//在没有触发器与其绑定的情况下，也可以在调度器中生存
        jobDetail.setVolatility(true);//如果有提供持久化的结构，则保存之
        jobDetail.getJobDataMap().put(JOB_ATTR_NAME, job);
        jobDetail.getJobDataMap().put(JOB_SERVICE_ATTR_NAME, jobService);
        return jobDetail;
    }
    
    /**
     * 启动本任务调度管理器中的任务
     */
    private void startJobs() {
        if (jobMap != null) {
            for (Job job : jobMap.values()) {
                scheduleJob(job);
            }
        }
    }
    
    /**
     * 对Job任务进行高度
     * @param job
     */
    private void scheduleJob(Job job) {
        try {
            List<Trigger> matchedTriggers = getJobTriggers(job);
            try {
                if (matchedTriggers != null) {
                    for (Trigger trigger : matchedTriggers) {
                        this.scheduler.scheduleJob(trigger);
                    }
                }
            } catch (SchedulerException e) {
                logger.error("任务调度管理器启动[" + getJobFullName(job) + "]时发生异常，异常信息为：" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("启动[" + getJobFullName(job) + "]时发生异常", e);
        }
    }
    
    /**
     * 获取Job所对应的触发器
     * @param job
     */
    private List<Trigger> getJobTriggers(Job job) {
        //如果有实现绑定触发器的接口，并且优先级高
        List<Trigger> triggers = getMatchTrigger(job.getJobGroupName(), job.getJobName());
        if (job instanceof BindTrigger) {
            BindTrigger tempBindTrigger = ((BindTrigger) job);
            if (tempBindTrigger.isTopPriority() || triggers == null) {//绑定触发器优先级高
                triggers = tempBindTrigger.getTriggers();
            }
        }

        if (triggers != null) {
            for (Trigger trigger : triggers) {
                trigger.setJobGroup(job.getJobGroupName());
                trigger.setJobName(job.getJobName());
                if (trigger.getStartTime() == null) {
                    trigger.setStartTime(new Date());
                }
            }
            return triggers;
        } else {
            throw new IllegalArgumentException("任务[" + getJobFullName(job) + "]没有匹配的触发器，您可以通过实现BindTrigger接口提供触发器，\n" +
                    "也可以在控制台中为任务[\"+getJobFullName(job)+\"]配置一个相应的触发器。");
        }
    }
    
    /**
     * 获取任务所对应的触发器，按以下规则匹配（由于一个任务会对应多个触发器)：
     *  jobGroupName = triggerGroupName 并且jobName 和triggerName#N 中的triggerName相等
     * @param jobGroupName
     * @param jobName
     */
    private List<Trigger> getMatchTrigger(String jobGroupName, String jobName) {
        return allJobTriggerMap.get(getJobFullName(jobGroupName, jobName));
    }

    /**
     * 调用任务触发器匹配的任务
     * @param jobGroupName
     * @param jobName
     * @param triggers
     */
    public void scheduleJob(String jobGroupName, String jobName, List<Trigger> triggers) {
        Job job = getJobByName(jobGroupName, jobName);
        if (job == null) {
            logger.warn("任务[" + getJobFullName(jobGroupName, jobName) + "]不存在.");
            return;
        }
        addJobTriggers(jobGroupName, jobName, triggers);
        scheduleJob(job);
    }
    
    /**
     * 根据全限定名获取注册表中的任务
     * @param jobGroupName
     * @param jobName
     * @return
     */
    private Job getJobByName(String jobGroupName, String jobName) {
        return jobMap.get(getJobFullName(jobGroupName, jobName));
    }
    
    /**
     * 添加任务触发器
     * @param triggers
     */
    private void addJobTriggers(String jobGroupName, String jobName, List<Trigger> triggers) {
        allJobTriggerMap.put(getJobFullName(jobGroupName, jobName), triggers);
    }
    
    /**
     * 获取任务的全限定名
     * @param job
     */
    private static String getJobFullName(Job job) {
        return getJobFullName(job.getJobGroupName(), job.getJobName());
    }

    /**
     * 获取任务的全限定名
     * @param jobGroupName
     * @param jobName
     * @return
     */
    private static String getJobFullName(String jobGroupName, String jobName) {
        return jobGroupName + SPLITOR + jobName;
    }
    
    /**
     * 停止某个触发器对任务的调度
     * @param jobGroupName 任务分组名称
     * @param jobName      任务名称
     */
    public boolean unscheduleJob(String jobGroupName, String jobName) {
        try {
            Trigger[] triggers = this.scheduler.getTriggersOfJob(jobName, jobGroupName);//quartz的jobName在前，groupName在后
            if (triggers != null) {
                for (Trigger trigger : triggers) {
                    scheduler.unscheduleJob(trigger.getName(), trigger.getGroup());
                }
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("在解除[" + getJobFullName(jobGroupName, jobName) + "]任务调度时发生异常。", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据控制台发布的命令刷新任务的状态
     */
    public void executeJobCommand() {
        List<JobCommand> jobCommands = jobService.getLastJobCommands();
        for (JobCommand jobCommand : jobCommands) {
        	com.kcp.platform.base.job.entity.Job job = jobService.getJobByFullName(jobCommand.getJobGroupName(), jobCommand.getJobName());

            if (job != null) {
                //设置任务的运行状态
                setJobStatus(job.getJobGroupName(), job.getJobName(), job.isActive());

                //不管是开始或结束都要事先停止任务
                if (logger.isInfoEnabled()) {
                    logger.info("停止[" + job.getJobGroupName() + "." + job.getJobName() + "]任务.");
                }
                unscheduleJob(jobCommand.getJobGroupName(), jobCommand.getJobName());

                if (jobCommand.isStartJobCommand()) { //重新开启任务
                    scheduleJob(job.getJobGroupName(), job.getJobName(),job.getTriggers());
                    if (logger.isInfoEnabled()) {
                        logger.info("开始[" + job.getJobGroupName() + "." + job.getJobName() + "]任务.");
                    }
                }
            }
        }
        if (jobCommands != null && jobCommands.size() > 0) {
            jobService.deleteJobComandIssuedBeforeOneDay();
        }
    }
    
    /**
     * BindJobBean继承了Spring的QuartzJobBean，而QuartzJobBean继承了Quartz的Job类。
     * JobBean提供了一个<code>setJob()</code>的方法，在BindJobBean中的任务在执行之前
     * 该方法会被自动调用，将JobDataMap中一个和属性同名的对象设置到JobBean中。
     */
    public static class BindJobBean extends QuartzJobBean {
        /**
         * 日志记录器
         */
        protected Logger logger = LoggerFactory.getLogger(this.getClass());

        private JobService jobService;
        
        private Job job;

        public void setJob(Job job) {
            this.job = job;
        }

        public void setJobService(JobService jobService) {
            this.jobService = jobService;
        }

        /**
         * 任务执行的业务逻辑
         * @param jobExecutionContext
         */
        protected void executeInternal(JobExecutionContext jobExecutionContext) {
            //如果只要全局任务调度服务节点上执行
            if (isJobCanRunInThisScheduler(job)) {
                if (logger.isInfoEnabled()) {
                    logger.info("开始执行[" + job.getJobGroupName() + "." + job.getJobName() + "]任务。");
                }
                executeJob(jobExecutionContext);
                jobService.updateJobLastActiveTime(job.getJobGroupName(), job.getJobName());
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("本任务调度管理器不执行[" + job.getJobGroupName() + "." + job.getJobName() + "]任务。");
                }
            }
        }

        /**
         * 执行任务
         * @param jobExecutionContext
         */
        private void executeJob(JobExecutionContext jobExecutionContext) {
            JobExecuteResult jobExecuteResult = new JobExecuteResult();
            try {
                jobExecutionContext.put(JobExecuteResult.EXECUTE_RESULT, jobExecuteResult);
                job.execute(jobExecutionContext);
            } catch (Throwable e) {
                jobExecuteResult.setExecuteFail();
                jobExecuteResult.putResult("errorInfo", e.getMessage());
            } finally {
                //设置结束执行时间
                jobExecuteResult.setEndTime(new Date());
            }
        }
    }
}