package com.kcp.platform.bpm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.kcp.platform.bpm.util.BpmUtil;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.util.DateUtils;

public class TaskOpinion{
	/**
	 * 尚未审批
	 */
	public static final Short STATUS_INIT = Short.valueOf((short)-2);
	 
	/**
	 * 正在审批
	 */
	public static final Short STATUS_CHECKING = Short.valueOf((short)-1);
	 /**
	  * 放弃
	  */
	public static final Short STATUS_ABANDON = Short.valueOf((short)0);
	
	/**
	 * 同意
	 */
	public static final Short STATUS_AGREE = Short.valueOf((short)1);
	
	/**
	 * 反对
	 */
	public static final Short STATUS_REFUSE = Short.valueOf((short)2);
	
	/**
	 * 驳回
	 */
	public static final Short STATUS_REJECT = Short.valueOf((short)3);
	/**
	 * 撤销
	 */
	public static final Short STATUS_RECOVER = Short.valueOf((short)4);
	 /**
	  * 会签通过
	  */
	public static final Short STATUS_PASSED = Short.valueOf((short)5);
	 
	/**
	 * 会签不通过
	 */
	public static final Short STATUS_NOT_PASSED = Short.valueOf((short)6);
	 /**
	  * 知会意见
	  */
	public static final Short STATUS_NOTIFY = Short.valueOf((short)7);
	 /**
	  * 更改执行路径
	  */
	public static final Short STATUS_CHANGEPATH = Short.valueOf((short)8);
	 /**
	  * 终止
	  */
	public static final Short STATUS_ENDPROCESS = Short.valueOf((short)14);
	 
	/**
	 * 沟通
	 */
	public static final Short STATUS_COMMUNICATION = Short.valueOf((short)15);
	/**
	 * 沟通反馈
	 */
	public static final Short STATUS_COMMUN_FEEDBACK = Short.valueOf((short)20);
	
	/**
	 * 办结转发
	 */
	public static final Short STATUS_FINISHDIVERT = Short.valueOf((short)16);
	 
	/**
	 * 转办
	 */
	public static final Short STATUS_DELEGATE = Short.valueOf((short)21);
	
	/**
	 * 取消转办
	 */
	public static final Short STATUS_DELEGATE_CANCEL = Short.valueOf((short)22);
	
	/**
	 * 更改执行人
	 */
	public static final Short STATUS_CHANGE_ASIGNEE = Short.valueOf((short)23);
	
	/**
	 * 驳回到发起人
	 */
	public static final Short STATUS_REJECT_TOSTART = Short.valueOf((short)24);
	 
	/**
	 * 撤销(撤销到发起人)
	 */
	public static final Short STATUS_RECOVER_TOSTART = Short.valueOf((short)25);
	 
	public static final Short STATUS_REVOKED = Short.valueOf((short)17);
	 
	public static final Short STATUS_DELETE = Short.valueOf((short)18);
	 
	public static final Short STATUS_NOTIFY_COPY = Short.valueOf((short)19);
	 
	/**
	 * 代理
	 */
	public static final Short STATUS_AGENT = Short.valueOf((short)26);
	
	/**
	 * 取消代理
	 */
	public static final Short STATUS_AGENT_CANCEL = Short.valueOf((short)27);
	
	/**
	 * 保存表单
	 */
	public static final Short STATUS_OPINION = Short.valueOf((short)28);
	
	/**
	 * 驳回取消
	 */
	public static final Short STATUS_BACK_CANCEL = Short.valueOf((short)29);
	
	/**
	 * 撤销取消
	 */
	public static final Short STATUS_REVOKED_CANCEL = Short.valueOf((short)30);
	
	/**
	 * 通过取消
	 */
	public static final Short STATUS_PASS_CANCEL = Short.valueOf((short)31);
	
	/**
	 * 反对取消
	 */
	public static final Short STATUS_REFUSE_CANCEL = Short.valueOf((short)32);
	
	/**
	 * 提交
	 */
	public static final Short STATUS_SUBMIT = Short.valueOf((short)33);
	
	/**
	 * 重新提交
	 */
	public static final Short STATUS_RESUBMIT = Short.valueOf((short)34);
	
	/**
	 * 干预
	 */
	public static final Short STATUS_INTERVENE = Short.valueOf((short)35);
	
	/**
	 * 重启任务
	 */
	public static final Short STATUS_RESTART_TASK = Short.valueOf((short)36);
	
	public static final Short STATUS_EXECUTED = Short.valueOf((short)37);
   
	/**
	 * 流转
	 */
	public static final Short STATUS_TRANSTO = Short.valueOf((short)38);
	/**
	 * 正在流转
	 */
	public static final Short STATUS_TRANSTO_ING = Short.valueOf((short)39);
	
	/**
	 * 代提交
	 */
	public static final Short STATUS_REPLACE_SUBMIT = Short.valueOf((short)40);
 
	public static final Short STATUS_COMMON_TRANSTO = Short.valueOf((short)41);
	
	public static final String TASKKEY_NOTIFY = "NotifyTask";
	
