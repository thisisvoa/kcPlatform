package com.casic27.platform.base.job;

import org.quartz.JobExecutionContext;

/**
 * 　　该接口是任务的高级抽象接口，该类仅定义了任务的执行逻辑。其调度规则即可以通过实现{@link com.epgis.platform.module.job.BindTrigger}接口提供，还
 * 可以在{@link JobSchedulerManager}中通过<code>addTrigger()</code>定义为任务定义触发器。系统将按照触发器中指定的任务分组
 * 名及任务名自动绑定任务。<br/>
 * 　　<p>任务的实现类是一个Bean，其Bean的名称格式为<jobGroupName>.<jobName>或<jobName> 它将通过Spring的自动注入机制在容器
 * 启动时注册到{@link JobSchedulerManager}中，并自动和
 * {@link JobSchedulerManager#allJobTriggerMap}
 * 中的触发器进行匹配，如果发现有匹配的触发器即用这个触发器调度该任务。
 */
public interface Job {
    /**
     *   任务执行逻辑，实现者在该方法中定义被调度执行的业务逻辑。jobExecutionContext自动绑定一个
     * {@link com.epgis.platform.module.job.JobExecuteResult}
     * 可以通过如下的代码获取这个对象：
     *   <div style="background-color:#EFEFEF;" >
     *   <pre>
     *      JobExecuteResult jobExecuteResult = (JobExecuteResult)jobExecutionContext.get(JobExecuteResult.EXECUTE_RESULT);
     *      jobExecuteResult.putResult("test1","1");
     *      jobExecuteResult.putResult("test2","2");
     *   </pre>
     *   </div>
     *   {@link com.epgis.platform.module.job.JobExecuteResult}其它的属性信息，beginTime,endTime,statusCode等属性由SchedulerManager自动维护。
     * 任务实现者无需关注。
     *   <p>在任务执行完成时，所有保存在{@link JobExecuteResult#results}中的信息都会格式化为JSON串保存到T_JOB_RUN_LOG执行
     * 日志表中。除非{@link #isNeedLog()}返回false。
     * @param jobExecutionContext
     */
     void execute(JobExecutionContext jobExecutionContext);

     /**
     *获取任务组名称
     * @return
     */
     String getJobGroupName();

    /**
     *获取任务对应的名称 
     * @return
     */
     String getJobName();
    

    /**
     *      只有在任务调度服务节点时才执行，服务节点是否是任务调度节点可以通过<code>JOB_SCHEDULE_SERVERS</code>系统参数指定，如
     * <code>JOB_SCHEDULE_SERVERS=198.168.1.4:9080,198.168.1.4:9081</code>，则只有服务节点的Web服务是工作在对应IP及端口上时才是任务
     * 调度服务节点。
     * @return
     */
     boolean isGlobal();

    /**
     *   是否需要记录任务运行日志。如果返回<code>true</code>，系统将记录任务每次运行的日志，保存到日志库
     * 的T_JOB_RUN_LOG表中。
     * @return
     */
     boolean isNeedLog();
}
