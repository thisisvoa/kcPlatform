package com.kcp.platform.bpm.entity;

import java.util.Date;

public class BpmCatalog{

	/**
	 * 流程分类ID
	 */
	private String id;

	/**
	 * 节点key
	 */
	private String catalogKey;

	/**
	 * 节点名称
	 */
	private String catalogName;
	
	/**
	 * 节点类型(1:流程定义 2:表单分类)
	 */
	private String catalogType;

	/**
	 * 父节点ID
	 */
	private String parentId;

	/**
	 * 排序号
	 */
	private Integer orderNo;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建用户
	 */
	private String createUser;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 修改用户
	 */
	private String modifyUser;
	
	/**
	 * 层级代码
	 */
	private String layerCode;

	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getCatalogKey() {
		return catalogKey;
	}

	public void setCatalogKey(String catalogKey) {
		this.catalogKey = catalogKey;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}
	
	public String getParentId(){
		return this.parentId;
	}
	
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public Integer getOrderNo(){
		return this.orderNo;
	}
	
	public void setOrderNo(Integer orderNo){
		this.orderNo = orderNo;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getModifyTime(){
		return this.modifyTime;
	}
	
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	public String getModifyUser(){
		return this.modifyUser;
	}
	
	public void setModifyUser(String modifyUser){
		this.modifyUser = modifyUser;
	}
	
	public String getLayerCode() {
		return layerCode;
	}

	public void setLayerCode(String layerCode) {
		this.layerCode = layerCode;
	}
}
