package com.casic27.platform.base.job.entity;

import com.casic27.platform.core.entity.BaseEntity;


/**
 * 任务活动状态信息类
 */
public class JobActiveStatus extends BaseEntity{
    /**
     * 任务分组名
     */
    private String jobGroupName;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务活动状态
     */
    private String activeStatus;

    /**
     * 任务活动状态
     * @param jobGroupName
     * @param jobName
     * @param activeStatus
     */
    public JobActiveStatus(String jobGroupName, String jobName, String activeStatus) {
        this.jobGroupName = jobGroupName;
        this.jobName = jobName;
        this.activeStatus = activeStatus;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
}
