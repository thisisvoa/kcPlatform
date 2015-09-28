package com.kcp.platform.base.job;

import org.quartz.Trigger;

import java.util.List;

/**
 *  提供一个或多个触发器（即任务调度的规则）用于任务的调度。
 */
public interface BindTrigger {
    /**
     * 任务调度的触发器，即任务调度规则
     * @return
     */
    List<Trigger> getTriggers();

    /**
     * 如果任务即实现本接口，又有t_job_config中配置了触发器，则使用该方法
     * 判断两者的优先级。如果返回true，表示使用该接口所指定的触发器，否则使用t_job_config中定义的触发器。
     * @return
     */
    boolean isTopPriority();
}
