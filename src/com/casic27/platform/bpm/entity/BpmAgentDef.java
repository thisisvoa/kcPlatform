/**
 * @(#)com.casic27.platform.bpm.entity.BpmAgentDef
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
