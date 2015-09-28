package com.kcp.platform.bpm.entity;


public class BpmFormTemplate{

	/**
	 * ID
	 */
	private String templateId;

	/**
	 * 模板名称
	 */
	private String templateName;

	/**
	 * 模板类型
	 */
	private String templateType;

	/**
	 * 使用宏模板别名
	 */
	private String macroTemplateAlias;

	/**
	 * 模版代码
	 */
	private String html;

	/**
	 * 描述
	 */
	private String templateDesc;

	/**
	 * 能否编辑
	 */
	private String canEdit;

	/**
	 * 别名
	 */
	private String alias;


	public String getTemplateId(){
		return this.templateId;
	}
	
	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}

	public String getTemplateName(){
		return this.templateName;
	}
	
	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}

	public String getTemplateType(){
		return this.templateType;
	}
	
	public void setTemplateType(String templateType){
		this.templateType = templateType;
	}

	public String getMacroTemplateAlias(){
		return this.macroTemplateAlias;
	}
	
	public void setMacroTemplateAlias(String macroTemplateAlias){
		this.macroTemplateAlias = macroTemplateAlias;
	}

	public String getHtml(){
		return this.html;
	}
	
	public void setHtml(String html){
		this.html = html;
	}

	public String getTemplateDesc(){
		return this.templateDesc;
	}
	
	public void setTemplateDesc(String templateDesc){
		this.templateDesc = templateDesc;
	}

	public String getCanEdit(){
		return this.canEdit;
	}
	
	public void setCanEdit(String canEdit){
		this.canEdit = canEdit;
	}

	public String getAlias(){
		return this.alias;
	}
	
	public void setAlias(String alias){
		this.alias = alias;
	}
}
