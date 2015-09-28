package com.kcp.platform.bpm.entity;

import java.util.Date;
import java.util.Date;
import java.util.Date;

public class BpmAgentSetting{
	public static int AUTHTYPE_GENERAL = 0;
	 
	public static int AUTHTYPE_PARTIAL = 1;
	
	public static int ENABLED_YES = 1;
	 
	public static int ENABLED_NO = 0;
	
	/**
	 * ID
	 */
	private String id;

	/**
	 * 代理人ID
	 */
	private String authId;

	/**
	 * 代理人名称
	 */
	private String authName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 代理开始时间
	 */
	private Date startDate;

	/**
	 * 代理结束时间
	 */
	private Date endDate;

	/**
	 * 是否启用
	 */
	private Integer enabled;

	/**
	 * 代理类型
	 */
	private Integer authType;

	/**
	 * 被代理人ID
	 */
	private String agentId;

	/**
	 * 被代理人名称
	 */
	private String agent;

	/**
	 * 流程KEY
	 */
	private String flowKey;

	/**
	 * 流程名称
	 */
	private String flowName;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getAuthId(){
		return this.authId;
	}
	
	public void setAuthId(String authId){
		this.authId = authId;
	}

	public String getAuthName(){
		return this.authName;
	}
	
	public void setAuthName(String authName){
		this.authName = authName;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getStartDate(){
		return this.startDate;
	}
	
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}
	
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	public Integer getEnabled(){
		return this.enabled;
	}
	
	public void setEnabled(Integer enabled){
		this.enabled = enabled;
	}

	public Integer getAuthType(){
		return this.authType;
	}
	
	public void setAuthType(Integer authType){
		this.authType = authType;
	}

	public String getAgentId(){
		return this.agentId;
	}
	
	public void setAgentId(String agentId){
		this.agentId = agentId;
	}

	public String getAgent(){
		return this.agent;
	}
	
	public void setAgent(String agent){
		this.agent = agent;
	}

	public String getFlowKey(){
		return this.flowKey;
	}
	
	public void setFlowKey(String flowKey){
		this.flowKey = flowKey;
	}

	public String getFlowName(){
		return this.flowName;
	}
	
	public void setFlowName(String flowName){
		this.flowName = flowName;
	}
}
