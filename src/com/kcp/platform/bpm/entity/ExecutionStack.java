package com.kcp.platform.bpm.entity;

import java.util.Date;

public class ExecutionStack{
	
	public static final Short MULTI_TASK = Short.valueOf((short)1);

	public static final Short COMMON_TASK = Short.valueOf((short)0);
	/**
	 * ID
	 */
	private String stackId;

	/**
	 * 
	 */
	private String actDefId;

	/**
	 * 
	 */
	private String nodeId;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 执行人IDS，如1,2,3
	 */
	private String assignees;

	/**
	 * 是否多实例1=是 0=否
	 */
	private Short isMultiTask = COMMON_TASK;

	/**
	 * 父ID
	 */
	private String parentId;

	/**
	 * ACT流程实例ID
	 */
	private String actInstId;

	/**
	 * 
	 */
	private String taskIds;

	/**
	 * 节点路径格式如：0.1.2. 其中2则为本行记录的主键值
	 */
	private String nodePath;

	/**
	 * 节点层
	 */
	private Integer depth;

	/**
	 * 针对分发任务时，携带的令牌，方便查找其父任务堆栈
	 */
	private String taskToken;


	public String getStackId(){
		return this.stackId;
	}
	
	public void setStackId(String stackId){
		this.stackId = stackId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getNodeId(){
		return this.nodeId;
	}
	
	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getNodeName(){
		return this.nodeName;
	}
	
	public void setNodeName(String nodeName){
		this.nodeName = nodeName;
	}

	public Date getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}

	public Date getEndTime(){
		return this.endTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}

	public String getAssignees(){
		return this.assignees;
	}
	
	public void setAssignees(String assignees){
		this.assignees = assignees;
	}

	public Short getIsMultiTask(){
		return this.isMultiTask;
	}
	
	public void setIsMultiTask(Short isMultiTask){
		this.isMultiTask = isMultiTask;
	}

	public String getParentId(){
		return this.parentId;
	}
	
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getTaskIds(){
		return this.taskIds;
	}
	
	public void setTaskIds(String taskIds){
		this.taskIds = taskIds;
	}

	public String getNodePath(){
		return this.nodePath;
	}
	
	public void setNodePath(String nodePath){
		this.nodePath = nodePath;
	}

	public Integer getDepth(){
		return this.depth;
	}
	
	public void setDepth(Integer depth){
		this.depth = depth;
	}

	public String getTaskToken(){
		return this.taskToken;
	}
	
	public void setTaskToken(String taskToken){
		this.taskToken = taskToken;
	}
}
