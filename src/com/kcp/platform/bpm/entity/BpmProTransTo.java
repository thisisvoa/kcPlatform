package com.kcp.platform.bpm.entity;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BpmProTransTo{

	/**
	 * 
	 */
	private String id;

	/**
	 * ACT流程实例ID
	 */
	private String actInstId;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 流转类型
	 */
	private Integer transType;

	/**
	 * 
	 */
	private Integer action;

	/**
	 * 创建用户
	 */
	private String createUserId;

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

	public Integer getTransType(){
		return this.transType;
	}
	
	public void setTransType(Integer transType){
		this.transType = transType;
	}

	public Integer getAction(){
		return this.action;
	}
	
	public void setAction(Integer action){
		this.action = action;
	}

	public String getCreateUserId(){
		return this.createUserId;
	}
	
	public void setCreateUserId(String createUserId){
		this.createUserId = createUserId;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof BpmProTransTo)) {
			return false;
		}
		BpmProTransTo rhs = (BpmProTransTo) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.taskId, rhs.taskId)
				.append(this.transType, rhs.transType)
				.append(this.action, rhs.action)
				.append(this.createUserId, rhs.createUserId)
				.append(this.createTime, rhs.createTime)
				.append(this.actInstId, rhs.actInstId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.taskId).append(this.transType).append(this.action)
				.append(this.createUserId).append(this.createTime)
				.append(this.actInstId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("taskId", this.taskId)
				.append("transType", this.transType)
				.append("createUserId", this.createUserId)
				.append("createtime", this.createTime)
				.append("action", this.action)
				.append("actInstId", this.actInstId).toString();
	}
}
