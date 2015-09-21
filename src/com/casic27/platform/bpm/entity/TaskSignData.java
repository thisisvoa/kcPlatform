/**
 * @(#)com.casic27.platform.bpm.entity.TaskSignData
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

public class TaskSignData{
	/**
	 * 同意
	 */
	public static Short AGREE = Short.valueOf((short) 1);
	
	/**
	 * 拒绝
	 */
	public static Short REFUSE = Short.valueOf((short) 2);

	/**
	 * 弃权
	 */
	public static Short ABORT = Short.valueOf((short) 0);
	
	/**
	 * 
	 */
	public static Short BACK = Short.valueOf((short) 3);

	/**
	 * 完成
	 */
	public static Short COMPLETED = Short.valueOf((short) 1);

	/**
	 * 未完成
	 */
	public static Short NOT_COMPLETED = Short.valueOf((short) 0);
	
	/**
	 * ID
	 */
	private String dataId;

	/**
	 * Activiti流程定义ID
	 */
	private String actDefId;

	/**
	 * Activiti流程实例ID
	 */
	private String actInstId;

	/**
	 * 流程节点名称
	 */
	private String nodeName;

	/**
	 * 流程节点ID
	 */
	private String nodeId;

	/**
	 * 会签任务Id
	 */
	private String taskId;

	/**
	 * 投票人ID
	 */
	private String voteUserId;

	/**
	 * 投票人名
	 */
	private String voteUserName;

	/**
	 * 投票时间
	 */
	private Date voteTime;

	/**
	 * 是否同意： 0=弃权票 1=同意2=拒绝，跟task_sign中的decideType是一样
	 */
	private Short isAgree;

	/**
	 * 投票意见内容
	 */
	private String content;

	/**
	 * 投票次数
	 */
	private Integer signNums;

	/**
	 * 是否完成1=完成 0=未完成
	 */
	private Short isCompleted;

	/**
	 * 批量
	 */
	private Integer batch;
	
	protected Long groupNo;


	public String getDataId(){
		return this.dataId;
	}
	
	public void setDataId(String dataId){
		this.dataId = dataId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getNodeName(){
		return this.nodeName;
	}
	
	public void setNodeName(String nodeName){
		this.nodeName = nodeName;
	}

	public String getNodeId(){
		return this.nodeId;
	}
	
	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}

	public String getVoteUserId(){
		return this.voteUserId;
	}
	
	public void setVoteUserId(String voteUserId){
		this.voteUserId = voteUserId;
	}

	public String getVoteUserName(){
		return this.voteUserName;
	}
	
	public void setVoteUserName(String voteUserName){
		this.voteUserName = voteUserName;
	}

	public Date getVoteTime(){
		return this.voteTime;
	}
	
	public void setVoteTime(Date voteTime){
		this.voteTime = voteTime;
	}

	public Short getIsAgree(){
		return this.isAgree;
	}
	
	public void setIsAgree(Short isAgree){
		this.isAgree = isAgree;
	}

	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		this.content = content;
	}

	public Integer getSignNums(){
		return this.signNums;
	}
	
	public void setSignNums(Integer signNums){
		this.signNums = signNums;
	}

	public Short getIsCompleted(){
		return this.isCompleted;
	}
	
	public void setIsCompleted(Short isCompleted){
		this.isCompleted = isCompleted;
	}

	public Integer getBatch(){
		return this.batch;
	}
	
	public void setBatch(Integer batch){
		this.batch = batch;
	}
	
	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
}
