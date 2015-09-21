/**
 * @(#)com.casic27.platform.common.log.entity.InterfaceLog
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

public class InterfaceLog{

	/**
	 * 日志ID，记录唯一标示
	 */
	private String logId;

	/**
	 * 调用时间
	 */
	private Date callTime;

	/**
	 * 服务请求方名称
	 */
	private String callerName;

	/**
	 * 终端标识(IP或移动设备序列号)
	 */
	private String terminalId;

	/**
	 * 返回数据条目数
	 */
	private long resultCount;

	/**
	 * 调用接口描述
	 */
	private String interfaceDesc;
	
	/**
	 * 调用用户所在单位ID
	 */
	private String orgId;

	/**
	 * 调用用户所在单位编号
	 */
	private String orgNo;

	/**
	 * 调用用户所在单位名称
	 */
	private String orgName;
	
	/**
	 * 调用用户Id
	 */
	private String userId;

	/**
	 * 调用用户名
	 */
	private String userName;

	/**
	 * 调用用户账号
	 */
	private String loginId;

	/**
	 * 调用结果（0:失败;1:成功）
	 */
	private String callSuccess;

	/**
	 * 调用参数记录
	 */
	private String paramVariable;


	public String getLogId(){
		return this.logId;
	}
	
	public void setLogId(String logId){
		this.logId = logId;
	}

	public Date getCallTime(){
		return this.callTime;
	}
	
	public void setCallTime(Date callTime){
		this.callTime = callTime;
	}

	public String getCallerName(){
		return this.callerName;
	}
	
	public void setCallerName(String callerName){
		this.callerName = callerName;
	}

	public String getTerminalId(){
		return this.terminalId;
	}
	
	public void setTerminalId(String terminalId){
		this.terminalId = terminalId;
	}

	public long getResultCount(){
		return this.resultCount;
	}
	
	public void setResultCount(long resultCount){
		this.resultCount = resultCount;
	}

	public String getInterfaceDesc(){
		return this.interfaceDesc;
	}
	
	public void setInterfaceDesc(String interfaceDesc){
		this.interfaceDesc = interfaceDesc;
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

	public String getUserName(){
		return this.userName;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getCallSuccess(){
		return this.callSuccess;
	}
	
	public void setCallSuccess(String callSuccess){
		this.callSuccess = callSuccess;
	}

	public String getParamVariable(){
		return this.paramVariable;
	}
	
	public void setParamVariable(String paramVariable){
		this.paramVariable = paramVariable;
	}
}
