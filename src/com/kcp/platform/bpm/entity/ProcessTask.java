package com.kcp.platform.bpm.entity;

import java.util.Date;

/**
 * 任务节点扩展
 */
public class ProcessTask {
	
	private String id;
	
	private String name;
	
	private String subject;
	
	private String parentTaskId;
	
	private String description;
	
	private String priority;
	
	private Date createTime;
	
	private String owner;
	
	private String assignee;
	
	private String delegationState;
	
	private String executionId;
	
	private String processInstanceId;
	
	private String processDefinitionId;
	
	private String taskDefinitionKey;
	
	private Date dueDate;
	
	private Integer revision;
	
	private String processName;
	
	private String taskUrl;
	
	private String status;
	
	private String type;
	
	private Integer allowDivert;
	
	private Integer ischeck;
	
	private String defId;
	
	private Integer allowBatchApprove;
	
	private String runId;
	
	private String typeId;
	
	private String typeName;
	
	private String orgName;
	
	private String tagIds;
	
	private String creatorId;
	
	private String creator;
	
	private Boolean isAgent = Boolean.valueOf(false);

	private Boolean isDivert = Boolean.valueOf(false);
	
	private Short taskStatus;
	
	private String codebefore;
	
	private Integer hasRead = Integer.valueOf(0);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAllowDivert() {
		return allowDivert;
	}

	public void setAllowDivert(Integer allowDivert) {
		this.allowDivert = allowDivert;
	}

	public Integer getIscheck() {
		return ischeck;
	}

	public void setIscheck(Integer ischeck) {
		this.ischeck = ischeck;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public Integer getAllowBatchApprove() {
		return allowBatchApprove;
	}

	public void setAllowBatchApprove(Integer allowBatchApprove) {
		this.allowBatchApprove = allowBatchApprove;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Boolean getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}

	public Boolean getIsDivert() {
		return isDivert;
	}

	public void setIsDivert(Boolean isDivert) {
		this.isDivert = isDivert;
	}

	public Short getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Short taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getCodebefore() {
		return codebefore;
	}

	public void setCodebefore(String codebefore) {
		this.codebefore = codebefore;
	}

	public Integer getHasRead() {
		return hasRead;
	}

	public void setHasRead(Integer hasRead) {
		this.hasRead = hasRead;
	}
}
