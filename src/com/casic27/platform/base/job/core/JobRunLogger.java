package com.casic27.platform.base.job.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.Job;
import com.casic27.platform.base.job.JobExecuteResult;
import com.casic27.platform.base.job.JobSchedulerManager;
import com.casic27.platform.base.job.entity.JobRunLog;
import com.casic27.platform.base.job.service.JobLogService;
import com.casic27.platform.util.CodeGenerator;


/**
 * 任务执行日志记录器，将负责将任务执行时的日志记录到<code>T_JOB_RUN_LOG</code>表中。
 */
@Component("jobRunLogger")
public class JobRunLogger extends JobListenerSupport {
    
    /**
     * 日志记录器
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public static final String JOB_RUN_LOGGER = "JOB_RUN_LOGGER";

    @Autowired
    private JobLogService jobLogService;

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        Job job = JobSchedulerManager.getJob(jobExecutionContext);
        if (JobSchedulerManager.isJobCanRunInThisScheduler(job)) {
            if (logger.isDebugEnabled()) {
                logger.debug("job:" + jobExecutionContext.getJobDetail().getGroup() + "." +
                        jobExecutionContext.getJobDetail().getName() +
                        "任务开始执行。");
            }
        }
    }

    /**
     * 在任务执行完成后执行该方法。
     *
     * @param jobExecutionContext
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        Job job = JobSchedulerManager.getJob(jobExecutionContext);
        if (JobSchedulerManager.isJobCanRunInThisScheduler(job)) {
            String jobGroupName = jobExecutionContext.getJobDetail().getGroup();
            String jobName = jobExecutionContext.getJobDetail().getName();
            JobExecuteResult jobExecuteResult = (JobExecuteResult) jobExecutionContext.get(JobExecuteResult.EXECUTE_RESULT);
            saveJobExecuteLog(jobGroupName, jobName, jobExecuteResult);
            if (logger.isDebugEnabled() && jobExecuteResult.isExecutedSucessful()) {
                logger.debug("job:" + jobExecutionContext.getJobDetail().getGroup() + "." +
                        jobExecutionContext.getJobDetail().getName() +
                        "执行结束。");
            }
            if (!jobExecuteResult.isExecutedSucessful()) {
                logger.error("job:" + jobExecutionContext.getJobDetail().getGroup() + "." +
                        jobExecutionContext.getJobDetail().getName() +
                        "执行发生错误。" , e);
            }
        }
    }

    /**
     * 将任务运行日志保存到数据库中
     *
     * @param jobGroupName
     * @param jobName
     * @param jobExecuteResult
     */
    private void saveJobExecuteLog(String jobGroupName, String jobName, JobExecuteResult jobExecuteResult) {
        JobRunLog jobRunLog = buildJobRunLog(jobGroupName, jobName, jobExecuteResult);
        jobLogService.saveJobRunLog(jobRunLog);
    }

    /**
     * 根据jobExecuteResult构造出一个JobRunLog对象。
     *
     * @param jobGroupName
     * @param jobName
     * @param jobExecuteResult
     * @return
     */
    private JobRunLog buildJobRunLog(String jobGroupName, String jobName, JobExecuteResult jobExecuteResult) {
        JobRunLog jobRunLog = new JobRunLog();
        jobRunLog.setLogId(CodeGenerator.getUUID32());
        jobRunLog.setJobName(jobName);
        jobRunLog.setScheduler(JobSchedulerManager.getHostUrl());
        jobRunLog.setBeginTime(jobExecuteResult.getBeginTime());
        jobRunLog.setEndTime(jobExecuteResult.getEndTime());
        jobRunLog.setResultType(jobExecuteResult.getStatusCode());
        jobRunLog.setResultInfo(jobExecuteResult.getSummaryInfo());
        return jobRunLog;
    }

    public String getName() {
        return JOB_RUN_LOGGER;
    }
}
