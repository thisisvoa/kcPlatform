/**
 * @(#)com.casic27.platform.bpm.entity.NodeParticipant
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

/**
 * 活动参与者
 * @author Administrator
 *
 */
public class BpmNodeParticipant{

	/**
	 * ID
	 */
	private String participantId;
	
	/**
	 * 流程设置ID
	 */
	private String configId;

	/**
	 * 流程定义ID
	 */
	private String defId;
	
	/**
	 * activiti流程定义ID
	 */
	private String actDefId;
	
	/**
	 * 流程节点ID
	 */
	private String nodeId;
	
	/**
	 * 参与者类型
	 */
	private String participantType;

	/**
	 * 参与者值
	 */
	private String participan;
	
	/**
	 * 参与值的描述
	 */
	private String participanDesc;
	
	/**
	 * 运算类型
	 */
	private String computeType;
	
	/**
	 * 排序号
	 */
	private Integer sn;
	
	/**
	 * 抽取用户
	 */
	private Short extractUser;


	public String getParticipantId(){
		return this.participantId;
	}
	
	public void setParticipantId(String participantId){
		this.participantId = participantId;
	}
	
	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParticipantType(){
		return this.participantType;
	}
	
	public void setParticipantType(String participantType){
		this.participantType = participantType;
	}

	public String getParticipan(){
		return this.participan;
	}
	
	public void setParticipan(String participan){
		this.participan = participan;
	}
	
	public String getParticipanDesc() {
		return participanDesc;
	}

	public void setParticipanDesc(String participanDesc) {
		this.participanDesc = participanDesc;
	}
	
	public String getComputeType() {
		return computeType;
	}

	public void setComputeType(String computeType) {
		this.computeType = computeType;
	}
	
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}
	
	public Short getExtractUser() {
		return extractUser;
	}

	public void setExtractUser(Short extractUser) {
		this.extractUser = extractUser;
	}
}
