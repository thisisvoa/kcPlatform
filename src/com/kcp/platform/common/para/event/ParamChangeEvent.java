package com.kcp.platform.common.para.event;

import com.kcp.platform.base.event.BaseEvent;
import com.kcp.platform.common.para.entity.Parameter;

public class ParamChangeEvent extends BaseEvent {
	
	public static final String CHANGE_TYPE_ADD = "add";
	
	public static final String CHANGE_TYPE_UPDATE = "update";
	
	public static final String CHANGE_TYPE_DEL = "del";
	
	public static final String CHANGE_TYPE_SYBZ = "sybz";
	
	/**
	 * 变化的参数
	 */
	public Parameter  param;
	
	/**
	 * 事件类型
	 */
	private String changeType;
	
	public ParamChangeEvent(){
		
	}
	
	public ParamChangeEvent(String changeType){
		this.changeType = changeType;
	}
	
	public ParamChangeEvent(String changeType, Parameter  param){
		this.changeType = changeType;
		this.param = param;
	}
	
	public Parameter getParam() {
		return param;
	}

	public void setParam(Parameter param) {
		this.param = param;
	}
	
	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
}
