package com.kcp.platform.bpm.entity;

import java.util.Date;

public class TaskRead{

	/**
	 * 
	 */
	private String id;

	/**
	 * 流程实例ID
	 */
	private String actInstId;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 创建时间
	 */
	private Date createTime;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}

	public String getUserId(){
		return this.userId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
}
