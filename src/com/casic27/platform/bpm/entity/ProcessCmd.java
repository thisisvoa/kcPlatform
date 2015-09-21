package com.casic27.platform.bpm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.casic27.platform.bpm.util.BpmUtil;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.StringUtils;


public class ProcessCmd {
	/**
	 * act流程定义Id
	 */
	private String actDefId;
	
	/**
	 * 流程定义Key
	 */
	private String flowKey;
	
	/**
	 * taskId
	 */
	private String taskId;
	
	/**
	 * 流程实例ID
	 */
	private String runId;
	
	/**
	 * 标题
	 */
	private String subject;
	/**
	 * 目标task
	 */
	private String destTask;
	/**
	 * 上个节点ID
	 */
	private String[] lastDestTaskIds;
	
	/**
	 * 上个节点执行用户ID
	 */
	private String[] lastDestTaskUids;
	
	/**
	 * 任务执行者
	 */
	 private List<TaskExecutor> taskExecutors = new ArrayList<TaskExecutor>();
	 
	/**
	 * 业务主键
	 */
	private String businessKey;
	
	private String stackId;
	
	/**
	 * 是否跳过前置处理器
	 */
	private boolean skipPreHandler = false;
	
	/**
	 * 是否跳过后置处理器
	 */
	private boolean skipAfterHandler = false;
	
	/**
	 * 是否回退
	 */
	private Integer isBack = Integer.valueOf(0);
	
	/**
	 * 是否撤销
	 */
	private boolean isRecover = false;
	
	/**
	 * 是否仅仅完成任务不跳转
	 */
	private boolean isOnlyCompleteTask = false;
	 
	/**
	 * 投票状态
	 */
	private Short voteAgree = Short.valueOf((short)1);
	
	/**
	 * 意见
	 */
	private String voteContent = "";
	 
	private String voteFieldName = "";
	
	/**
	 * 变量
	 */
	private Map<String, Object> variables = new HashMap<String, Object>();
	/**
	 * 表单数据，在线表单使用
	 */
	private String formData = "";
	
	/**
	 * 表单映射MAP
	 */
	private Map<String,Object> formDataMap = new HashMap<String,Object>();
	
	/**
	 * 当前用户
	 */
	private String currentUserId = "";
	
	/**
	 * 当前流程实例
	 */
	private ProcessRun processRun = null;
	
	/**
	 * 用户帐号
	 */
	private String userAccount = null;
	
	/**
	 * 外部表单
	 */
	private boolean invokeExternal = false;
	
	/**
	 * 提醒类型
	 */
	private String informType = "";
 
	private String informStart = "";
	
	/**
	 * 是否管理页面
	 */
	private Short isManage = Short.valueOf((short)0);
 
	private String dynamicTask;
	
	/**
	 * 跳转类型
	 */
	private Short jumpType;

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDestTask() {
		return destTask;
	}

	public void setDestTask(String destTask) {
		this.destTask = destTask;
	}

	public String[] getLastDestTaskIds() {
		return lastDestTaskIds;
	}

	public void setLastDestTaskIds(String[] lastDestTaskIds) {
		this.lastDestTaskIds = lastDestTaskIds;
	}

	public String[] getLastDestTaskUids() {
		return lastDestTaskUids;
	}

	public void setLastDestTaskUids(String[] lastDestTaskUids) {
		this.lastDestTaskUids = lastDestTaskUids;
	}

	public List<TaskExecutor> getTaskExecutors() {
		return taskExecutors;
	}

	public void setTaskExecutors(List<TaskExecutor> taskExecutors) {
		this.taskExecutors = taskExecutors;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getStackId() {
		return stackId;
	}

	public void setStackId(String stackId) {
		this.stackId = stackId;
	}

	public boolean isSkipPreHandler() {
		return skipPreHandler;
	}

	public void setSkipPreHandler(boolean skipPreHandler) {
		this.skipPreHandler = skipPreHandler;
	}

	public boolean isSkipAfterHandler() {
		return skipAfterHandler;
	}

	public void setSkipAfterHandler(boolean skipAfterHandler) {
		this.skipAfterHandler = skipAfterHandler;
	}

	public Integer isBack() {
		return isBack;
	}

	public void setBack(Integer isBack) {
		this.isBack = isBack;
	}

	public boolean isRecover() {
		return isRecover;
	}

	public void setRecover(boolean isRecover) {
		this.isRecover = isRecover;
	}

	public boolean isOnlyCompleteTask() {
		return isOnlyCompleteTask;
	}

	public void setOnlyCompleteTask(boolean isOnlyCompleteTask) {
		this.isOnlyCompleteTask = isOnlyCompleteTask;
	}

	public Short getVoteAgree() {
		return voteAgree;
	}

	public void setVoteAgree(Short voteAgree) {
		if (TaskOpinion.STATUS_RECOVER.equals(voteAgree)) {
			setRecover(true);
		}
		this.voteAgree = voteAgree;
	}

	public String getVoteContent() {
		return voteContent;
	}

	public void setVoteContent(String voteContent) {
		this.voteContent = voteContent;
	}

	public String getVoteFieldName() {
		return voteFieldName;
	}

	public void setVoteFieldName(String voteFieldName) {
		this.voteFieldName = voteFieldName;
	}

	public Map<String, Object> getVariables(){
		return this.variables;
	}
	 
	public void setVariables(Map<String, Object> variables){
		this.variables = variables;
	}
	 
	public void putVariables(Map<String, Object> variables){
		this.variables.putAll(variables);
	}
	 
	public void addVariable(String key, Object obj){
		this.variables.put(key, obj);
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public Map<String, Object> getFormDataMap() {
		return formDataMap;
	}

	public void setFormDataMap(Map<String, Object> formDataMap) {
		this.formDataMap = formDataMap;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public ProcessRun getProcessRun() {
		return processRun;
	}

	public void setProcessRun(ProcessRun processRun) {
		this.processRun = processRun;
		this.businessKey = processRun.getBusinessKey();
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public boolean isInvokeExternal() {
		return invokeExternal;
	}

	public void setInvokeExternal(boolean invokeExternal) {
		this.invokeExternal = invokeExternal;
	}

	public String getInformType() {
		return informType;
	}

	public void setInformType(String informType) {
		this.informType = informType;
	}

	public String getInformStart() {
		return informStart;
	}

	public void setInformStart(String informStart) {
		this.informStart = informStart;
	}

	public Short getIsManage() {
		return isManage;
	}

	public void setIsManage(Short isManage) {
		this.isManage = isManage;
	}

	public String getDynamicTask() {
		return dynamicTask;
	}

	public void setDynamicTask(String dynamicTask) {
		this.dynamicTask = dynamicTask;
	}

	public Short getJumpType() {
		return jumpType;
	}

	public void setJumpType(Short jumpType) {
		this.jumpType = jumpType;
	}
	
	public Map<String, List<TaskExecutor>> getTaskExecutor() {
		Map<String,List<TaskExecutor>> map = new HashMap<String,List<TaskExecutor>>();
		if (BeanUtils.isEmpty(this.lastDestTaskIds)) return map;
		for (int i = 0; i < this.lastDestTaskIds.length; i++) {
			String nodeId = this.lastDestTaskIds[i];
			String executor = this.lastDestTaskUids[i];
			if (!StringUtils.isEmpty(executor)) {
				List<TaskExecutor> list = BpmUtil.getTaskExecutors(executor);
				map.put(nodeId, list);
			}
		}
		return map;
	}
}
