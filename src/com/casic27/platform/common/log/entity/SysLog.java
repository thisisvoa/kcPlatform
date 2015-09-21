/**
 * @(#)com.casic27.platform.common.log.entity.SysLog
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
 
package com.casic27.platform.common.log.entity;

import java.util.Date;

public class SysLog{

	/**
	 * 日志ID，记录唯一标示
	 */
	private String logId;
	
	/**
	 * 登录用户所在单位ID
	 */
	private String orgId;

	/**
	 * 登录用户所在单位编号
	 */
	private String orgNo;

	/**
	 * 登录用户所在单位名称
	 */
	private String orgName;

	/**
	 * 身份证号
	 */
	private String idCard;

	/**
	 * 警号
	 */
	private String policeId;
	
	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 登录用户账号
	 */
	private String loginId;

	/**
	 * 操作时间（如：查询时间、删除时间等）
	 */
	private Date operateTime;

	/**
	 * 终端标识(IP或移动设备序列号)
	 */
	private String terminalId;

	/**
	 * 操作内容,一般用于记录操作调用SQL语句
	 */
	private String operateContent;

	/**
	 * 操作反馈
	 */
	private String operateResult;

	/**
	 * 操作描述
	 */
	private String operateDesc;

	/**
	 * 功能模块名称
	 */
	private String moduleName;

	/**
	 * 访问系统名称(预留字段)
	 */
	private String sysName;

	/**
	 * 日志类型(1:查询;2:新增;3:删除;4:修改)
	 */
	private String logType;


	public String getLogId(){
		return this.logId;
	}
	
	public void setLogId(String logId){
		this.logId = logId;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getOrgNo(){
		return this.orgNo;
	}
	
	public void setOrgNo(String orgNo){
		this.orgNo = orgNo;
	}

	public String getOrgName(){
		return this.orgName;
	}
	
	public void setOrgName(String orgName){
		this.orgName = orgName;
	}

	public String getIdCard(){
		return this.idCard;
	}
	
	public void setIdCard(String idCard){
		this.idCard = idCard;
	}

	public String getPoliceId(){
		return this.policeId;
	}
	
	public void setPoliceId(String policeId){
		this.policeId = policeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getLoginId(){
		return this.loginId;
	}
	
	public void setLoginId(String loginId){
		this.loginId = loginId;
	}

	public Date getOperateTime(){
		return this.operateTime;
	}
	
	public void setOperateTime(Date operateTime){
		this.operateTime = operateTime;
	}

	public String getTerminalId(){
		return this.terminalId;
	}
	
	public void setTerminalId(String terminalId){
		this.terminalId = terminalId;
	}

	public String getOperateContent(){
		return this.operateContent;
	}
	
	public void setOperateContent(String operateContent){
		if(operateContent!=null && operateContent.length()>4000){
			operateContent = operateContent.substring(0,3999);
		}
		this.operateContent = operateContent;
	}

	public String getOperateResult(){
		return this.operateResult;
	}
	
	public void setOperateResult(String operateResult){
		this.operateResult = operateResult;
	}

	public String getOperateDesc(){
		return this.operateDesc;
	}
	
	public void setOperateDesc(String operateDesc){
		this.operateDesc = operateDesc;
	}

	public String getModuleName(){
		return this.moduleName;
	}
	
	public void setModuleName(String moduleName){
		this.moduleName = moduleName;
	}

	public String getSysName(){
		return this.sysName;
	}
	
	public void setSysName(String sysName){
		this.sysName = sysName;
	}

	public String getLogType(){
		return this.logType;
	}
	
	public void setLogType(String logType){
		this.logType = logType;
	}
}
