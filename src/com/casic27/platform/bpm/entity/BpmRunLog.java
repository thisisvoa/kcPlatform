/**
 * @(#)com.casic27.platform.bpm.entity.BpmRunLog
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

public class BpmRunLog{
	/**
	 * 启动流程
	 */
	public static final Integer OPERATOR_TYPE_START = Integer.valueOf(0);
	
	/**
	 * 交办
	 */
	public static final Integer OPERATOR_TYPE_DELEGATE = Integer.valueOf(1);
 
	/**
	 * 追回
	 */
	public static final Integer OPERATOR_TYPE_RETRIEVE = Integer.valueOf(2);
	
	/**
	 * 删除流程实例
	 */
	public static final Integer OPERATOR_TYPE_DELETEINSTANCE = Integer.valueOf(3);
	
	/**
	 * 同意(投票)
	 */
	public static final Integer OPERATOR_TYPE_AGREE = Integer.valueOf(4);
	
	/**
	 * 反对(投票)
	 */
	public static final Integer OPERATOR_TYPE_OBJECTION = Integer.valueOf(5);
	
	/**
	 * 弃权(投票)
	 */
	public static final Integer OPERATOR_TYPE_ABSTENTION = Integer.valueOf(6);
 
	/**
	 * 补签
	 */
	public static final Integer OPERATOR_TYPE_SIGN = Integer.valueOf(7);
	
	/**
	 * 驳回
	 */
	public static final Integer OPERATOR_TYPE_REJECT = Integer.valueOf(8);
	
	/**
	 * 驳回到发起人
	 */
	public static final Integer OPERATOR_TYPE_REJECT2SPONSOR = Integer.valueOf(9);
	
	/**
	 * 删除任务
	 */
	public static final Integer OPERATOR_TYPE_DELETETASK = Integer.valueOf(10);
	
	/**
	 * 代理任务
	 */
	public static final Integer OPERATOR_TYPE_AGENT = Integer.valueOf(11);
	
	/**
	 * 锁定任务
	 */
	public static final Integer OPERATOR_TYPE_LOCK = Integer.valueOf(13);
 
	/**
	 * 任务解锁
	 */
	public static final Integer OPERATOR_TYPE_UNLOCK = Integer.valueOf(14);
	
	/**
	 * 添加意见
	 */
	public static final Integer OPERATOR_TYPE_ADDOPINION = Integer.valueOf(15);
	
	/**
	 * 任务指派
	 */
	public static final Integer OPERATOR_TYPE_ASSIGN = Integer.valueOf(16);
	
	/**
	 * 设定所有人
	 */
	public static final Integer OPERATOR_TYPE_SETOWNER = Integer.valueOf(17);
	
	/**
	 * 结束任务
	 */
	public static final Integer OPERATOR_TYPE_ENDTASK = Integer.valueOf(18);
	
	/**
	 * 修改执行路径
	 */
	public static final Integer OPERATOR_TYPE_CHANGEPATH = Integer.valueOf(19);
 
	/**
	 * 
	 */
	public static final Integer OPERATOR_TYPE_BACK = Integer.valueOf(20);
 
	/**
	 * 终止流程
	 */
	public static final Integer OPERATOR_TYPE_ENDINSTANCE = Integer.valueOf(21);
	
	/**
	 * 保存草稿
	 */
	public static final Integer OPERATOR_TYPE_SAVEFORM = Integer.valueOf(22);
	
	/**
	 * 删除草稿
	 */
	public static final Integer OPERATOR_TYPE_DELETEFORM = Integer.valueOf(23);
	
	/**
	 * 会签直接通过
	 */
	public static final Integer OPERATOR_TYPE_SIGN_PASSED = Integer.valueOf(24);
	
	/**
	 * 会签直接不通过
	 */
	public static final Integer OPERATOR_TYPE_SIGN_NOT_PASSED = Integer.valueOf(25);
	/**
	 * 
	 */
	private String id;

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

	/**
	 * 操作类型
	 */
	private Integer operatorType;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 流程实例ID
	 */
	private String runId;

	/**
	 * 流程实例标题
	 */
	private String processSubject;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
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

	public Integer getOperatorType(){
		return this.operatorType;
	}
	
	public void setOperatorType(Integer operatorType){
		this.operatorType = operatorType;
	}

	public String getMemo(){
		return this.memo;
	}
	
	public void setMemo(String memo){
		this.memo = memo;
	}

	public String getRunId(){
		return this.runId;
	}
	
	public void setRunId(String runId){
		this.runId = runId;
	}

	public String getProcessSubject(){
		return this.processSubject;
	}
	
	public void setProcessSubject(String processSubject){
		this.processSubject = processSubject;
	}
}
