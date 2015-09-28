package com.kcp.platform.bpm.entity;

import java.util.Date;

public class ProcessRun implements Cloneable{
	/**
	 * 中断
	 */
	public static final Short STATUS_SUSPEND = Short.valueOf((short)0);

	/**
	 * 正在运行
	 */
	public static final Short STATUS_RUNNING = Short.valueOf((short)1);

	/**
	 * 结束
	 */
	public static final Short STATUS_FINISH = Short.valueOf((short)2);
	
	/**
	 * 人工结束
	 */
	public static final Short STATUS_MANUAL_FINISH = Short.valueOf((short)3);
	 
	/**
	 * 草稿
	 */
	public static final Short STATUS_FORM = Short.valueOf((short)4);
	
	/**
	 * 撤销
	 */
	public static final Short STATUS_RECOVER = Short.valueOf((short)5);
 
	/**
	 * 驳回
	 */
	public static final Short STATUS_REJECT = Short.valueOf((short)6);
 
	/**
	 * 删除
	 */
	public static final Short STATUS_DELETE = Short.valueOf((short)10);
	
	/**
	 * ID
	 */
	private String runId;

	/**
	 * 流程定义ID
	 */
	private String defId;

	/**
	 * 流程名
	 */
	private String processName;

	/**
	 * 标题
	 */
	private String subject;

	/**
	 * 创建人ID
	 */
	private String creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 业务表单简述
	 */
	private String busDescp;

	/**
	 * 状态:1=正在运行 2=结束 3=人工结束 4=草稿 5=撤销 6=驳回
	 */
	private Short status;

	/**
	 * ACT流程实例ID
	 */
	private String actInstId;

	/**
	 * ACT流程定义ID
	 */
	private String actDefId;

	/**
	 * 
	 */
	private String businessKey;

	/**
	 * 
	 */
	private String businessUrl;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 持续时间
	 */
	private Long duration;

	/**
	 * 主键名
	 */
	private String pkName;

	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 父流程实例ID
	 */
	private String parentId;

	/**
	 * 启动用户所在单位
	 */
	private String startOrgId;
	
	/**
	 * 启动用户所在单位名称
	 */
	private String startOrgName;
	
	/**
	 * 流程定义ID
	 */
	private String formDefId;
	
	/**
	 * 流程分类
	 */
	private String typeId;
	
	/**
	 * KEY
	 */
	private String flowKey;
	
	
	private String formKeyUrl;
	
	/**
	 * 最后提交耗时
	 */
	private Long lastSubmitDuration;
	
	/**
	 * 是否正式
	 */
	private String isFormal;

	public String getRunId(){
		return this.runId;
	}
	
	public void setRunId(String runId){
		this.runId = runId;
	}

	public String getDefId(){
		return this.defId;
	}
	
	public void setDefId(String defId){
		this.defId = defId;
	}

	public String getProcessName(){
		return this.processName;
	}
	
	public void setProcessName(String processName){
		this.processName = processName;
	}

	public String getSubject(){
		return this.subject;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getCreatorId(){
		return this.creatorId;
	}
	
	public void setCreatorId(String creatorId){
		this.creatorId = creatorId;
	}

	public String getCreator(){
		return this.creator;
	}
	
	public void setCreator(String creator){
		this.creator = creator;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getBusDescp(){
		return this.busDescp;
	}
	
	public void setBusDescp(String busDescp){
		this.busDescp = busDescp;
	}

	public Short getStatus(){
		return this.status;
	}
	
	public void setStatus(Short status){
		this.status = status;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getBusinessKey(){
		return this.businessKey;
	}
	
	public void setBusinessKey(String businessKey){
		this.businessKey = businessKey;
	}

	public String getBusinessUrl(){
		return this.businessUrl;
	}
	
	public void setBusinessUrl(String businessUrl){
		this.businessUrl = businessUrl;
	}

	public Date getEndTime(){
		return this.endTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}

	public Long getDuration(){
		return this.duration;
	}
	
	public void setDuration(Long duration){
		this.duration = duration;
	}

	public String getPkName(){
		return this.pkName;
	}
	
	public void setPkName(String pkName){
		this.pkName = pkName;
	}

	public String getTableName(){
		return this.tableName;
	}
	
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getStartOrgId() {
		return startOrgId;
	}

	public void setStartOrgId(String startOrgId) {
		this.startOrgId = startOrgId;
	}

	public String getStartOrgName() {
		return startOrgName;
	}

	public void setStartOrgName(String startOrgName) {
		this.startOrgName = startOrgName;
	}

	public String getFormDefId() {
		return formDefId;
	}

	public void setFormDefId(String formDefId) {
		this.formDefId = formDefId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getFormKeyUrl() {
		return formKeyUrl;
	}

	public void setFormKeyUrl(String formKeyUrl) {
		this.formKeyUrl = formKeyUrl;
	}

	public Long getLastSubmitDuration() {
		return lastSubmitDuration;
	}

	public void setLastSubmitDuration(Long lastSubmitDuration) {
		this.lastSubmitDuration = lastSubmitDuration;
	}

	public String getIsFormal() {
		return isFormal;
	}

	public void setIsFormal(String isFormal) {
		this.isFormal = isFormal;
	}
	
	public Object clone() {
		ProcessRun obj = null;
		try {
			obj = (ProcessRun) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
