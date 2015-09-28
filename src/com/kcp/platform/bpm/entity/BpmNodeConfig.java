package com.kcp.platform.bpm.entity;


public class BpmNodeConfig{
	/**
	 * 任务节点设置
	 */
	public static final String TYPE_TASK = "0";
	
	/**
	 * 开始节点设置
	 */
	public static final String TYPE_GLOBEL = "1";
	
	/**
	 * 无设置表单类型
	 */
	public static final String NODE_FORM_TYPE_DEFAULT = "";
	/**
	 * 在线表单
	 */
	public static final String NODE_FORM_TYPE_ONLINE = "1";
	/**
	 * URL表单
	 */
	public static final String NODE_FORM_TYPE_URL = "2";
	
	/**
	 * 节点类型为
	 */
	public static final String NODE_TYPE_NORMAL = "0";
	 
	public static final String NODE_TYPE_FORK = "1";
	 
	public static final String NODE_TYPE_JOIN = "2";
	   
	/**
	 * ID
	 */
	private String configId;

	/**
	 * 流程定义ID
	 */
	private String defId;
	
	/**
	 * Acitiviti流程定义ID
	 */
	private String actDefId;
	
	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 表单类型
	 */
	private String formType;

	/**
	 * 表单URL
	 */
	private String formUrl;
	
	/**
	 * 在线表单ID
	 */
	private String formDefId;
	
	/**
	 * 表单名称
	 */
	private String formDefName;
	
	/**
	 * 在线表单Key
	 */
	private String formKey;
	
	/**
	 * 节点类型
	 */
	private String nodeType;
	
	/**
	 * 
	 */
	private String joinTaskKey;
	
	/**
	 * 
	 */
	private String joinTaskName;
	
	/**
	 * 前置处理器
	 */
	private String beforeHandler;

	/**
	 * 后置处理器
	 */
	private String afterHandler;

	/**
	 * 是否允许回退
	 */
	private String allowBack;
	
	/**
	 * 设置类型 0.任务节点 1.开始表单 2.默认表单
	 */
	private String setType;
	
	/**
	 * 排序号
	 */
	private Integer sn;
	
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

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	
	public String getFormDefId() {
		return formDefId;
	}

	public void setFormDefId(String formDefId) {
		this.formDefId = formDefId;
	}
	
	public String getFormDefName() {
		return formDefName;
	}

	public void setFormDefName(String formDefName) {
		this.formDefName = formDefName;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getJoinTaskKey() {
		return joinTaskKey;
	}

	public void setJoinTaskKey(String joinTaskKey) {
		this.joinTaskKey = joinTaskKey;
	}

	public String getJoinTaskName() {
		return joinTaskName;
	}

	public void setJoinTaskName(String joinTaskName) {
		this.joinTaskName = joinTaskName;
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

	public String getAllowBack() {
		return allowBack;
	}

	public void setAllowBack(String allowBack) {
		this.allowBack = allowBack;
	}

	public String getSetType() {
		return setType;
	}

	public void setSetType(String setType) {
		this.setType = setType;
	}
	
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}
	
}
