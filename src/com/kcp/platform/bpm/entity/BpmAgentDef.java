package com.kcp.platform.bpm.entity;


public class BpmAgentDef{

	/**
	 * 
	 */
	private String id;

	/**
	 * 
	 */
	private String settingId;

	/**
	 * 
	 */
	private String flowKey;

	/**
	 * 
	 */
	private String flowName;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getSettingId(){
		return this.settingId;
	}
	
	public void setSettingId(String settingId){
		this.settingId = settingId;
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
