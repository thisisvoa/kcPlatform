package com.kcp.platform.bpm.entity;


public class BpmDefVar{

	/**
	 * 变量ID
	 */
	private String varId;

	/**
	 * 流程定义ID
	 */
	private String defId;

	/**
	 * 变量名称
	 */
	private String varName;

	/**
	 * 变量Key
	 */
	private String varKey;

	/**
	 * 变量数据类型
	 */
	private String varDataType;

	/**
	 * 默认值
	 */
	private String defValue;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 
	 */
	private String actDeployId;

	/**
	 * 作用域
	 */
	private String varScope;


	public String getVarId(){
		return this.varId;
	}
	
	public void setVarId(String varId){
		this.varId = varId;
	}

	public String getDefId(){
		return this.defId;
	}
	
	public void setDefId(String defId){
		this.defId = defId;
	}

	public String getVarName(){
		return this.varName;
	}
	
	public void setVarName(String varName){
		this.varName = varName;
	}

	public String getVarKey(){
		return this.varKey;
	}
	
	public void setVarKey(String varKey){
		this.varKey = varKey;
	}

	public String getVarDataType(){
		return this.varDataType;
	}
	
	public void setVarDataType(String varDataType){
		this.varDataType = varDataType;
	}

	public String getDefValue(){
		return this.defValue;
	}
	
	public void setDefValue(String defValue){
		this.defValue = defValue;
	}

	public String getNodeName(){
		return this.nodeName;
	}
	
	public void setNodeName(String nodeName){
		this.nodeName = nodeName;
	}

	public String getNodeId(){
		return this.nodeId;
	}
	
	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getActDeployId(){
		return this.actDeployId;
	}
	
	public void setActDeployId(String actDeployId){
		this.actDeployId = actDeployId;
	}

	public String getVarScope(){
		return this.varScope;
	}
	
	public void setVarScope(String varScope){
		this.varScope = varScope;
	}
}
