/**
 * @(#)com.casic27.platform.bpm.entity.NodeSign
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


public class BpmNodeSign{
	public static final Short VOTE_TYPE_PERCENT = Short.valueOf((short) 1);

	public static final Short VOTE_TYPE_ABSOLUTE = Short.valueOf((short) 2);

	public static final Short FLOW_MODE_DIRECT = Short.valueOf((short) 1);

	public static final Short FLOW_MODE_WAITALL = Short.valueOf((short) 2);

	public static final Short DECIDE_TYPE_PASS = Short.valueOf((short) 1);

	public static final Short DECIDE_TYPE_REFUSE = Short.valueOf((short) 2);
	   
	/**
	 * ID
	 */
	private String signId;


	/**
	 * act流程定义ID
	 */
	private String actDefId;

	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 票数
	 */
	private Integer voteAmount;

	/**
	 * 决策方式1：通过 2：拒绝
	 */
	private Short decideType = Short.valueOf((short)0);

	/**
	 * 投票类型：1=百分比 2=绝对票数
	 */
	private Short voteType = Short.valueOf((short)0);

	/**
	 * 后续处理模式 1=直接处理 2=等待所有人投票
	 */
	private Short flowMode = Short.valueOf((short)0);

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
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

	public Integer getVoteAmount() {
		return voteAmount;
	}

	public void setVoteAmount(Integer voteAmount) {
		this.voteAmount = voteAmount;
	}

	public Short getDecideType() {
		return decideType;
	}

	public void setDecideType(Short decideType) {
		this.decideType = decideType;
	}

	public Short getVoteType() {
		return voteType;
	}

	public void setVoteType(Short voteType) {
		this.voteType = voteType;
	}

	public Short getFlowMode() {
		return flowMode;
	}

	public void setFlowMode(Short flowMode) {
		this.flowMode = flowMode;
	}

}
