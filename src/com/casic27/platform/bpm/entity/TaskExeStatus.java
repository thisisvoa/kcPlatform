package com.casic27.platform.bpm.entity;
/**
 * 任务执行状态
 * @author Administrator
 *
 */
public class TaskExeStatus {
	/**
	 * 执行人ID
	 */
	private String executorId;
	/**
	 * 执行人
	 */
	private String executor;
	/**
	 * 获选用户
	 */
	private String candidateUser;
	
	/**
	 * 是否已读
	 */
	private boolean read;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 单位名称
	 */
	private String mainOrgName;

	public String getExecutorId() {
		return this.executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}

	public String getExecutor() {
		return this.executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getCandidateUser() {
		return this.candidateUser;
	}

	public void setCandidateUser(String candidateUser) {
		this.candidateUser = candidateUser;
	}

	public boolean isRead() {
		return this.read;
	}

	public void setRead(boolean isRead) {
		this.read = isRead;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}

	public String getMainOrgName() {
		return this.mainOrgName;
	}
}