	public static final String TASKKEY_DIVERT = "DivertTask";
	
	public static final Short READ = Short.valueOf((short)1);
 
	public static final Short NOT_READ = Short.valueOf((short)0);
	
	
	/**
	 * ID
	 */
	private String opinionId;

	/**
	 * ACT流程定义ID
	 */
	private String actDefId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务KEY
	 */
	private String taskKey;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 任务TOKEN
	 */
	private String taskToken;

	/**
	 * 流程实例ID
	 */
	private String actInstId;

	/**
	 * 执行开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 持续时间
	 */
	private Long durTime;

	/**
	 * 执行人ID
	 */
	private String exeUserId;

	/**
	 * 执行人名
	 */
	private String exeUserName;

	/**
	 * 审批意见
	 */
	private String opinion;

	/**
	 * 审批状态1=同意 2=反对3=驳回 0=弃权4=追回
	 */
	private Short checkStatus;

	/**
	 * 表单定义ID
	 */
	private String formDefId;

	/**
	 * 
	 */
	private String fieldName;
	
	/**
	 * 
	 */
	private String superExecution;
	
	/**
	 * 获选执行用户
	 */
	public List<TaskExeStatus> candidateUserStatusList = new ArrayList<TaskExeStatus>();
	 
	/**
	 * 执行状态
	 */
	public TaskExeStatus taskExeStatus;
	 
	public TaskOpinion(){
		
	}

	public TaskOpinion(ProcessTask task){
		this.actDefId = task.getProcessDefinitionId();
		this.actInstId = task.getProcessInstanceId();
		this.taskId = task.getId();
		this.taskName = task.getName();
		this.taskKey = task.getTaskDefinitionKey();
		this.startTime = new Date();
	}
	
	public TaskOpinion(DelegateTask task) {
		this.actDefId = task.getProcessDefinitionId();
		this.actInstId = task.getProcessInstanceId();
		this.taskId = task.getId();
		this.taskKey = task.getTaskDefinitionKey();
		this.taskName = task.getName();
		this.checkStatus = STATUS_CHECKING;
		this.startTime = new Date();
		ExecutionEntity superExecution = ((ExecutionEntity) task.getExecution()).getProcessInstance().getSuperExecution();
		if (superExecution != null)
			this.superExecution = superExecution.getProcessInstanceId();
	}
	
	public String getOpinionId(){
		return this.opinionId;
	}
	
	public void setOpinionId(String opinionId){
		this.opinionId = opinionId;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getTaskName(){
		return this.taskName;
	}
	
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskKey(){
		return this.taskKey;
	}
	
	public void setTaskKey(String taskKey){
		this.taskKey = taskKey;
	}

	public String getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}

	public String getTaskToken(){
		return this.taskToken;
	}
	
	public void setTaskToken(String taskToken){
		this.taskToken = taskToken;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public Date getStartTime(){
		return this.startTime;
	}
	
	public String getStartTimeStr(){
		if(startTime!=null){
			return DateUtils.parseDate2String(startTime, "yyyy-MM-dd HH:mm");
		}else{
			return "";
		}
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}

	public Date getEndTime(){
		return this.endTime;
	}
	
	public String getEndTimeStr(){
		if(endTime!=null){
			return DateUtils.parseDate2String(endTime, "yyyy-MM-dd HH:mm");
		}else{
			return "";
		}
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	
	public Long getDurTime(){
		return this.durTime;
	}
	
	public String getDurTimeStr(){
		return DateUtils.getTime(durTime);
	}
	
	public void setDurTime(Long durTime){
		this.durTime = durTime;
	}

	public String getExeUserId(){
		return this.exeUserId;
	}
	
	public void setExeUserId(String exeUserId){
		this.exeUserId = exeUserId;
	}

	public String getExeUserName(){
		return this.exeUserName;
	}
	
	public void setExeUserName(String exeUserName){
		this.exeUserName = exeUserName;
	}

	public String getOpinion(){
		return this.opinion;
	}
	
	public void setOpinion(String opinion){
		this.opinion = opinion;
	}

	public Short getCheckStatus(){
		return this.checkStatus;
	}
	
	public String getStatus(){
		return BpmUtil.getTaskStatus(checkStatus, 1);
	}
	
	public void setCheckStatus(Short checkStatus){
		this.checkStatus = checkStatus;
	}

	public String getFormDefId(){
		return this.formDefId;
	}
	
	public void setFormDefId(String formDefId){
		this.formDefId = formDefId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getSuperExecution() {
		return superExecution;
	}

	public void setSuperExecution(String superExecution) {
		this.superExecution = superExecution;
	}
	
	public List<TaskExeStatus> getCandidateUserStatusList() {
		return candidateUserStatusList;
	}

	public void setCandidateUserStatusList(List<TaskExeStatus> candidateUserStatusList) {
		this.candidateUserStatusList = candidateUserStatusList;
	}
	
	public TaskExeStatus getTaskExeStatus() {
		return taskExeStatus;
	}

	public void setTaskExeStatus(TaskExeStatus taskExeStatus) {
		this.taskExeStatus = taskExeStatus;
	}
}
