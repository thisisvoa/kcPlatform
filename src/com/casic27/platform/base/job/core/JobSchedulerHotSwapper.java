package com.casic27.platform.base.job.core;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.AbstractTriggerBeanJob;
import com.casic27.platform.base.job.service.JobSchedulerService;


/**
 * 任务调度服务器热切换监控器，它在每个监控周期中完成以下的工作：<br>
 * 1)如果本任务调度服务器是当前全局任务调度服务器，但是已经不在控制台的服务列表中，则将其置为非当前全局；<br>
 * 2)定期汇报服务器的最后活动时间，并将最后活动时间超限的任务调度服务解除装载；<br>
 * 3)如果还没有全局任务调度服务，则将优先高的活动服务设置为当前全局的任务调度服务。<br>
 */
@Component("jobSchedulerHotSwapper")
public class JobSchedulerHotSwapper extends AbstractTriggerBeanJob {
    
    /**
     * 热切换监控器扫描周期，单位为秒
     */
    private static final int PERIOD_IN_SECONDS = 20;

    /**
     * 热切换监控器的延迟启动时间，单位为秒
     */
    private static final long DELAY_IN_SECONDS = 30;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    public void execute(JobExecutionContext jobExecutionContext) {
        jobSchedulerService.hotSwapJobScheduler();
    }

    /**
     * 设置任务的运行触发器，延迟{@link #DELAY_IN_SECONDS}秒，周期为{@link #PERIOD_IN_SECONDS}秒
     *
     * @return
     */
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
