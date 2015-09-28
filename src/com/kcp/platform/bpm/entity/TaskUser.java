package com.kcp.platform.bpm.entity;

/**
 * 流程节点用户
 * @author Administrator
 *
 */
public class TaskUser {
	
	protected String id;
	
	protected Integer reversion;
	/**
	 * act流程实例ID
	 */
	protected String groupId;
	
	/**
	 * 用户类型
	 */
	protected String type;
	
	/**
	 * 用户ID
	 */
	protected String userId;
	
	/**
	 * 任务ID
	 */
	protected String taskId;
	
	/**
	 * 流程实例ID
	 */
	protected String procInstId;
	
	/**
	 * 流程定义ID
	 */
	protected String procDefId;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getReversion() {
		return this.reversion;
	}

	public void setReversion(Integer reversion) {
		this.reversion = reversion;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
}
