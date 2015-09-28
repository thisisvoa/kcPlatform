package com.kcp.platform.base.job.entity;

import java.util.Date;

import com.kcp.platform.core.entity.BaseEntity;

/**
 * 任务运行日志
 */
public class JobRunLog extends BaseEntity {
	/**
	 * 主键ID
	 */
    private String logId;
    
    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务调度管理器，格式为IP:PORT
     */
    private String scheduler;

    /**
     * 任务执行开始时间
     */
    private Date beginTime;

    /**
     * 任务执行结束时间
     */
    private Date endTime;

    /**
     * 执行结果类型
     */
    private String resultType;
    
    /**
     * 操作结果信息
     */
    private String resultInfo;
    
    /**
     * 创建时间
     */
    private String createTime;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 任务执行结果类型
     */
    public static class ResultType {
        /**
         * 执行成功
         */
        public static final String SUCCESSFUL = "1";

        /**
         * 执行失败
         */
        public static final String FAILURE = "2";
    }
}