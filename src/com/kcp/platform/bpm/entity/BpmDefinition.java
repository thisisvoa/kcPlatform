package com.kcp.platform.bpm.entity;

import java.util.Date;

public class BpmDefinition implements Cloneable{
	public static final String DefaultSubjectRule = "{流程标题:title}-{发起人:startUser}-{发起时间:startDate}";
	
	/**
	 * 未发布
	 */
	public static final Integer STATUS_UNDEPLOYED = Integer.valueOf((short)0);
   
	/**
	 * 启用
	 */
	public static final Integer STATUS_DEPLOYED = Integer.valueOf((short)1);
 
	/**
	 * ID
	 */
	private String defId;

	/**
	 * 流程分类
	 */
	private String typeId;

	/**
	 * 流程标题
	 */
	private String subject;

	/**
	 * 流程Key
	 */
	private String defKey;

	/**
	 * 流程实例标题
	 */
	private String instNameRule;

	/**
	 * 流程描述
	 */
	private String description;

	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 设计器导出的流程xml
	 */
	private String defXml;
	
	/**
	 * 发布后的activiti ID
	 */
	private String actDefId;
	
	/**
	 * 发布后的activiti key
	 */
	private String actDefKey;
	
	/**
	 * activiti部署包ID
	 */
	private String actDeployId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 修改用户
	 */
	private String modifyUser;

	/**
	 * 跳转到第一个节点(0,不跳转 1,跳转)
	 */
	private String toFirstNode;
	
	/**
	 * 隶属主定义ID(当基于旧版本流程上进行新版本发布时，则生成一份新的记录，并且该记录的该字段则隶属于原来记录的主键id)
	 */
	private String parentDefId;
	
	/**
	 * 是否为主版本(1=主版本 0=非主版本)
	 */
	private String isMain;
	/**
	 * 版本号
	 */
	private Integer version;
	
	/**
	 * 流程类型名称
	 */
	private String catalogName;
	
	/**
	 * 启用/禁用
	 */
	private String usable;
	
	/**
	 * 直接启动流程
	 */
	private String directStart;
	
	/**
	 * 相邻任务节点人员相同时自动跳过
	 */
	private String sameExecutorJump;

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDefKey() {
		return defKey;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}

	public String getInstNameRule() {
		return instNameRule;
	}

	public void setInstNameRule(String instNameRule) {
		this.instNameRule = instNameRule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDefXml() {
		return defXml;
	}

	public void setDefXml(String defXml) {
		this.defXml = defXml;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getActDefKey() {
		return actDefKey;
	}

	public void setActDefKey(String actDefKey) {
		this.actDefKey = actDefKey;
	}

	public String getActDeployId() {
		return actDeployId;
	}

	public void setActDeployId(String actDeployId) {
		this.actDeployId = actDeployId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getToFirstNode() {
		return toFirstNode;
	}

	public void setToFirstNode(String toFirstNode) {
		this.toFirstNode = toFirstNode;
	}

	public String getParentDefId() {
		return parentDefId;
	}

	public void setParentDefId(String parentDefId) {
		this.parentDefId = parentDefId;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getUsable() {
		return usable;
	}

	public void setUsable(String usable) {
		this.usable = usable;
	}
	
	public String getDirectStart() {
		return directStart;
	}

	public void setDirectStart(String directStart) {
		this.directStart = directStart;
	}
	
	public String getSameExecutorJump() {
		return sameExecutorJump;
	}

	public void setSameExecutorJump(String sameExecutorJump) {
		this.sameExecutorJump = sameExecutorJump;
	}

	public Object clone() {
		BpmDefinition obj = null;
		try {
			obj = (BpmDefinition) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
