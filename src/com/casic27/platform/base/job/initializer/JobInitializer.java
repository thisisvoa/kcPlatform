package com.casic27.platform.base.job.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.JobSchedulerManager;
import com.casic27.platform.sys.initialize.AbstractInitializer;


/**
 * <br>
 * <b>类描述:</b> <p/>
 * <pre>
 *  任务调度初始化
 * </pre>
 */
@Component
public class JobInitializer extends AbstractInitializer
{
    @Autowired
    private JobSchedulerManager jobSchedulerManager;

    @Override
    public void init() {
        jobSchedulerManager.initialize();        
    }

    public String getDesc() {
        return "任务调度器";
    }
}
