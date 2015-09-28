package com.kcp.platform.base.job.entity;

import com.kcp.platform.core.entity.BaseEntity;

import java.util.Date;

/**
 * 功能说明：
 */
public class JobCommand extends BaseEntity {
	
    private String commandId;
    
    /**
     * 任务ID
     */
    private String jobId;
    
    /**
     * 任务分组名
     */
    private String jobGroupName;

    /**
     * 任务名
     */
    private String jobName;
    
    /**
     * 1：卸载 2：装载
     */
    private String ctrlCommand;

    /**
     * 命令发布时间（数据库时间，精确到秒）
     */
    private Date issueTime;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getFullJobName() {
        return this.jobGroupName+"."+this.jobName;
    }


    public String getCtrlCommand() {
        return ctrlCommand;
    }

    public void setCtrlCommand(String ctrlCommand) {
        this.ctrlCommand = ctrlCommand;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
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

    /**
     * 是否任务开始
     * @return
     */
    public boolean isStartJobCommand(){
        return CtrlCommand.START.equals(this.getCtrlCommand());
    }

    /**
     * 控制命令静态类
     */
    public static class CtrlCommand {
        /**
         * 装载
         */
        public static String START = "1";
        /**
         * 卸载
         */
        public static String STOP = "2";
    }
}
