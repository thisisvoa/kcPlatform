package com.kcp.platform.bpm.entity;



public class BpmNodeButton{

	/**
	 * ID
	 */
	private String btnId;
	
	/**
	 * 节点配置ID
	 */
	private String defId;
	
	/**
	 * Activiti流程定义ID
	 */
	private String actDefId;
	
	/**
	 * 节点ID
	 */
	private String nodeId;
	
	/**
	 * 节点类型
	 */
	private String nodeType;
	
	/**
	 * 按钮名称
	 */
	private String btnName;

	/**
	 * 操作类型
	 */
	private String type;


	/**
	 * 前置脚本
	 */
	private String beforeHandler;

	/**
	 * 前置脚本
	 */
	private String afterHandler;
	
	/**
	 * 排序号
	 */
	private Integer sn;
	
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getBtnId(){
		return this.btnId;
	}
	
	public void setBtnId(String btnId){
		this.btnId = btnId;
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

	public String getBtnName(){
		return this.btnName;
	}
	
	public void setBtnName(String btnName){
		this.btnName = btnName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBeforeHandler() {
		return beforeHandler;
	}

	public void setBeforeHandler(String beforeHandler) {
		this.beforeHandler = beforeHandler;
	}

	public String getAfterHandler() {
		return afterHandler;
	}

	public void setAfterHandler(String afterHandler) {
		this.afterHandler = afterHandler;
	}
}
