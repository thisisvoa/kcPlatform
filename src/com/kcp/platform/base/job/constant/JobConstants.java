package com.kcp.platform.base.job.constant;

import java.util.ResourceBundle;

/**
 * <br>
 * <b>公用常量 </b>
 */
public class JobConstants {
    
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("conf.application");
        START_JOB_SCHEDULER_WHEN_APP_START = new Boolean(resourceBundle.getString("startJobSchedulerWhenAppStart"));
    }

    /**
     * 是否在系统启动时启动任务调度器
     */
    public static final Boolean START_JOB_SCHEDULER_WHEN_APP_START;
}
