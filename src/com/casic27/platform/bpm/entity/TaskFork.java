/**
 * @(#)com.casic27.platform.bpm.entity.TaskFork
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

public class TaskFork{
	
	public static String TAKEN_PRE = "T";
	
	public static String TAKEN_VAR_NAME = "_token_";

	/**
	 * ID
	 */
	private String taskForkId;

	/**
	 * 流程实例ID
	 */
	private String actInstId;

	/**
	 * 分发任务名称
	 */
	private String forkTaskName;

	/**
	 * 分发任务Key
	 */
	private String forkTaskKey;

	/**
	 * 分发序号
	 */
	private Integer forkSn;

	/**
	 * 分发个数
	 */
	private Integer forkCount;

	/**
	 * 完成个数
	 */
	private Integer fininshCount;

	/**
	 * 分发时间
	 */
	private Date forkTime;

	/**
	 * 汇总任务Key
	 */
	private String joinTaskName;

	/**
	 * 汇总任务名称
	 */
	private String joinTaskKey;

	/**
	 * 分发令牌号 格式如：T_1_1,T_1_2,T_1_3, 或 T_1,T_2,T_3,
	 */
	private String forkTokens;

	/**
	 * 分发令牌前缀 格式为T_ 或格式T_1 或T_1_2等
	 */
	private String forkTokenPre;


	public String getTaskForkId(){
		return this.taskForkId;
	}
	
	public void setTaskForkId(String taskForkId){
		this.taskForkId = taskForkId;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getForkTaskName(){
		return this.forkTaskName;
	}
	
	public void setForkTaskName(String forkTaskName){
		this.forkTaskName = forkTaskName;
	}

	public String getForkTaskKey(){
		return this.forkTaskKey;
	}
	
	public void setForkTaskKey(String forkTaskKey){
		this.forkTaskKey = forkTaskKey;
	}

	public Integer getForkSn(){
		return this.forkSn;
	}
	
	public void setForkSn(Integer forkSn){
		this.forkSn = forkSn;
	}

	public Integer getForkCount(){
		return this.forkCount;
	}
	
	public void setForkCount(Integer forkCount){
		this.forkCount = forkCount;
	}

	public Integer getFininshCount(){
		return this.fininshCount;
	}
	
	public void setFininshCount(Integer fininshCount){
		this.fininshCount = fininshCount;
	}

	public Date getForkTime(){
		return this.forkTime;
	}
	
	public void setForkTime(Date forkTime){
		this.forkTime = forkTime;
	}

	public String getJoinTaskName(){
		return this.joinTaskName;
	}
	
	public void setJoinTaskName(String joinTaskName){
		this.joinTaskName = joinTaskName;
	}

	public String getJoinTaskKey(){
		return this.joinTaskKey;
	}
	
	public void setJoinTaskKey(String joinTaskKey){
		this.joinTaskKey = joinTaskKey;
	}

	public String getForkTokens(){
		return this.forkTokens;
	}
	
	public void setForkTokens(String forkTokens){
		this.forkTokens = forkTokens;
	}

	public String getForkTokenPre(){
		return this.forkTokenPre;
	}
	
	public void setForkTokenPre(String forkTokenPre){
		this.forkTokenPre = forkTokenPre;
	}
}
