package com.kcp.platform.bpm.service.paticipant;

import java.util.HashMap;
import java.util.Map;

import com.kcp.platform.util.BeanUtils;

/**
 * 节点用户计算参数，供IBpmNodeParticipantCalculation计算时候使用
 * 
 * @author Administrator
 * 
 */
public class CalcVars {
	/**
	 * 流程发起人
	 */
	private String startUserId;
	/**
	 * 上一节点执行人
	 */
	private String prevExecUserId;
	/**
	 * 流程实例ID
	 */
	private String actInstId;
	
	/**
	 * 参数
	 */
	private Map<String, Object> vars = new HashMap<String, Object>();

	public CalcVars() {
		
	}

	public CalcVars(String startUserId, String preExecUserId, String actInstId, Map<String, Object> vars) {
		this.startUserId = startUserId;
		this.prevExecUserId = preExecUserId;
		this.actInstId = actInstId;
		this.vars = vars;
	}

	public String getStartUserId() {
		return this.startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getPrevExecUserId() {
		return this.prevExecUserId;
	}

	public void setPrevExecUserId(String prevExecUserId) {
		this.prevExecUserId = prevExecUserId;
	}

	public String getActInstId() {
		return this.actInstId;
	}

	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}

	public Map<String, Object> getVars() {
		return this.vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public String getStartOrgId() {
		Object obj = getVariable("startOrgId");
		if (BeanUtils.isEmpty(obj)) return "";
		return obj.toString();
	}

	public String getPreStepOrgId() {
		Object obj = getVariable("preOrgId");
		if (BeanUtils.isEmpty(obj)) return "";
		return obj.toString();
	}

	public Object getVariable(String varName) {
		if (this.vars == null)
			return null;
		if (!this.vars.containsKey(varName))
			return null;
		return this.vars.get(varName);
	}
}
