/**
 * @(#)com.casic27.platform.bpm.entity.BpmTaskExe
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
 
package com.casic27.platform.bpm.entity;

import java.util.Date;

public class BpmTaskExe{
   public static final Integer STATUS_INIT = 0;
   
   public static final Integer STATUS_COMPLETE = 1;
 
   public static final Integer STATUS_CANCEL = 2;
 
   public static final Integer STATUS_OTHER_COMPLETE = 3;
 
   public static final Integer STATUS_BACK = 4;
 
   public static final Integer TYPE_ASSIGNEE = 1;
 
   public static final Integer TYPE_TRANSMIT = 2;
	/**
	 * ID
	 */
	private String id;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 转办(代理)人ID
	 */
	private String assigneeId;

	/**
	 * 转办(代理)人名称
	 */
	private String assigneeName;

	/**
	 * 
	 */
	private String ownerId;

	/**
	 * 
	 */
	private String ownerName;

	/**
	 * 标题
	 */
	private String subject;

	/**
	 * 状态(0:初始状态;1:完成任务;2:取消;3:其他)
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * ACT流程实例ID
	 */
	private String actInstId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务KEY
	 */
	private String taskDefKey;

	/**
	 * 
	 */
	private Date exeTime;

	/**
	 * 
	 */
	private String exeUserId;

	/**
	 * 
	 */
	private String exeUserName;

	/**
	 * 待办类型(1:代理 2:转办)
	 */
	private Integer assignType;

	/**
	 * 流程实例ID
	 */
	private String runId;

	/**
	 * 流程分类ID
	 */
	private String typeId;
	
	/**
	 * 流程定义名称
	 */
	private String processName;
	
	/**
	 * 流程实例创建人
	 */
	private String creatorId;
	
	/**
	 * 流程实例创建人名称
	 */
	private String creator;

	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}

	public String getAssigneeId(){
		return this.assigneeId;
	}
	
	public void setAssigneeId(String assigneeId){
		this.assigneeId = assigneeId;
	}

	public String getAssigneeName(){
		return this.assigneeName;
	}
	
	public void setAssigneeName(String assigneeName){
		this.assigneeName = assigneeName;
	}

	public String getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(String ownerId){
		this.ownerId = ownerId;
	}

	public String getOwnerName(){
		return this.ownerName;
	}
	
	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}

	public String getSubject(){
		return this.subject;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}

	public Integer getStatus(){
		return this.status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

	public String getMemo(){
		return this.memo;
	}
	
	public void setMemo(String memo){
		this.memo = memo;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getTaskName(){
		return this.taskName;
	}
	
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskDefKey(){
		return this.taskDefKey;
	}
	
	public void setTaskDefKey(String taskDefKey){
		this.taskDefKey = taskDefKey;
	}

	public Date getExeTime(){
		return this.exeTime;
	}
	
	public void setExeTime(Date exeTime){
		this.exeTime = exeTime;
	}

	public String getExeUserId(){
		return this.exeUserId;
	}
	
	public void setExeUserId(String exeUserId){
		this.exeUserId = exeUserId;
	}

	public String getExeUserName(){
		return this.exeUserName;
	}
	
	public void setExeUserName(String exeUserName){
		this.exeUserName = exeUserName;
	}

	public Integer getAssignType(){
		return this.assignType;
	}
	
	public void setAssignType(Integer assignType){
		this.assignType = assignType;
	}

	public String getRunId(){
		return this.runId;
	}
	
	public void setRunId(String runId){
		this.runId = runId;
	}

	public String getTypeId(){
		return this.typeId;
	}
	
	public void setTypeId(String typeId){
		this.typeId = typeId;
	}
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
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
}
