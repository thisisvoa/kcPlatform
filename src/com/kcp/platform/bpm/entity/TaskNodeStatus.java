package com.kcp.platform.bpm.entity;

import java.util.ArrayList;
import java.util.List;

public class TaskNodeStatus {
	/**
	 * 流程实例ID
	 */
	private String actInstId;
	
	/**
	 * 节点ID
	 */
	private String taskKey;
	
	/**
	 * 最后的状态
	 */
	private Short lastCheckStatus = TaskOpinion.STATUS_INIT;

	/**
	 * 节点审批人员
	 */
	private List<TaskOpinion> taskOpinionList = new ArrayList<TaskOpinion>();
	
	/**
	 * 节点执行人
	 */
	private List<TaskExecutor> taskExecutorList = new ArrayList<TaskExecutor>();

	public void setLastCheckStatus(Short lastCheckStatus) {
		this.lastCheckStatus = lastCheckStatus;
	}

	public TaskNodeStatus() {
	}

	public String getActInstId() {
		return this.actInstId;
	}

	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}

	public String getTaskKey() {
		return this.taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public List<TaskOpinion> getTaskOpinionList() {
		return this.taskOpinionList;
	}

	public void setTaskOpinionList(List<TaskOpinion> taskOpinionList) {
		this.taskOpinionList = taskOpinionList;
	}

	public TaskNodeStatus(String actInstId, String taskKey, Short lastCheckStatus, List<TaskOpinion> taskOpinionList,
				List<TaskExecutor> taskExecutorList) {
		this.actInstId = actInstId;
		this.taskKey = taskKey;
		this.lastCheckStatus = lastCheckStatus;
		this.taskOpinionList = taskOpinionList;
		this.taskExecutorList = taskExecutorList;
	}

	public Short getLastCheckStatus() {
		return this.lastCheckStatus;
	}

	public List<TaskExecutor> getTaskExecutorList() {
		return this.taskExecutorList;
	}

	public void setTaskExecutorList(List<TaskExecutor> taskExecutorList) {
		this.taskExecutorList = taskExecutorList;
	}
}
