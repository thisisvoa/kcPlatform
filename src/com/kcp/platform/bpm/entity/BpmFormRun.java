package com.kcp.platform.bpm.entity;


public class BpmFormRun{

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 表单定义ID
	 */
	private String formDefId;

	/**
	 * 表单定义key
	 */
	private String formDefKey;

	/**
	 * ACT流程实例ID
	 */
	private String actInstanceId;

	/**
	 * ACT流程定义ID
	 */
	private String actDefId;

	/**
	 * 流程节点id
	 */
	private String actNodeId;

	/**
	 * 流程运行ID
	 */
	private String runId;

	/**
	 * 表单类型0,任务节点 1,开始表单 2,全局表单
	 */
	private String setType;

	/**
	 * 表单类型
	 */
	private String formType;

	/**
	 * 表单URL
	 */
	private String formUrl;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getFormDefId(){
		return this.formDefId;
	}
	
	public void setFormDefId(String formDefId){
		this.formDefId = formDefId;
	}

	public String getFormDefKey(){
		return this.formDefKey;
	}
	
	public void setFormDefKey(String formDefKey){
		this.formDefKey = formDefKey;
	}

	public String getActInstanceId(){
		return this.actInstanceId;
	}
	
	public void setActInstanceId(String actInstanceId){
		this.actInstanceId = actInstanceId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getActNodeId(){
		return this.actNodeId;
	}
	
	public void setActNodeId(String actNodeId){
		this.actNodeId = actNodeId;
	}

	public String getRunId(){
		return this.runId;
	}
	
	public void setRunId(String runId){
		this.runId = runId;
	}

	public String getSetType(){
		return this.setType;
	}
	
	public void setSetType(String setType){
		this.setType = setType;
	}

	public String getFormType(){
		return this.formType;
	}
	
	public void setFormType(String formType){
		this.formType = formType;
	}

	public String getFormUrl(){
		return this.formUrl;
	}
	
	public void setFormUrl(String formUrl){
		this.formUrl = formUrl;
	}
}
