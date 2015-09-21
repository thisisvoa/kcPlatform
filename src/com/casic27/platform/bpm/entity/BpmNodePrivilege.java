/**
 * @(#)com.casic27.platform.bpm.entity.BpmNodePrivilege
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


public class BpmNodePrivilege{

	/**
	 * 
	 */
	private String privilegeId;

	/**
	 * 流程定义ID
	 */
	private String actDefId;

	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 特权类型
	 */
	private Short privilegeMode;

	/**
	 * 用户类型
	 */
	private Short userType;

	/**
	 * 参与者名称
	 */
	private String cmpNames;

	/**
	 * 参与者ID
	 */
	private String cmpIds;


	public String getPrivilegeId(){
		return this.privilegeId;
	}
	
	public void setPrivilegeId(String privilegeId){
		this.privilegeId = privilegeId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getNodeId(){
		return this.nodeId;
	}
	
	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public Short getPrivilegeMode(){
		return this.privilegeMode;
	}
	
	public void setPrivilegeMode(Short privilegeMode){
		this.privilegeMode = privilegeMode;
	}

	public Short getUserType(){
		return this.userType;
	}
	
	public void setUserType(Short userType){
		this.userType = userType;
	}

	public String getCmpNames(){
		return this.cmpNames;
	}
	
	public void setCmpNames(String cmpNames){
		this.cmpNames = cmpNames;
	}

	public String getCmpIds(){
		return this.cmpIds;
	}
	
	public void setCmpIds(String cmpIds){
		this.cmpIds = cmpIds;
	}
}
