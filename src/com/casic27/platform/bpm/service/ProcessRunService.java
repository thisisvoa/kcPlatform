/**
 * @(#)com.casic27.platform.bpm.service.ProcessRunService
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
package com.casic27.platform.bpm.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activity.ActivityRequiredException;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.dao.IBpmDefinitionMapper;
import com.casic27.platform.bpm.dao.IBpmNodeConfigMapper;
import com.casic27.platform.bpm.dao.IProcessExecutionMapper;
import com.casic27.platform.bpm.dao.IProcessRunMapper;
import com.casic27.platform.bpm.dao.IProcessTaskMapper;
import com.casic27.platform.bpm.dao.ITaskUserMapper;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmNodeConfig;
import com.casic27.platform.bpm.entity.BpmRunLog;
import com.casic27.platform.bpm.entity.BpmTaskExe;
import com.casic27.platform.bpm.entity.ExecutionStack;
import com.casic27.platform.bpm.entity.FlowNode;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.ProcessCmd;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.entity.TaskFork;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.entity.TaskRead;
import com.casic27.platform.bpm.entity.TaskSignData;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.bpm.service.thread.TaskUserAssignService;
import com.casic27.platform.bpm.util.BpmUtil;
import com.casic27.platform.bpm.util.ExceptionUtil;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;

@Service("processRunService")
public class ProcessRunService extends BaseService {
	
	private Logger logger = LoggerFactory.getLogger(ProcessRunService.class);
	
	@Autowired
	private IProcessRunMapper processRunMapper;
	
	@Autowired
	private IBpmDefinitionMapper bpmDefinitionMapper;
	
	@Autowired
	private IBpmNodeConfigMapper bpmNodeConfigMapper;
	
	@Autowired
	private IProcessTaskMapper processTaskMapper;
	
	@Autowired
	private IProcessExecutionMapper processExecutionMapper;
	
	@Autowired
	private ITaskUserMapper taskUserMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private BpmFormRunService bpmFormRunService;
	
	@Autowired
	private BpmRunLogService bpmRunLogService;
	
	@Autowired
	private TaskUserAssignService taskUserAssignService;
	
	@Autowired
	private ExecutionStackService executionStackService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private TaskSignDataService taskSignDataService;
	
	@Autowired
	private TaskUserService taskUserService;
	
	@Autowired
	private BpmProStatusService bpmProStatusService;
	
	@Autowired
	private TaskReadService taskReadService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private BpmTaskExeService bpmTaskExeService;
	
	/**
	 * 启动流程
	 * @param processCmd
	 */
	public ProcessRun startProcess(ProcessCmd processCmd) throws Exception{
		BpmDefinition bpmDefinition = getBpmDefinitionProcessCmd(processCmd);
		if(bpmDefinition==null){
			throw new BusinessException("流程定义不存在!");
		}
		if(BpmDefinition.STATUS_UNDEPLOYED.toString().equals(bpmDefinition.getStatus()) || CommonConst.NO.equals(bpmDefinition.getUsable())){
			throw new BusinessException("该流程已经被禁用!");
		}
		String defId = bpmDefinition.getDefId();
		String toFirstNode = bpmDefinition.getToFirstNode();
		String actDefId = bpmDefinition.getActDefId();
		String nodeId = getFirstNodetByDefId(actDefId);
		BpmNodeConfig bpmNodeConfig = getStartBpmNodeConfig(defId, nodeId, toFirstNode);//获取首节点配置
		User user = SecurityContext.getCurrentUser();
		ProcessRun processRun = processCmd.getProcessRun();
		try {
			
			TaskThreadService.setToFirstNode(toFirstNode);
			if (CommonConst.YES.equals(toFirstNode)) {
				List<TaskExecutor> excutorList = new ArrayList<TaskExecutor>();
				excutorList.add(TaskExecutor.getTaskUser(user.getZjid(), user.getYhmc()));
				this.taskUserAssignService.addNodeUser(nodeId, excutorList);//ThreadLocal中存储节点执行人信息
			}
			setTaskUser(processCmd);
			
			String businessKey = "";
			 
			if (BeanUtils.isEmpty(processRun)) {
				processRun = initProcessRun(bpmDefinition, bpmNodeConfig);
			}else {
			   processRun.setCreateTime(new Date());
			   businessKey = processRun.getBusinessKey();
			}
			
			
			if (!processCmd.isInvokeExternal()) {
				businessKey = handerFormData(processRun, processCmd);
			}
			
			//执行前置处理器
			if (!processCmd.isSkipPreHandler()) {
				invokeHandler(processCmd, bpmNodeConfig, true);
			}
			
			if (StringUtils.isEmpty(businessKey)) {
				businessKey = processCmd.getBusinessKey();
			}
			
			String subject = getSubject(bpmDefinition, processCmd);
			Org curOrg = SecurityContext.getCurrentUserOrg();
			if (curOrg != null) {
				processCmd.addVariable("startOrgId", curOrg.getZjid());
			}
			processCmd.addVariable("flowRunId", processRun.getRunId());
			processCmd.addVariable("subject_", subject);
			   
			ProcessInstance processInstance = startWorkFlow(processCmd);
			String processInstanceId = processInstance.getProcessInstanceId();
			processRun.setBusinessKey(businessKey);
			processRun.setActInstId(processInstanceId);
			processRun.setSubject(subject);
			if (curOrg != null) {
				processRun.setStartOrgId(curOrg.getZjid());
				processRun.setStartOrgName(curOrg.getDwmc());
			}
			processRun.setStatus(ProcessRun.STATUS_RUNNING);
			if (BeanUtils.isEmpty(processCmd.getProcessRun()))
				addProcessRun(processRun);
			else {
				updateProcessRun(processRun);
			}
			
			List<Task> taskList = TaskThreadService.getNewTasks();
			
			this.executionStackService.initStack(processInstanceId, taskList);
			
			processCmd.setProcessRun(processRun);

			//执行后置处理器
			if (!processCmd.isSkipAfterHandler()) {
				invokeHandler(processCmd, bpmNodeConfig, false);
			}

			if (CommonConst.YES.equals(toFirstNode)) {
				handJumpOverFirstNode(processInstanceId, processCmd);
			}
			
			String memo = "启动流程:" + subject;
			this.bpmRunLogService.addBpmRunLog(processRun, BpmRunLog.OPERATOR_TYPE_START, memo);
			if (!processCmd.isInvokeExternal()) {
				this.bpmFormRunService.addBpmFormRun(actDefId, processRun.getRunId(), processInstanceId);
			}
			
			if (!CommonConst.YES.equals(toFirstNode)) {
				addSubmitOpinion(processRun);
			}
			
			handleAgentTaskExe(processCmd);//处理任务代理
		} catch (Exception e) {
			logger.error("启动流程"+bpmDefinition.getSubject()+"失败:", e);
			throw new BusinessException(ExceptionUtil.getExceptionMsg(e),e);
		}finally{
			clearThreadLocal();
		}
		return processRun;
	}
	
	public void addProcessRun(ProcessRun processRun){
		processRunMapper.addProcessRun(processRun);
	}
	
	public void updateProcessRun(ProcessRun processRun){
		if ((ProcessRun.STATUS_MANUAL_FINISH == processRun.getStatus())
				|| (ProcessRun.STATUS_FINISH == processRun.getStatus())) {
			Date endDate = new Date();
			Date startDate = processRun.getCreateTime();
			long duration = endDate.getTime()-startDate.getTime();
			processRun.setEndTime(endDate);
			processRun.setDuration(Long.valueOf(duration));
		}
		processRunMapper.updateProcessRun(processRun);
	}
	/**
	 * 完成任务
	 * @param processCmd
	 * @return
	 * @throws Exception
	 */
	public ProcessRun nextProcess(ProcessCmd processCmd) throws Exception {
		ProcessRun processRun = null;
		String taskId = "";
		TaskEntity taskEntity = getTaskEntByCmd(processCmd);
		if (taskEntity == null) return null;
		taskId = taskEntity.getId();
		if ((taskEntity.getExecutionId() == null) && (TaskOpinion.STATUS_COMMUNICATION.toString().equals(taskEntity.getDescription()))) {
			return null;
		}

		Object nextPathObj = processCmd.getFormDataMap().get("nextPathId");
		if (nextPathObj != null) {
			this.bpmService.setTaskVariable(taskId, "NextPathId", nextPathObj.toString());
		}
		String parentNodeId = taskEntity.getTaskDefinitionKey();
		String actDefId = taskEntity.getProcessDefinitionId();
		String instanceId = taskEntity.getProcessInstanceId();
		String executionId = taskEntity.getExecutionId();
		BpmDefinition bpmDefinition = this.bpmDefinitionMapper.getByActDefId(actDefId);
		processRun = this.processRunMapper.getProcessRunByActInstId(instanceId);
		processCmd.setProcessRun(processRun);
		try {
			String taskToken = (String) this.taskService.getVariableLocal(taskId, TaskFork.TAKEN_VAR_NAME);
			BpmNodeConfig bpmNodeConfig = this.bpmNodeConfigMapper.getByActDefIdNodeId(actDefId, parentNodeId);
			setTaskUser(processCmd);

			if (!processCmd.isInvokeExternal()) {
				//TODO 处理在线表单
			}
			if (!processCmd.isSkipPreHandler()) {
				invokeHandler(processCmd, bpmNodeConfig, true);
			}
			
			ExecutionStack parentStack = backPrepare(processCmd, taskEntity, taskToken);
			if (parentStack != null) {
				parentNodeId = parentStack.getNodeId();
			}

			signUsersOrSignVoted(processCmd, taskEntity);//处理会签节点

			processCmd.setSubject(processRun.getSubject());
			setVariables(taskId, processCmd);//设置变量
			addInterVene(processCmd, taskEntity);//添加干预
			completeTask(processCmd, taskEntity);//完成任务

			if (!processCmd.isSkipAfterHandler()) {
				invokeHandler(processCmd, bpmNodeConfig, false);
			}

			if ((StringUtils.isEmpty(processRun.getBusinessKey())) && (StringUtils.isNotEmpty(processCmd.getBusinessKey()))) {
				processRun.setBusinessKey(processCmd.getBusinessKey());
				this.runtimeService.setVariable(executionId, "businessKey", processCmd.getBusinessKey());//设置businessKey为流程变量
			}

			if ((processCmd.isBack().intValue() > 0) && (parentStack != null)) {
				//如果是回退，则把堆栈信息回退到要回退的节点
				this.executionStackService.pop(parentStack, processCmd.isRecover(), processCmd.isBack());
			} else {
				//把当前任务加入到堆栈中
				List<String> map = TaskThreadService.getExtSubProcess();
				if (BeanUtils.isEmpty(map)) {
					this.executionStackService.addStack(instanceId, parentNodeId, taskToken);
				} else {
					initExtSubProcessStack();//初始化外部调用流程堆栈
				}
			}
			if ((processCmd.isBack().intValue() > 0) || (StringUtils.isNotEmpty(processCmd.getDestTask()))) {
				this.processTaskMapper.updateOldTaskDefKeyByInstIdNodeId(taskEntity.getTaskDefinitionKey() + "_1", taskEntity.getTaskDefinitionKey(), taskEntity.getProcessInstanceId());
			}
			handleAgentTaskExe(processCmd);//处理任务代理
			recordLog(processCmd, taskEntity.getName(), processRun.getRunId());//记录日志
			this.bpmTaskExeService.complete(taskId);//完成流程代理
			if (TaskThreadService.getObject() == null) {
				updateStatus(processCmd, processRun);//修改状态
			}
			List<Task> taskList = TaskThreadService.getNewTasks();
			handSameExecutorJump(taskList, bpmDefinition, processCmd);//执行若设置相邻节点同一个执行人跳过执行
		} catch (Exception ex) {
			throw new BusinessException("任务执行失败:"+ExceptionUtil.getExceptionMsg(ex),ex);
		} finally {
			clearThreadLocal();
		}
		return processRun;
	}
	/**
	 * 批量审批
	 * @param taskIds
	 * @param opinion
	 * @throws Exception
	 */
	public void nextProcessBat(String taskIds, String opinion) throws Exception {
		String[] aryTaskId = taskIds.split(",");
		for (String taskId : aryTaskId) {
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			ProcessRun processRun = this.processRunMapper.getProcessRunByActInstId(taskEntity.getProcessInstanceId());
			String subject = processRun.getSubject();
			if ((taskEntity.getExecutionId() == null)
					&& (TaskOpinion.STATUS_COMMUNICATION.toString().equals(taskEntity.getDescription()))) {
				throw new BusinessException("<span class='red'>" + subject + ",为沟通任务!</span><br/>");
			} else if ((taskEntity.getExecutionId() == null)
					&& (TaskOpinion.STATUS_TRANSTO.toString().equals(taskEntity.getDescription()))) {
				throw new BusinessException("<span class='red'>" + subject + ",为流转任务!</span><br/>");
			} else {
				ProcessCmd processCmd = new ProcessCmd();
				processCmd.setVoteAgree(TaskOpinion.STATUS_AGREE);
				processCmd.setVoteContent(opinion);
				processCmd.setTaskId(taskId);
				nextProcess(processCmd);
			}
		}
	}
	/**
	 * 保存为草稿
	 * @param processCmd
	 * @throws Exception
	 */
	public void saveForm(ProcessCmd processCmd) throws Exception {
		String actDefId = processCmd.getActDefId();
		BpmDefinition bpmDefinition = this.bpmDefinitionMapper.getByActDefId(actDefId);

		String nodeId = getFirstNodetByDefId(actDefId);
		BpmNodeConfig bpmNodeSet = getStartBpmNodeConfig(bpmDefinition.getDefId(), nodeId, bpmDefinition.getToFirstNode());
		ProcessRun processRun = initProcessRun(bpmDefinition, bpmNodeSet);
		String businessKey = "";
		if (!processCmd.isInvokeExternal()) {
			//TODO 处理在线表单
		}
		if (!processCmd.isSkipPreHandler()) {
			invokeHandler(processCmd, bpmNodeSet, true);
		}
		if (StringUtils.isEmpty(businessKey)) {
			businessKey = processCmd.getBusinessKey();
		}
		String subject = getSubject(bpmDefinition, processCmd);
		processRun.setBusinessKey(businessKey);
		processRun.setSubject(subject);
		processRun.setStatus(ProcessRun.STATUS_FORM);
		processRun.setCreateTime(new Date());
		addProcessRun(processRun);
		if (!processCmd.isSkipAfterHandler()) {
			invokeHandler(processCmd, bpmNodeSet, false);
		}
		String memo = "保存草稿:" + subject;
		this.bpmRunLogService.addBpmRunLog(processRun, BpmRunLog.OPERATOR_TYPE_SAVEFORM, memo);
	}
	
	/**
	 * 保存表单
	 * @param processCmd
	 * @throws Exception
	 */
	public void saveData(ProcessCmd processCmd) throws Exception {
		BpmDefinition bpmDefinition = this.bpmDefinitionMapper.getByActDefId(processCmd.getActDefId());
		String taskId = processCmd.getTaskId();
		ProcessRun processRun = null;
		BpmNodeConfig bpmNodeConfig = null;
		if (StringUtils.isEmpty(taskId)) {
			processRun = processCmd.getProcessRun();
			FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
			bpmNodeConfig = getStartBpmNodeConfig(processRun.getDefId(), flowNode.getNodeId(), bpmDefinition.getToFirstNode());
		} else {
			TaskEntity task = this.bpmService.getTask(taskId);
			String actInstId = task.getProcessInstanceId();
			String actDefId = task.getProcessDefinitionId();
			String nodeId = task.getTaskDefinitionKey();
			processRun = getProcessRunByActInstId(actInstId);
			String opinion = processCmd.getVoteContent();
			saveOpinion(task, opinion);
			bpmNodeConfig = this.bpmNodeConfigMapper.getByActDefIdNodeId(actDefId, nodeId);
		}
		if (!processCmd.isInvokeExternal()) {
			//TODO 处理在线表单
		}

		if (!processCmd.isSkipPreHandler()){
			invokeHandler(processCmd, bpmNodeConfig, true);
		}
	}
	/**
	 * 保存审批意见
	 * @param taskEntity
	 * @param opinion
	 */
	public void saveOpinion(TaskEntity taskEntity, String opinion) {
		User curUser = SecurityContext.getCurrentUser();
		TaskOpinion taskOpinion = this.taskOpinionService.getOpinionByTaskId(taskEntity.getId(), curUser.getZjid());
		if (taskOpinion == null) {
			String taskId = taskEntity.getId();
			String opinionId = CodeGenerator.getUUID32();
			taskOpinion = new TaskOpinion();
			taskOpinion.setOpinionId(opinionId);
			taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
			taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
			taskOpinion.setTaskId(taskId);
			taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
			taskOpinion.setTaskName(taskEntity.getName());
			taskOpinion.setStartTime(new Date());
			taskOpinion.setCheckStatus(TaskOpinion.STATUS_OPINION);
			taskOpinion.setOpinion(opinion);
			taskOpinion.setExeUserId(curUser.getZjid());
			taskOpinion.setExeUserName(curUser.getYhmc());
			this.taskOpinionService.addTaskOpinion(taskOpinion);
		} else {
			taskOpinion.setExeUserId(curUser.getZjid());
			taskOpinion.setExeUserName(curUser.getYhmc());
			taskOpinion.setOpinion(opinion);
			this.taskOpinionService.updateTaskOpinion(taskOpinion);
		}
	}
	/**
	 * 删除流程实例
	 * @param ids
	 */
	public void delByIds(String[] ids) {
		if ((ids == null) || (ids.length == 0))
			return;
		for (String uId : ids) {
			ProcessRun processRun = processRunMapper.getProcessRunById(uId);
			Short procStatus = processRun.getStatus();
			String instanceId = processRun.getActInstId();
			if ((ProcessRun.STATUS_FINISH != procStatus) && (ProcessRun.STATUS_FORM != procStatus)) {
				deleteProcessInstance(processRun);
			} else {
				String memo = "用户删除了流程实例[" + processRun.getProcessName() + "]。";
				if (ProcessRun.STATUS_FORM == procStatus) {
					memo = "用户删除了流程草稿【" + processRun.getProcessName() + "】。";
					this.bpmRunLogService.addBpmRunLog(processRun, BpmRunLog.OPERATOR_TYPE_DELETEFORM, memo);
				} else {
					this.bpmRunLogService.addBpmRunLog(processRun, BpmRunLog.OPERATOR_TYPE_DELETEINSTANCE, memo);
				}
				if(ProcessRun.STATUS_FORM != procStatus){
					taskOpinionService.delByActInstId(instanceId);//删除任务审批
					bpmProStatusService.delByActInstId(instanceId);//删除任务状态
					taskSignDataService.delByIdActInstId(instanceId);//删除会签数据
					bpmTaskExeService.delByRunId(uId);//删除流程代理
				}
				delById(uId);
			}
		}
	}
	/**
	 * 撤销
	 * @param runId
	 * @param memo
	 * @throws Exception
	 */
	public void recover(String runId, String memo) throws Exception {
		ProcessRun processRun = getProcessRunById(runId);
		List<ProcessTask> taskList = this.bpmService.getTasks(processRun.getActInstId());
		validCanRecover(processRun, taskList);
		boolean hasRead = getTaskHasRead(taskList);
		if ((hasRead) && ("".equals(memo))) {
			throw new BusinessException("对不起,请填写撤销的原因!");
		}
		backToStart(memo, taskList);//回退到起始节点
	}
	/**
	 * 验证是否可以撤销
	 * @param runId
	 */
	public void validCanRecover(String runId){
		ProcessRun processRun = getProcessRunById(runId);
		List<ProcessTask> taskList = this.bpmService.getTasks(processRun.getActInstId());
		validCanRecover(processRun, taskList);
	}
	
	/**
	 * 验证是否可以撤销
	 * @param processRun
	 * @param taskList
	 */
	private void validCanRecover(ProcessRun processRun, List<ProcessTask> taskList){
		Short status = processRun.getStatus();
		if (status.shortValue() == ProcessRun.STATUS_FINISH.shortValue()) {
			throw new BusinessException("对不起,此流程实例已经结束!");
		}
		if (status.shortValue() == ProcessRun.STATUS_MANUAL_FINISH.shortValue()) {
			throw new BusinessException("对不起,此流程实例已经被删除!");
		}
		String curUserId = SecurityContext.getCurrentUserId();
		boolean isCreator = processRun.getCreatorId().equals(curUserId);
		if (!isCreator) {
			throw new BusinessException("对不起,非流程发起人不能撤销到开始节点!");
		}

		FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
		String nodeId = flowNode.getNodeId();

		
		List<String> taskNodeIdList = getNodeIdByTaskList(taskList);
		if (taskNodeIdList.contains(nodeId)) {
			throw new BusinessException("当前已经是开始节点!");
		}

		boolean allowBack = getTaskAllowBack(taskList);
		if (!allowBack) {
			throw new BusinessException("对不起,当前流程实例不允许撤销!");
		}
	}
	
	/**
	 * 召回操作
	 * @param runId
	 * @param informType
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	public void redo(String runId,  String memo) throws Exception {
		ProcessRun processRun = getProcessRunById(runId);
		String instanceId = processRun.getActInstId();
		List<ProcessTask> tasks = this.bpmService.getTasks(instanceId);
		
		ProcessTask taskEntity = (ProcessTask) tasks.get(0);
		ProcessCmd processCmd = new ProcessCmd();
		processCmd.setTaskId(taskEntity.getId());
		processCmd.setRecover(true);
		processCmd.setBack(BpmConstants.TASK_BACK);
		processCmd.setVoteAgree(TaskOpinion.STATUS_RECOVER);
		processCmd.setVoteContent(memo);
		nextProcess(processCmd);
	}
	/**
	 * 获取初始表单设置
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeConfig getStartBpmNodeConfig(String defId,  String nodeId, String toFirstNode) {
		BpmNodeConfig firstBpmNodeConfig = this.bpmNodeConfigMapper.getByDefIdNodeId(defId, nodeId);
		BpmNodeConfig bpmNodeSetGlobal = this.bpmNodeConfigMapper.getBySetType(defId, BpmNodeConfig.TYPE_GLOBEL);
		if ((firstBpmNodeConfig != null) && StringUtils.isNotEmpty(firstBpmNodeConfig.getFormType()) && CommonConst.YES.equals(toFirstNode)) {
		       return firstBpmNodeConfig;
		}
		return bpmNodeSetGlobal;
	}
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<ProcessRun> findProcessRun(Map<String,Object> queryMap){
		return processRunMapper.findProcessRun(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<ProcessRun> findProcessRunPaging(Map<String,Object> queryMap){
		return processRunMapper.findProcessRunPaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public ProcessRun getProcessRunById(String id){
		return processRunMapper.getProcessRunById(id);
	}
	
	/**
	 * 根据流程实例ID查询
	 * @param actInstId
	 * @return
	 */
	public ProcessRun getProcessRunByActInstId(String actInstId){
		return processRunMapper.getProcessRunByActInstId(actInstId);
	}
	
	
	public void delTransToTaskByParentTaskId(String parentTaskId) {
	     this.processTaskMapper.delTransToTaskByParentTaskId(parentTaskId);
	}
	
	/**
	 * 获取第一个节点ID
	 * @param actDefId
	 * @return
	 */
	public String getFirstNodetByDefId(String actDefId) {
		FlowNode flowNode = NodeCache.getFirstNodeId(actDefId);
		if(flowNode!=null){
			return flowNode.getNodeId();
		}
		return "";
	}
	
	/**
	 * 删除流程实例和流程实例历史
	 * @param id
	 */
	public void delById(String id){
		this.processRunMapper.delById(id);
	}
	/**
	 * 删除流程实例
	 * @param id
	 */
	public void delProRunById(String id){
		this.processRunMapper.delById(id);
	}
	
	/**
	 * 获取流程处理时长
	 * 
	 * @param entity
	 * @return
	 */
	public Long getProcessDuration(ProcessRun entity) {
		Long duration = Long.valueOf(0L);
		String actInstId = entity.getActInstId();
		List<TaskOpinion> taskOpinions = this.taskOpinionService.getByActInstId(actInstId);
		Map<String, List<TaskOpinion>> signTask = new HashMap<String, List<TaskOpinion>>();
		TaskSignData signData = null;
		for (TaskOpinion opn : taskOpinions) {
			boolean isSignTask = this.bpmService.isSignTask(entity.getActDefId(), opn.getTaskKey());
			if (!isSignTask) {
				if (BeanUtils.isNotEmpty(opn.getDurTime())) {
					duration = Long.valueOf(duration.longValue() + opn.getDurTime().longValue());
				}
			}else{
				/**
				 * 如果是会签任务，则先把审批意见按节点进行整理，以便统计会签节点的处理时长
				 */
				List<TaskOpinion> taskList = signTask.get(opn.getTaskKey());
				if (taskList == null) {
					taskList = new ArrayList<TaskOpinion>();
					signTask.put(opn.getTaskKey(), taskList);
				}

				if (opn.getTaskId() != null) {
					signData = this.taskSignDataService.getByTaskId(opn.getTaskId());
					if (signData != null) {
						taskList.add(opn);
					}
				}
			}
		}
		/**
		 * 计算整理后的会签任务处理时长
		 */
		Iterator<Map.Entry<String, List<TaskOpinion>>> it = signTask.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, List<TaskOpinion>> entry = it.next();
			List<TaskOpinion> opList = entry.getValue();
			Long maxDuration = Long.valueOf(0L);
			for (TaskOpinion o : opList) {
				long durtime = o.getDurTime() == null ? 0L : o.getDurTime().longValue();
				if (maxDuration.longValue() < durtime) {
					maxDuration = Long.valueOf(durtime);
				}
			}
			duration = Long.valueOf(duration.longValue() + maxDuration.longValue());
		}
		return duration;
	}
	
	/**
	 * 获取流程最后提交耗时
	 * @param entity
	 * @return
	 */
	public Long getProcessLastSubmitDuration(ProcessRun processRun) {
		Long duration = Long.valueOf(0L);
		String actInstId = processRun.getActInstId();
		
		List<TaskOpinion> taskOpinions = this.taskOpinionService.getByActInstId(actInstId);
		List<TaskOpinion> lastTaskOpinions = new ArrayList<TaskOpinion>();
		Collections.sort(taskOpinions, new Comparator<TaskOpinion>(){
			public int compare(TaskOpinion o1, TaskOpinion o2) {
				Date startTime1 = o1.getStartTime();
				Date endTime1 = o1.getEndTime();
				Date startTime2 = o2.getStartTime();
				Date endTime2 = o2.getEndTime();
	 
				if ((endTime1 != null) && (endTime2 != null)){
					return endTime1.compareTo(endTime2);
				}
					
				if ((endTime1 != null) && (endTime2 == null)){
					return 1;
				}
					
				if ((endTime1 == null) && (endTime2 != null)) {
					return -1;
				}
				return startTime1.compareTo(startTime2);
			}
	     });
		
	     boolean hasResubmit = false;
	     for (int i = taskOpinions.size() - 1; i >= 0; i--) {
	    	 if (TaskOpinion.STATUS_RESUBMIT.equals(((TaskOpinion)taskOpinions.get(i)).getCheckStatus())) {
	    		 hasResubmit = true;
	    		 break;
	    	 }
	    	 lastTaskOpinions.add((TaskOpinion)taskOpinions.get(i));
	     }
	 
	     if (!hasResubmit) {
	    	 if (processRun.getDuration() != null) {
	    		 return processRun.getDuration();
	    	 }
	    	 return getProcessDuration(processRun);
	     }
	     
	     for (TaskOpinion opn : lastTaskOpinions) {
	    	 boolean isSignTask = this.bpmService.isSignTask(processRun.getActDefId(), opn.getTaskKey());
	    	 if (!isSignTask) {
	    		 if (opn.getDurTime() != null){
	    			 duration = Long.valueOf(duration.longValue() + opn.getDurTime().longValue());
	    		 }
	    	 }
	     }
	     return duration;
	}
	/**
	 * 获取流程实例任务列表
	 * @param runId
	 * @return
	 */
	public List<ProcessTask> getTasksByRunId(String runId) {
		return this.processTaskMapper.getTasksByRunId(runId);
	}
	
	/**
	 * 获取任务
	 * @param taskId
	 * @return
	 */
	public ProcessTask getTaskByTaskId(String taskId){
		return this.processTaskMapper.getByTaskId(taskId);
	}
	/**
	 * 获取经办事项
	 * @param queryMap
	 * @return
	 */
	public List<ProcessRun> getAlreadyMattersList(Map<String,Object> queryMap){
		return this.processRunMapper.getAlreadyMattersList(queryMap);
	}
	
	/**
	 * 获取已结事项
	 * @param queryMap
	 * @return
	 */
	public List<ProcessRun> getCompleteMattersList(Map<String,Object> queryMap){
		return this.processRunMapper.getCompleteMattersList(queryMap);
	}
	
	/**
	 * 我的请求
	 * @param queryMap
	 * @return
	 */
	public List<ProcessRun> getMyRequestList(Map<String,Object> queryMap) {
		return this.processRunMapper.getMyRequestList(queryMap);
	}
	
	/**
	 * 我的办结
	 * @param queryMap
	 * @return
	 */
	public List<ProcessRun> getMyCompleteList(Map<String,Object> queryMap) {
		return this.processRunMapper.getMyCompleteList(queryMap);
	}
	
	/**
	 * 我的草稿
	 * @param queryMap
	 * @return
	 */
	public List<ProcessRun> getMyDraftList(Map<String,Object> queryMap) {
		return this.processRunMapper.getMyDraftList(queryMap);
	}
	
	/**
	 * 获取流程定义
	 * @param processCmd
	 * @return
	 */
	private BpmDefinition getBpmDefinitionProcessCmd(ProcessCmd processCmd) {
		BpmDefinition bpmDefinition = null;
		if (processCmd.getActDefId() != null){
			bpmDefinition = bpmDefinitionMapper.getByActDefId(processCmd.getActDefId());
		}else{
			bpmDefinition = bpmDefinitionMapper.getByActDefKeyIsMain(processCmd.getFlowKey(), "1");
		}
		return bpmDefinition;
	}
	
	/**
	 * 页面上如果传递了下个节点的任务处理人员，则保存任务处理人员到taskUserAssignService中
	 * @param processCmd
	 */
	private void setTaskUser(ProcessCmd processCmd){
		if (processCmd.isBack().intValue() == 0) {
			String[] nodeIds = processCmd.getLastDestTaskIds();
			String[] nodeUserIds = processCmd.getLastDestTaskUids();
	 
			if ((nodeIds != null) && (nodeUserIds != null)) {
	         this.taskUserAssignService.addNodeUser(nodeIds, nodeUserIds);
			}
	     }
	 
	     if (processCmd.getTaskExecutors().size() > 0) {
	       this.taskUserAssignService.setExecutors(processCmd.getTaskExecutors());
	     }
	     TaskThreadService.setProcessCmd(processCmd);
	}
	
	private ProcessRun initProcessRun(BpmDefinition bpmDefinition, BpmNodeConfig bpmNodeConfig) {
		ProcessRun processRun = new ProcessRun();
		if(bpmNodeConfig!=null){
			if(BpmNodeConfig.NODE_FORM_TYPE_ONLINE.equals(bpmNodeConfig.getFormType())){
				//TODO
			}else{
				processRun.setBusinessUrl(bpmNodeConfig.getFormUrl());
			}
		}
		User curUser = SecurityContext.getCurrentUser();
		processRun.setCreator(curUser.getYhmc());
		processRun.setCreatorId(curUser.getZjid());
		processRun.setActDefId(bpmDefinition.getActDefId());
		processRun.setDefId(bpmDefinition.getDefId());
		processRun.setProcessName(bpmDefinition.getSubject());
		processRun.setFlowKey(bpmDefinition.getDefKey());
		processRun.setCreateTime(new Date());
		processRun.setStatus(ProcessRun.STATUS_RUNNING);
		processRun.setIsFormal(CommonConst.YES);
		processRun.setTypeId(bpmDefinition.getTypeId());
		processRun.setRunId(CodeGenerator.getUUID32());
		return processRun;
	}
	/**
	 * 执行处理器(前置|后置)
	 * @param processCmd
	 * @param bpmNodeConfig
	 * @param isBefore
	 * @throws Exception
	 */
	private void invokeHandler(ProcessCmd processCmd, BpmNodeConfig bpmNodeConfig, boolean isBefore) throws Exception {
		if (bpmNodeConfig == null)
			return;
		String handler = "";
		if (isBefore)
			handler = bpmNodeConfig.getBeforeHandler();
		else {
			handler = bpmNodeConfig.getAfterHandler();
		}
		if (StringUtils.isEmpty(handler)) {
			return;
		}
		String[] aryHandler = handler.split("[.]");
		if (aryHandler != null) {
			String beanId = aryHandler[0];
			String method = aryHandler[1];
			Object serviceBean = SpringContextHolder.getBean(beanId);
			if (serviceBean != null) {
				Method invokeMethod = serviceBean.getClass().getDeclaredMethod(
						method, new Class[] { ProcessCmd.class });
				invokeMethod.invoke(serviceBean, new Object[] { processCmd });
			}
		}
	}
	
	/**
	 * 处理保存在线表单数据
	 * @param processRun
	 * @param processCmd
	 * @return
	 * @throws Exception
	 */
	private String handerFormData(ProcessRun processRun, ProcessCmd processCmd)throws Exception {
		String json = processCmd.getFormData();
		if (StringUtils.isNotEmpty(json)) {
			//TODO 处理表单保存
		}
		return "";
	}
	
	/**
	 * 启动流程
	 * @param processCmd
	 * @param businessKey
	 * @param userId
	 * @return
	 */
	private ProcessInstance startWorkFlow(ProcessCmd processCmd) {
		String businessKey = processCmd.getBusinessKey();
		String userId = SecurityContext.getCurrentUserId();
		ProcessInstance processInstance = null;
		if (StringUtils.isNotEmpty(businessKey)) {
			processCmd.getVariables().put("businessKey", businessKey);
		}else{
	       businessKey = CodeGenerator.getUUID32();
		}
		
		Authentication.setAuthenticatedUserId(userId);
		if (processCmd.getActDefId() != null)
			processInstance = this.bpmService.startFlowById(processCmd.getActDefId(), businessKey, processCmd.getVariables());
		else {
			processInstance = this.bpmService.startFlowByKey(processCmd.getFlowKey(), businessKey, processCmd.getVariables());
		}
		Authentication.setAuthenticatedUserId(null);
		return processInstance;
	}
	/**
	 * 解析流程标题
	 * @param bpmDefinition
	 * @param processCmd
	 * @return
	 */
	private String getSubject(BpmDefinition bpmDefinition, ProcessCmd processCmd) {
		if (StringUtils.isNotEmpty(processCmd.getSubject())) {
			return processCmd.getSubject();
		}
		String rule = bpmDefinition.getInstNameRule();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", bpmDefinition.getSubject());
		User user = SecurityContext.getCurrentUser();
		map.put("startUser", user.getYhmc());
		map.put("startDate", DateUtils.getCurrentDate10());
		map.put("startTime", DateUtils.getCurrentTime());
		map.put("businessKey", processCmd.getBusinessKey());
		map.putAll(processCmd.getVariables());
		rule = BpmUtil.parseTextByRule(rule, map);
		return rule;
	}
	
	/**
	 * 跳过第一个处理节点
	 * @param processInstanceId
	 * @param processCmd
	 * @throws Exception
	 */
	private void handJumpOverFirstNode(String processInstanceId, ProcessCmd processCmd) throws Exception {
		TaskThreadService.clearNewTasks();
		List<ProcessTask> taskList = this.bpmService.getTasks(processInstanceId);
		ProcessTask taskEntity = (ProcessTask) taskList.get(0);
		String taskId = taskEntity.getId();
		String parentNodeId = taskEntity.getTaskDefinitionKey();
		processCmd.getVariables().put("approvalStatus_" + parentNodeId, TaskOpinion.STATUS_SUBMIT);
		processCmd.getVariables().put("approvalContent_" + parentNodeId, "填写表单");
		processCmd.setVoteAgree(TaskOpinion.STATUS_SUBMIT);
		setVariables(taskId, processCmd);

		this.bpmService.transTo(taskId, "");//跳转到下个任务节点
		this.executionStackService.addStack(taskEntity.getProcessInstanceId(), parentNodeId, "");
	}
	
	/**
	 * 设置流程变量
	 * @param taskId
	 * @param processCmd
	 */
	private void setVariables(String taskId, ProcessCmd processCmd) {
		if (StringUtils.isEmpty(taskId))
			return;
		Map<String,Object> vars = processCmd.getVariables();
		if (BeanUtils.isNotEmpty(vars)) {
			for (Iterator<Map.Entry<String,Object>> it = vars.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String,Object> obj = (Map.Entry<String,Object>) it.next();
				this.taskService.setVariable(taskId, (String) obj.getKey(), obj.getValue());
			}
		}
		Map<String,Object> formVars = this.taskService.getVariables(taskId);
		formVars.put("isExtCall", Boolean.valueOf(processCmd.isInvokeExternal()));
		formVars.put("subject_", processCmd.getSubject());
		TaskThreadService.setVariables(formVars);
		TaskThreadService.getVariables().putAll(processCmd.getVariables());
	}
	/**
	 * 添加提交意见
	 * @param processRun
	 */
	private void addSubmitOpinion(ProcessRun processRun) {
		TaskOpinion opinion = new TaskOpinion();
		String startUserId = processRun.getCreatorId();
		User startUser = (User)this.userService.getUserById(startUserId);
		opinion.setOpinionId(CodeGenerator.getUUID32());
		opinion.setCheckStatus(TaskOpinion.STATUS_SUBMIT);
		opinion.setActInstId(processRun.getActInstId());
		opinion.setActDefId(processRun.getActDefId());
		opinion.setExeUserName(startUser.getYhmc());
		opinion.setExeUserId(startUserId);
		opinion.setOpinion("提交");
		opinion.setStartTime(processRun.getCreateTime());
		opinion.setTaskName("提交流程");
		opinion.setEndTime(new Date());
		opinion.setDurTime(Long.valueOf(0L));
		this.taskOpinionService.addTaskOpinion(opinion);
	}
	
	/**
	 * 清除ThreadLocal中存储的参数
	 */
	private void clearThreadLocal(){
	     TaskThreadService.clearAll();
	     TaskUserAssignService.clearAll();
	}
	
	/**
	 * 获取任务
	 * @param processCmd
	 * @return
	 */
	private TaskEntity getTaskEntByCmd(ProcessCmd processCmd) {
		TaskEntity taskEntity = null;
		String taskId = processCmd.getTaskId();
		String runId = processCmd.getRunId();

		if (StringUtils.isNotEmpty(taskId)) {
			taskEntity = this.bpmService.getTask(taskId);
		}
		if ((taskEntity == null) && StringUtils.isNotEmpty(runId)) {
			ProcessRun processRun = (ProcessRun) getProcessRunById(runId);
			if (processRun == null) return null;
			String instanceId = processRun.getActInstId();
			ProcessTask processTask = this.bpmService.getFirstNodeTask(instanceId);
			if (processTask == null) return null;
			taskEntity = this.bpmService.getTask(processTask.getId());
		}
		return taskEntity;
	}
	
	/**
	 * 回退准备
	 * @param processCmd
	 * @param taskEntity
	 * @param taskToken
	 * @return
	 * @throws Exception
	 */
	private ExecutionStack backPrepare(ProcessCmd processCmd, TaskEntity taskEntity, String taskToken) throws Exception {
		List<TaskOpinion> taskOpinions = null;
		String instanceId = taskEntity.getProcessInstanceId();
		if (StringUtils.isNotEmpty(processCmd.getDestTask())) {
			taskOpinions = this.taskOpinionService.getByActInstIdTaskKey(instanceId, processCmd.getDestTask());
		}
		if ((processCmd.isBack().intValue() == 0) && (BeanUtils.isEmpty(taskOpinions))) return null;
		String aceDefId = taskEntity.getProcessDefinitionId();

		String backToNodeId = NodeCache.getFirstNodeId(aceDefId).getNodeId();
		ExecutionStack parentStack = null;

		if ((processCmd.isBack().equals(BpmConstants.TASK_BACK)) || (BeanUtils.isNotEmpty(taskOpinions))) {
			parentStack = this.executionStackService.backPrepared(processCmd, taskEntity, taskToken);
		} else if (processCmd.isBack() == BpmConstants.TASK_BACK_TOSTART) {
			parentStack = this.executionStackService.getLastestStack( instanceId, backToNodeId, null);
			if (parentStack != null) {
				processCmd.setDestTask(parentStack.getNodeId());
				this.taskUserAssignService.addNodeUser(parentStack.getNodeId(), parentStack.getAssignees());
			}
		}
		return parentStack;
	}
	
	/**
	 * 处理会签
	 * @param processCmd
	 * @param taskEntity
	 */
	private void signUsersOrSignVoted(ProcessCmd processCmd, TaskEntity taskEntity) {
		String nodeId = taskEntity.getTaskDefinitionKey();
		String taskId = taskEntity.getId();

		boolean isSignTask = this.bpmService.isSignTask(taskEntity);

		if (isSignTask) {
			Map<String, List<TaskExecutor>> executorMap = processCmd.getTaskExecutor();
			if ((executorMap != null) && (executorMap.containsKey(nodeId))) {
				List<TaskExecutor> executorList =  executorMap.get(nodeId);
				this.taskUserAssignService.setExecutors(executorList);
			}
		}

		if (processCmd.getVoteAgree() != null) {
			if (isSignTask) {
				this.taskSignDataService.signVoteTask(taskId, processCmd.getVoteContent(), processCmd.getVoteAgree());
			}
			processCmd.getVariables().put("approvalStatus_" + nodeId, processCmd.getVoteAgree());
		}
	}
	
	/**
	 * 添加干预
	 * @param processCmd
	 * @param taskEnt
	 */
	private void addInterVene(ProcessCmd processCmd, DelegateTask taskEnt) {
		if (processCmd.getIsManage().shortValue() == 0)
			return;
		String assignee = taskEnt.getAssignee();
		String tmp = "";

		User curUser = SecurityContext.getCurrentUser();
		String userId = "";
		String userName = "系统";
		if (curUser != null) {
			userId = curUser.getZjid();
			userName = curUser.getYhmc();
		}
		if (StringUtils.isNotEmpty(assignee)) {
			User sysUser = this.userService.getUserById(assignee);
			if (sysUser != null){
				tmp = "原执行人:"+sysUser.getYhmc();
			}
		} else {
			tmp = "原候选执行人:";
			Set<User> userList = this.taskUserService.getCandidateUsers(taskEnt.getId());
			String spliter  = "";
			for (User user : userList) {
				if (user != null) {
					tmp = tmp + spliter+ user.getYhmc();
					spliter = ",";
				}
			}
		}
		Date endDate = new Date();
		TaskOpinion taskOpinion = new TaskOpinion();
		taskOpinion.setOpinionId(CodeGenerator.getUUID32());
		taskOpinion.setActDefId(taskEnt.getProcessDefinitionId());
		taskOpinion.setActInstId(taskEnt.getProcessInstanceId());
		taskOpinion.setTaskKey(taskEnt.getTaskDefinitionKey());
		taskOpinion.setTaskName(taskEnt.getName());
		taskOpinion.setExeUserId(userId);
		taskOpinion.setExeUserName(userName);
		taskOpinion.setCheckStatus(TaskOpinion.STATUS_INTERVENE);
		taskOpinion.setStartTime(taskEnt.getCreateTime());
		taskOpinion.setTaskId(taskEnt.getId());
		taskOpinion.setEndTime(endDate);
		taskOpinion.setOpinion(tmp);
		Long duration = endDate.getTime()- taskEnt.getCreateTime().getTime();
		taskOpinion.setDurTime(duration);
		this.taskOpinionService.addTaskOpinion(taskOpinion);
	}
	/**
	 * 完成任务
	 * @param processCmd
	 * @param taskEntity
	 * @throws ActivityRequiredException
	 */
	private void completeTask(ProcessCmd processCmd, TaskEntity taskEntity) throws ActivityRequiredException {
		String taskId = taskEntity.getId();
		if (processCmd.isOnlyCompleteTask()) {
			this.bpmService.onlyCompleteTask(taskId);
		} else if (StringUtils.isNotEmpty(processCmd.getDestTask())) {
			this.bpmService.transTo(taskId, processCmd.getDestTask());
		}  else {
			this.bpmService.transTo(taskId, "");
		}
	}
	
	/**
	 * 初始化外部流程
	 */
	private void initExtSubProcessStack(){
		List<String> list = TaskThreadService.getExtSubProcess();
		if (BeanUtils.isEmpty(list)) return;
		List<Task> taskList = TaskThreadService.getNewTasks();
		Map<String, List<Task>> map = getMapByTaskList(taskList);
		for (String instanceId : list) {
			List<Task> tmpList =  map.get(instanceId);
			this.executionStackService.initStack(instanceId, tmpList);
		}
	}
	/**
	 * 将task按流程实例ID整理分组
	 * @param taskList
	 * @return
	 */
	private Map<String, List<Task>> getMapByTaskList(List<Task> taskList) {
		Map<String, List<Task>> map = new HashMap<String, List<Task>>();
		for (Task task : taskList) {
			String instanceId = task.getProcessInstanceId();
			if (map.containsKey(instanceId)) {
				((List<Task>) map.get(instanceId)).add(task);
			} else {
				List<Task> list = new ArrayList<Task>();
				list.add(task);
				map.put(instanceId, list);
			}
		}
		return map;
	}
	
	/**
	 * 记录日志
	 * @param processCmd
	 * @param taskName
	 * @param runId
	 * @throws Exception
	 */
	private void recordLog(ProcessCmd processCmd, String taskName, String runId)throws Exception {
		String memo = "";
		Integer type = Integer.valueOf(-1);

		if (processCmd.isRecover()) {
			type = BpmRunLog.OPERATOR_TYPE_RETRIEVE;
			memo = "用户在任务节点[" + taskName + "]执行了追回操作。";
		} else if (BpmConstants.TASK_BACK.equals(processCmd.isBack())) {
			type = BpmRunLog.OPERATOR_TYPE_REJECT;
			memo = "用户在任务节点[" + taskName + "]执行了驳回操作。";
		} else if (BpmConstants.TASK_BACK_TOSTART.equals(processCmd.isBack())) {
			type = BpmRunLog.OPERATOR_TYPE_REJECT2SPONSOR;
			memo = "用户在任务节点[" + taskName + "]执行了驳回到发起人操作。";
		} else {
			if (TaskOpinion.STATUS_AGREE.equals(processCmd.getVoteAgree())) {
				type = BpmRunLog.OPERATOR_TYPE_AGREE;
				memo = "用户在任务节点[" + taskName + "]执行了同意操作。";
			} else if (TaskOpinion.STATUS_REFUSE.equals(processCmd
					.getVoteAgree())) {
				type = BpmRunLog.OPERATOR_TYPE_OBJECTION;
				memo = "用户在任务节点[" + taskName + "]执行了反对操作。";
			} else if (TaskOpinion.STATUS_ABANDON.equals(processCmd
					.getVoteAgree())) {
				type = BpmRunLog.OPERATOR_TYPE_ABSTENTION;
				memo = "用户在任务节点[" + taskName + "]执行了弃权操作。";
			}

			if (TaskOpinion.STATUS_CHANGEPATH.equals(processCmd.getVoteAgree())) {
				type = BpmRunLog.OPERATOR_TYPE_CHANGEPATH;
				memo = "用户在任务节点[" + taskName + "]执行了更改执行路径操作。";
			}
			if(TaskOpinion.STATUS_PASSED.equals(processCmd.getVoteAgree())){
				type = BpmRunLog.OPERATOR_TYPE_SIGN_PASSED;
				memo = "用户在任务节点[" + taskName + "]执行了会签直接通过操作。";
			}
			if(TaskOpinion.STATUS_NOT_PASSED.equals(processCmd.getVoteAgree())){
				type = BpmRunLog.OPERATOR_TYPE_SIGN_NOT_PASSED;
				memo = "用户在任务节点[" + taskName + "]执行了会签直接不通过操作。";
			}
		}
		if (type.intValue() == -1) {
			return;
		}
		this.bpmRunLogService.addBpmRunLog(runId, type, memo);
	}
	
	/**
	 * 修改流程实例状态
	 * @param cmd
	 * @param processRun
	 */
	private void updateStatus(ProcessCmd cmd, ProcessRun processRun) {
		boolean isRecover = cmd.isRecover();
		int isBack = cmd.isBack().intValue();
		Short status = ProcessRun.STATUS_RUNNING;
		switch (isBack) {
			case 0:
				status = ProcessRun.STATUS_RUNNING;
				break;
			case 1:
				if (isRecover)
					status = ProcessRun.STATUS_RECOVER;
				else
					status = ProcessRun.STATUS_REJECT;
				break;
			case 2:
				if (isRecover)
					status = ProcessRun.STATUS_RECOVER;
				else {
					status = ProcessRun.STATUS_REJECT;
				}
				break;
		}
		processRun.setStatus(status);
		updateProcessRun(processRun);
	}
	
	/**
	 * 删除流程实例
	 * @param processRun
	 */
	private void deleteProcessInstance(ProcessRun processRun) {
		ProcessRun rootProcessRun = getRootProcessRun(processRun);
		deleteProcessRunCasade(rootProcessRun);
	}
	/**
	 * 获取跟流程实例
	 * @param processRun
	 * @return
	 */
	private ProcessRun getRootProcessRun(ProcessRun processRun) {
		ProcessInstance parentProcessInstance = (ProcessInstance) this.runtimeService.createProcessInstanceQuery().subProcessInstanceId(processRun.getActInstId()).singleResult();
		if (parentProcessInstance != null) {
			ProcessRun parentProcessRun = getProcessRunByActInstId(parentProcessInstance.getProcessInstanceId());
			return getRootProcessRun(parentProcessRun);
		}
		return processRun;
	}
	/**
	 * 删除Execution、Task等级联信息
	 * @param processRun
	 */
	private void deleteProcessRunCasade(ProcessRun processRun) {
		List<ProcessInstance> childrenProcessInstance = this.runtimeService.createProcessInstanceQuery().superProcessInstanceId(processRun.getActInstId()).list();
		for (ProcessInstance instance : childrenProcessInstance) {
			ProcessRun pr = getProcessRunByActInstId(instance.getProcessInstanceId());
			if (pr != null) {
				deleteProcessRunCasade(pr);
			}
		}
		String procInstId = processRun.getActInstId();
		Short procStatus = processRun.getStatus();
		if (ProcessRun.STATUS_FINISH != procStatus) {
			this.taskSignDataService.delByIdActInstId(procInstId);//删除会签数据
			this.executionStackService.delByActInstId(procInstId);//删除任务堆栈
			this.taskOpinionService.delByActInstId(procInstId);//删除任务审批意见
			this.bpmProStatusService.delByActInstId(procInstId);//删除任务状态
			this.taskReadService.delByActInstId(procInstId);//删除任务是否已读
			this.taskUserMapper.delByInstanceId(procInstId);//删除任务分配用户
			this.processExecutionMapper.delVariableByProcInstId(procInstId);//删除流程变量
			this.processTaskMapper.delCandidateByInstanceId(procInstId);//删除任务分配用户
			this.processTaskMapper.delByInstanceId(procInstId);//删除ACT流程任务
			this.processExecutionMapper.delExecutionByProcInstId(procInstId);//删除ACT流程实例
			this.bpmTaskExeService.delByRunId(processRun.getRunId());
			
		}
		String memo = "用户删除了流程实例[" + processRun.getProcessName() + "]。";
		this.bpmRunLogService.addBpmRunLog(processRun,BpmRunLog.OPERATOR_TYPE_DELETEINSTANCE, memo);
		delById(processRun.getRunId());
	}
	
	/**
	 * 相邻节点同一个执行人，则跳过执行
	 * 
	 * @param taskList
	 * @param bpmDefinition
	 * @param processCmd
	 * @throws Exception
	 */
	private void handSameExecutorJump(List<Task> taskList, BpmDefinition bpmDefinition, ProcessCmd processCmd)throws Exception {
		if ((processCmd.isBack().intValue() == 0) && (CommonConst.YES.equals(bpmDefinition.getSameExecutorJump()))) {
			if ((BeanUtils.isNotEmpty(taskList)) && (processCmd.getIsManage().shortValue() == 0)) {
				Task task = (Task) taskList.get(0);
				TaskThreadService.clearNewTasks();
				this.taskUserAssignService.clearExecutors();
				if (SecurityContext.getCurrentUserId().equals(task.getAssignee())) {
					processCmd.setTaskId(task.getId());
					processCmd.setVoteAgree(TaskOpinion.STATUS_AGREE);
					nextProcess(processCmd);
				}
			}
		}
	}
	/**
	 * 获取流程节点ID
	 * @param taskList
	 * @return
	 */
	private List<String> getNodeIdByTaskList(List<ProcessTask> taskList) {
		List<String> list = new ArrayList<String>();
		for (ProcessTask task : taskList) {
			list.add(task.getTaskDefinitionKey());
		}
		return list;
	}
	
	/**
	 * 任务是否已读
	 * @param list
	 * @return
	 */
	private boolean getTaskHasRead(List<ProcessTask> list) {
		boolean rtn = false;
		for (ProcessTask task : list) {
			List<TaskRead> readList = this.taskReadService.getTaskRead(task.getProcessInstanceId(),task.getId());
			if (BeanUtils.isNotEmpty(readList)) {
				rtn = true;
				break;
			}
		}
		return rtn;
	}
	/**
	 * 是否有任务允许回退
	 * @param list
	 * @return
	 */
	private boolean getTaskAllowBack(List<ProcessTask> list) {
		for (ProcessTask task : list) {
			boolean allBack = this.bpmService.getIsAllowBackByTask(task);
			if (allBack) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 回退到起始节点
	 * @param memo
	 * @param taskList
	 * @throws Exception
	 */
	private void backToStart(String memo, List<ProcessTask> taskList) throws Exception {
		List<Task> taskEntityList = new ArrayList<Task>();
		for (ProcessTask task : taskList) {
			Task taskEntity = this.bpmService.getTask(task.getId());
			taskEntityList.add(taskEntity);
		}
		for (int i = 0; i < taskList.size(); i++) {
			ProcessTask taskEntity = (ProcessTask) taskList.get(i);
			ProcessCmd processCmd = new ProcessCmd();
			processCmd.setTaskId(taskEntity.getId());
			processCmd.setRecover(true);
			processCmd.setBack(BpmConstants.TASK_BACK_TOSTART);
			processCmd.setVoteAgree(TaskOpinion.STATUS_RECOVER_TOSTART);
			processCmd.setVoteContent(memo);
			if (i > 0) {
				processCmd.setOnlyCompleteTask(true);
			}
			nextProcess(processCmd);
			this.bpmTaskExeService.cancel(taskEntity.getId());//取消代理
		}
		
	}
	/**
	 * 处理流程代理任务
	 * @param cmd
	 * @throws Exception
	 */
	private void handleAgentTaskExe(ProcessCmd cmd) throws Exception {
		List<Task> taskList = TaskThreadService.getNewTasks();
		if (BeanUtils.isEmpty(taskList))
			return;
		for (Task taskEntity : taskList)
			if (TaskOpinion.STATUS_AGENT.toString().equals(taskEntity.getDescription())) {
				String assigeeId = taskEntity.getAssignee();
				User auth = userService.getUserById(taskEntity.getOwner());
				User agent = userService.getUserById(assigeeId);
				addAgentTaskExe(taskEntity, cmd, auth, agent);
			}
	}
	
	/**
	 * 添加任务代理
	 * @param task
	 * @param cmd
	 * @param auth
	 * @param agent
	 * @throws Exception
	 */
	private void addAgentTaskExe(Task task, ProcessCmd cmd, User auth, User agent) throws Exception {
		ProcessRun processRun = cmd.getProcessRun();
		if (processRun == null) {
			processRun = getProcessRunByActInstId(task.getProcessInstanceId());
		}
		String memo = "[" + auth.getYhmc() + "]自动代理给[" + agent.getYhmc() + "]";
		String processSubject = processRun.getSubject();
		BpmTaskExe bpmTaskExe = new BpmTaskExe();
		bpmTaskExe.setId(CodeGenerator.getUUID32());
		bpmTaskExe.setTaskId(task.getId());
		bpmTaskExe.setAssigneeId(agent.getZjid());
		bpmTaskExe.setAssigneeName(agent.getYhmc());
		bpmTaskExe.setOwnerId(auth.getZjid());
		bpmTaskExe.setOwnerName(auth.getYhmc());
		bpmTaskExe.setSubject(processSubject);
		bpmTaskExe.setStatus(BpmTaskExe.STATUS_INIT);
		bpmTaskExe.setMemo(memo);
		bpmTaskExe.setCreateTime(new Date());
		bpmTaskExe.setActInstId(task.getProcessInstanceId());
		bpmTaskExe.setTaskDefKey(task.getTaskDefinitionKey());
		bpmTaskExe.setTaskName(task.getName());
		bpmTaskExe.setAssignType(BpmTaskExe.TYPE_ASSIGNEE);
		bpmTaskExe.setRunId(processRun.getRunId());
		bpmTaskExe.setTypeId(processRun.getTypeId());
		this.bpmTaskExeService.assignSave(bpmTaskExe);
	}
}