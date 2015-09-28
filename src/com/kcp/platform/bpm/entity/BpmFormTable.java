package com.kcp.platform.bpm.entity;

import java.util.Date;

public class BpmFormTable{

	/**
	 * 
	 */
	private String tableId;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表描述
	 */
	private String tableDesc;

	/**
	 * 是否主表
	 */
	private String isMain;

	/**
	 * 主表ID
	 */
	private String mainTableId;

	/**
	 * 版本号
	 */
	private Integer versionNo;

	/**
	 * 是否默认使用
	 */
	private String isDefault;

	/**
	 * 是否发布
	 */
	private String isPublished;

	/**
	 * 发布人
	 */
	private String publishedBy;

	/**
	 * 发布时间
	 */
	private Date publishTime;

	/**
	 * 是否外部数据源
	 */
	private String isExternal;

	/**
	 * 数据源别名
	 */
	private String dsAlias;

	/**
	 * 数据源名称
	 */
	private String dsName;

	/**
	 * 字段关联关系
	 */
	private String relation;

	/**
	 * 键类型
	 */
	private String keyType;

	/**
	 * 键值
	 */
	private String keyValue;

	/**
	 * 主键字段
	 */
	private String pkField;

	/**
	 * 列表模板
	 */
	private String listTemplate;

	/**
	 * 明细模板
	 */
	private String detailTemplate;


	public String getTableId(){
		return this.tableId;
	}
	
	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getTableName(){
		return this.tableName;
	}
	
	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getTableDesc(){
		return this.tableDesc;
	}
	
	public void setTableDesc(String tableDesc){
		this.tableDesc = tableDesc;
	}

	public String getIsMain(){
		return this.isMain;
	}
	
	public void setIsMain(String isMain){
		this.isMain = isMain;
	}

	public String getMainTableId(){
		return this.mainTableId;
	}
	
	public void setMainTableId(String mainTableId){
		this.mainTableId = mainTableId;
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

	public String getIsExternal(){
		return this.isExternal;
	}
	
	public void setIsExternal(String isExternal){
		this.isExternal = isExternal;
	}

	public String getDsAlias(){
		return this.dsAlias;
	}
	
	public void setDsAlias(String dsAlias){
		this.dsAlias = dsAlias;
	}

	public String getDsName(){
		return this.dsName;
	}
	
	public void setDsName(String dsName){
		this.dsName = dsName;
	}

	public String getRelation(){
		return this.relation;
	}
	
	public void setRelation(String relation){
		this.relation = relation;
	}

	public String getKeyType(){
		return this.keyType;
	}
	
	public void setKeyType(String keyType){
		this.keyType = keyType;
	}

	public String getKeyValue(){
		return this.keyValue;
	}
	
	public void setKeyValue(String keyValue){
		this.keyValue = keyValue;
	}

	public String getPkField(){
		return this.pkField;
	}
	
	public void setPkField(String pkField){
		this.pkField = pkField;
	}

	public String getListTemplate(){
		return this.listTemplate;
	}
	
	public void setListTemplate(String listTemplate){
		this.listTemplate = listTemplate;
	}

	public String getDetailTemplate(){
		return this.detailTemplate;
	}
	
	public void setDetailTemplate(String detailTemplate){
		this.detailTemplate = detailTemplate;
	}
}
