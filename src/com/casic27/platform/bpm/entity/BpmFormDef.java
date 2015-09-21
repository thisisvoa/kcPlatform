/**
 * @(#)com.casic27.platform.bpm.entity.BpmFormDef
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

import java.util.Date;

public class BpmFormDef{

	/**
	 * ID
	 */
	private String formDefId;

	/**
	 * 表单分类
	 */
	private String catalogId;

	/**
	 * 表单key
	 */
	private String formKey;

	/**
	 * 表单标题
	 */
	private String subject;

	/**
	 * 描述
	 */
	private String formDesc;
	
	/**
	 * 对应的表名称
	 */
	private String tableId;
	
	
	/**
	 * 模板html
	 */
	private String template;
	
	/**
	 * 定义html
	 */
	private String html;

	/**
	 * 版本号
	 */
	private Integer versionNo;

	/**
	 * 是否默认版本
	 */
	private String isDefault;

	/**
	 * 是否发布
	 */
	private String isPublished;

	/**
	 * 发布用户
	 */
	private String publishedBy;

	/**
	 * 发布时间
	 */
	private Date publishTime;

	/**
	 * 创建用户
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改用户
	 */
	private String modifyUser;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 业务表名称
	 */
	private String tableDesc;
	
	
	public String getFormDefId(){
		return this.formDefId;
	}
	
	public void setFormDefId(String formDefId){
		this.formDefId = formDefId;
	}

	public String getCatalogId(){
		return this.catalogId;
	}
	
	public void setCatalogId(String catalogId){
		this.catalogId = catalogId;
	}

	public String getFormKey(){
		return this.formKey;
	}
	
	public void setFormKey(String formKey){
		this.formKey = formKey;
	}

	public String getSubject(){
		return this.subject;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getFormDesc(){
		return this.formDesc;
	}
	
	public void setFormDesc(String formDesc){
		this.formDesc = formDesc;
	}

	public String getHtml(){
		return this.html;
	}
	
	public void setHtml(String html){
		this.html = html;
	}

	public Integer getVersionNo(){
		return this.versionNo;
	}
	
	public void setVersionNo(Integer versionNo){
		this.versionNo = versionNo;
	}

	public String getIsDefault(){
		return this.isDefault;
	}
	
	public void setIsDefault(String isDefault){
		this.isDefault = isDefault;
	}

	public String getIsPublished(){
		return this.isPublished;
	}
	
	public void setIsPublished(String isPublished){
		this.isPublished = isPublished;
	}

	public String getPublishedBy(){
		return this.publishedBy;
	}
	
	public void setPublishedBy(String publishedBy){
		this.publishedBy = publishedBy;
	}

	public Date getPublishTime(){
		return this.publishTime;
	}
	
	public void setPublishTime(Date publishTime){
		this.publishTime = publishTime;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getModifyUser(){
		return this.modifyUser;
	}
	
	public void setModifyUser(String modifyUser){
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime(){
		return this.modifyTime;
	}
	
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
}
