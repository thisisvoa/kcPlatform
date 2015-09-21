package com.casic27.platform.base.job;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.Assert;

/**
 * 　该抽象类使Bean的名称自动转换为任务分组名称和任务名称。这要求Bean的命名满足一定的规范。即只能是以下
 * 两种形式：
 * <pre>
 *   1. <jobGroupName>.<jobName>：其中前者为任务分组名，后者为任务名，两者用.号分隔
 * 　2. <jobName> ：没有分组名，只有任务名，系统将自动使用默认的分组名。
 * </pre>
 */
public abstract class AbstractBeanJob implements Job, BeanNameAware {

    //分隔组名和任务名的字符
    private static final String SEPARATOR =".";

    //任务分组名
    private String jobGroupName;

    //任务名称
    private String jobName;

    /*日志记录器*/
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public  void setBeanName(String beanName) {
        assertJobNameIsRight(beanName);
        //Bean名字的格林为jobGroupName.jobName
        if(beanName != null && beanName.indexOf(SEPARATOR) > 1){
              int dotSeparator = beanName.indexOf(SEPARATOR);
              this.jobGroupName = beanName.substring(0,dotSeparator);
              this.jobName = beanName.substring(dotSeparator+1);
        }else{
             this.jobGroupName = Scheduler.DEFAULT_GROUP;
             this.jobName = beanName;
        }
    }

    /**
     *    对Bean的名称作正确性断言，合法的Bean名称必须为以下两种格式：
     * 1 <jobGroupName>.<jobName>
     * 2 <jobName>
     * @param beanName
     */
    private static void assertJobNameIsRight(String beanName){
        Assert.hasText(beanName,"任务的名称不能为空");
        int pos = beanName.indexOf(SEPARATOR);
        if(pos > 0){//不能拥有多个。
             Assert.isTrue(beanName.indexOf(SEPARATOR,pos+1) == -1,
                    "bean名称["+beanName+"]不合法，"+
                    "任务Bean名称的正确格式必须为<jobGroupName>.<jobName>或<jobName>");
        }
    }

    /**
     *    获取任务的分组名
     * @return
     */
    public  String getJobGroupName() {
        return jobGroupName;
    }

    /**
     *  获取任务的名称
     * @return
     */
    public  String getJobName() {
        return jobName;
    }

    /**
     *    使子类任务默认情况下只会在任务调度服务节点执行，子类可以通过覆盖该方法,返回<code>false</code>。使任务在所有服务
     * 节点上都执行。
     *     全局任务是指其执行逻辑具有全局整体性影响的任务，如定期生成报表，定期批量发送Mail等的任务都属于全局任务。由于在群集
     * 环境下，全局任务只能限定在任务调度管理器上执行，也即全局任务该方法<font color=red>只有</font>返回<code>true</code>。
     *     本地任务是指其执行逻辑只局限于本地服务节点的任务，如刷新本地缓存任务就是一个本地任务。本地任务可以在各个服务节点
     * 上并行执行，它们的执行结果只会限定在集群的本地服务节点上，不会扩散到全局中去。这时，必须覆盖此方法，使其返回为
     * <code>false</code>。
     *
     * 必须返回为<code>true</code>，反之
     * @return
     */
    public boolean isGlobal() {
        return true;
    }

    /**
     *    命名子类任务在默认情况下记录任务的执行日志，子类也可以通过覆盖该方法，返回<code>false</code>。这样任务运行时
     * 就不会记录执行日志了。一般情况下，如果任务执行的时间间隔不会过短，都可以采用此默认方法，以记录任务的执行日志。
     * 如果任务执行周期太短，如每分钟执行一次，为了避免产生过多的执行日志，则可以覆盖此方法关闭任务执行日志记录功能。
     * 
     * @return
     */
    public boolean isNeedLog() {
        return true;
    }

    /**
     * 获取JobExecuteResult对象
     * @param jobExecutionContext
     * @return
     */
    protected JobExecuteResult getJobExecuteResult(JobExecutionContext jobExecutionContext){
        return (JobExecuteResult) jobExecutionContext.get(JobExecuteResult.EXECUTE_RESULT);
    }
}
