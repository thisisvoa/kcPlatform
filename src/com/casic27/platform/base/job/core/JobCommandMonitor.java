package com.casic27.platform.base.job.core;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.AbstractTriggerBeanJob;
import com.casic27.platform.base.job.JobSchedulerManager;

/**
 * 任务控制命令监控器，它定时运行，监控控制台是否有发布任务的控制命令，如果有，获取任务最后的那个命令，在本任务
 * 调度管理服务器中执行。
 *
 */
@Component("jobCommandMonitor")
public class JobCommandMonitor extends AbstractTriggerBeanJob {

    /**
     * 热切换监控器扫描周期，单位为秒
     */
    private static final int PERIOD_IN_SECONDS = 10;

    /**
     * 热切换监控器的延迟启动时间，单位为秒
     */
    private static final long DELAY_IN_SECONDS = 30;

    @Autowired
    private JobSchedulerManager jobSchedulerManager;

    /**
     * 执行任务控制命令
     * @param jobExecutionContext
     */
    public void execute(JobExecutionContext jobExecutionContext) {
        jobSchedulerManager.executeJobCommand();
    }

    public List<Trigger> getTriggers() {
        Trigger trigger = new SimpleTrigger(this.getJobName(), this.getJobGroupName(),
                SimpleTrigger.REPEAT_INDEFINITELY, (long) PERIOD_IN_SECONDS * 1000);
        trigger.setStartTime(new Date(System.currentTimeMillis() + (DELAY_IN_SECONDS * 1000)));
        return asTriggerList(trigger);
    }

    @Override
    public boolean isTopPriority() {
        return true;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public boolean isNeedLog() {
        return false;
    }
}
