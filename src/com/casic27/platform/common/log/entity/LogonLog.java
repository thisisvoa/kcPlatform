/**
 * @(#)com.casic27.platform.common.log.entity.LogonLog
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
import java.util.Date;

public class LogonLog{

	/**
	 * 日志ID
	 */
	private String logId;

	/**
	 * 终端标识(IP或移动设备序列号)
	 */
	private String terminalId;
	
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
	 * 登录时间
	 */
	private Date logonTime;

	/**
	 * 登出时间
	 */
	private Date logoutTime;

	/**
	 * 登录结果(0:登录失败；1：登录成功)
	 */
	private String logonResult;

	/**
	 * 登录用户的SESSION_ID
	 */
	private String sessionId;


	public String getLogId(){
		return this.logId;
	}
	
	public void setLogId(String logId){
		this.logId = logId;
	}

	public String getTerminalId(){
		return this.terminalId;
	}
	
	public void setTerminalId(String terminalId){
		this.terminalId = terminalId;
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

	public Date getLogonTime(){
		return this.logonTime;
	}
	
	public void setLogonTime(Date logonTime){
		this.logonTime = logonTime;
	}

	public Date getLogoutTime(){
		return this.logoutTime;
	}
	
	public void setLogoutTime(Date logoutTime){
		this.logoutTime = logoutTime;
	}

	public String getLogonResult(){
		return this.logonResult;
	}
	
	public void setLogonResult(String logonResult){
		this.logonResult = logonResult;
	}

	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}
}
