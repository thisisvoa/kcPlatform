package com.casic27.platform.base.job;

import org.quartz.Scheduler;

/**
 * @version 1.0
 */
public abstract class AbstractBeanJobListener implements BeanJobListener{

    /**
     * 返回默认的分组
     * @return
     */
    public String getJobGroupName() {
        return Scheduler.DEFAULT_GROUP;
    }
}
