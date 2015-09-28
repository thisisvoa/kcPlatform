package com.kcp.platform.bpm.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.entity.FlowNode;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.entity.ProcessCmd;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.bpm.entity.TaskFork;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.service.BpmAgentSettingService;
import com.kcp.platform.bpm.service.BpmNodeConfigService;
import com.kcp.platform.bpm.service.BpmNodeParticipantService;
import com.kcp.platform.bpm.service.BpmProStatusService;
import com.kcp.platform.bpm.service.BpmService;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.bpm.service.TaskForkService;
import com.kcp.platform.bpm.service.TaskOpinionService;
import com.kcp.platform.bpm.service.thread.TaskThreadService;
import com.kcp.platform.bpm.service.thread.TaskUserAssignService;
import com.kcp.platform.bpm.util.BpmUtil;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.BeanUtils;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.util.StringUtils;
/**
 * 任务创建监听器，在activiti任务创建时候执行
 * @author Administrator
 *
 */
public class TaskCreateListener extends BaseTaskListener {
	
	private static final long serialVersionUID = -1284630488148202088L;
	
	private TaskOpinionService taskOpinionService = SpringContextHolder.getBean(TaskOpinionService.class);
	
	private TaskUserAssignService taskUserAssignService = SpringContextHolder.getBean(TaskUserAssignService.class);
	 
	private BpmNodeConfigService bpmNodeConfigService = SpringContextHolder.getBean(BpmNodeConfigService.class);
	 
	private BpmProStatusService bpmProStatusService = SpringContextHolder.getBean(BpmProStatusService.class);
	
	private TaskForkService taskForkService = SpringContextHolder.getBean(TaskForkService.class);
	
	private BpmService bpmService = SpringContextHolder.getBean(BpmService.class);
	 
	private UserService userService = SpringContextHolder.getBean(UserService.class);
	 
	private ProcessRunService processRunService = SpringContextHolder.getBean(ProcessRunService.class);
	
	private BpmAgentSettingService bpmAgentSettingService = SpringContextHolder.getBean(BpmAgentSettingService.class);
	
	@Override
	protected void execute(DelegateTask delegateTask, String actDefId, String nodeId) {
		delegateTask.setDescription(TaskOpinion.STATUS_CHECKING.toString());
		TaskThreadService.addTask((TaskEntity)delegateTask);//加入到线程变量内
		String token = TaskThreadService.getToken();//任务分发令牌
		if (token != null) {
			delegateTask.setVariableLocal(TaskFork.TAKEN_VAR_NAME, token);
		}
		addOpinion(token, delegateTask);//添加审批意见
		String actInstanceId = delegateTask.getProcessInstanceId();
		this.bpmProStatusService.addOrUpd(actDefId, actInstanceId, nodeId);//设置流程节点状态
		
		Map<String, List<TaskExecutor>> nodeUserMap = this.taskUserAssignService.getNodeUserMap();
		boolean isHandForkTask = handlerForkTask(actDefId, nodeId, nodeUserMap, delegateTask);//处理任务分发
		if (isHandForkTask) return;
	 
		/********设置子流程用户*********/
		boolean isSubProcess = handSubProcessUser(delegateTask);
		if (isSubProcess) return;
		
		/********设置外部流程用户*********/
		boolean isHandExtUser = handExtSubProcessUser(delegateTask);
		if (isHandExtUser) return;
		
		/********设置任务执行用户*********/
		if ((nodeUserMap != null) && (nodeUserMap.get(nodeId) != null)) {//外部传入了要执行的用户
	       List<TaskExecutor> executorIds = nodeUserMap.get(nodeId);
	       assignUser(delegateTask, executorIds);
	       return;
	     }
		 //如果界面上传递了下个节点的执行用户，则分配给界面传递的数据
	     List<TaskExecutor> executorUsers = this.taskUserAssignService.getExecutors();
	     if (BeanUtils.isNotEmpty(executorUsers)) {
	    	 assignUser(delegateTask, executorUsers);
	    	 return;
	     }
	     handAssignUserFromDb(actDefId, nodeId, delegateTask);
	}
	
	/**
	 * 添加流程审批
	 * @param token
	 * @param delegateTask
	 */
	private void addOpinion(String token, DelegateTask delegateTask){
	     TaskOpinion taskOpinion = new TaskOpinion(delegateTask);
	     taskOpinion.setOpinionId(CodeGenerator.getUUID32());
	     taskOpinion.setTaskToken(token);
	     this.taskOpinionService.addTaskOpinion(taskOpinion);
	}
	/**
	 * 处理分发任务
	 * @param actDefId
	 * @param nodeId
	 * @param nodeUserMap
	 * @param delegateTask
	 * @return
	 */
	private boolean handlerForkTask(String actDefId, String nodeId, Map<String, List<TaskExecutor>> nodeUserMap, DelegateTask delegateTask) {
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((processCmd != null) && (BpmConstants.TASK_BACK.equals(processCmd.isBack()))) return false;
		BpmNodeConfig bpmNodeConfig = this.bpmNodeConfigService.getByActDefIdNodeId(actDefId, nodeId);
		if ((bpmNodeConfig != null) && (BpmNodeConfig.NODE_TYPE_FORK.equals(bpmNodeConfig.getNodeType()))) {
			List<TaskExecutor> taskExecutors = this.taskUserAssignService.getExecutors();

			if (BeanUtils.isEmpty(taskExecutors)) {
				if ((nodeUserMap != null) && (nodeUserMap.get(nodeId) != null)) {
					taskExecutors =  nodeUserMap.get(nodeId);
				} else {
					BpmNodeParticipantService participantService = SpringContextHolder.getBean(BpmNodeParticipantService.class);
					ProcessInstance processInstance = this.bpmService.getProcessInstance(delegateTask.getProcessInstanceId());
					if (processInstance != null) {
						Map<String,Object> vars = delegateTask.getVariables();
						vars.put("executionId", delegateTask.getExecutionId());
						String preTaskUser = SecurityContext.getCurrentUserId();
						String actInstId = delegateTask.getProcessInstanceId();
						String startUserId = (String) delegateTask.getVariable("startUser");
						taskExecutors = participantService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preTaskUser, vars);
					}
				}
			}
			if (BeanUtils.isNotEmpty(taskExecutors)) {
				this.bpmService.newForkTasks((TaskEntity) delegateTask, taskExecutors);
				this.taskForkService.newTaskFork(delegateTask, bpmNodeConfig.getJoinTaskName(), bpmNodeConfig.getJoinTaskKey(), Integer.valueOf(taskExecutors.size()));
			} else {
				ProcessRun processRun = this.processRunService.getProcessRunByActInstId(delegateTask.getProcessInstanceId());
				String msg = processRun.getSubject() + "请设置分发人员";
				throw new BusinessException(msg);
			}

			return true;
		}
		return false;
	}
	
	/**
	 * 处理子流程,设置任务执行用户
	 * @param delegateTask
	 * @return
	 */
	private boolean handSubProcessUser(DelegateTask delegateTask) {
		FlowNode flowNode = NodeCache.getByActDefId(delegateTask.getProcessDefinitionId()).get(delegateTask.getTaskDefinitionKey());
		boolean isMultipleNode = flowNode.getIsMultiInstance().booleanValue();
		if (!isMultipleNode)
			return false;

		TaskExecutor taskExecutor = (TaskExecutor) delegateTask.getVariable("assignee");
		if (taskExecutor != null) {
			assignUser(delegateTask, taskExecutor);
			int completeInstance = ((Integer) delegateTask.getVariable("nrOfCompletedInstances")).intValue();
			int nrOfInstances = ((Integer) delegateTask.getVariable("nrOfInstances")).intValue();
			if (completeInstance == nrOfInstances) {
				delegateTask.removeVariable("subAssignIds");
			}
		}
		return true;
	}
	/**
	 * 设置任务的执行用户
	 * @param delegateTask
	 * @param taskExecutor
	 */
	private void assignUser(DelegateTask delegateTask, TaskExecutor taskExecutor) {
		TaskOpinion taskOpinion;
		if ("user".equals(taskExecutor.getType())) {
			delegateTask.setOwner(taskExecutor.getExecuteId());
			String userId = taskExecutor.getExecuteId();
			User user = null;
			if (isAllowAgent()) {
				user = this.bpmAgentSettingService.getAgent(delegateTask, userId);
			}
			if (user != null) {
				/**如果设置了代理则设置任务执行用户为代理用户，任务拥有者为原分配用户**/
				delegateTask.setAssignee(user.getZjid().toString());
				delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
				delegateTask.setOwner(taskExecutor.getExecuteId());
			} else {
				delegateTask.setAssignee(userId);
			}
			taskOpinion = taskOpinionService.getTaskOpinionById(delegateTask.getId());
			User exeUser = userService.getUserById(userId);
			taskOpinion.setExeUserId(exeUser.getZjid());
			taskOpinion.setExeUserName(exeUser.getYhmc());
			this.taskOpinionService.updateTaskOpinion(taskOpinion);
		} else {
			delegateTask.setAssignee("0");
			delegateTask.setOwner("0");
			List<TaskExecutor> userList = getByTaskExecutor(taskExecutor);
			for (TaskExecutor ex : userList){
				if (ex.getType().equals("user")) {
					delegateTask.addCandidateUser(ex.getExecuteId());
				} else{
					delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
				}
			}
		}
	}
	
	/**
	 * 设置外部流程用户
	 * @param delegateTask
	 * @return
	 */
	private boolean handExtSubProcessUser(DelegateTask delegateTask) {
		ExecutionEntity executionEnt = (ExecutionEntity) delegateTask.getExecution();
		if (executionEnt.getSuperExecution() == null)
			return false;

		if (!BpmUtil.isMuiltiExcetion(executionEnt.getSuperExecution()))//不是多实例不处理
			return false;
		String actDefId = executionEnt.getSuperExecution().getProcessDefinitionId();
		Map<String,FlowNode> mapParent = NodeCache.getByActDefId(actDefId);
		String parentNodeId = executionEnt.getSuperExecution().getActivityId();
		String curentNodeId = executionEnt.getActivityId();
		
		FlowNode parentFlowNode = (FlowNode) mapParent.get(parentNodeId);
		Map<String,FlowNode> subNodeMap = parentFlowNode.getSubProcessNodes();
		FlowNode startNode = NodeCache.getStartNode(subNodeMap);
		if (startNode.getNextFlowNodes().size() == 1) {
			FlowNode nextNode = (FlowNode) startNode.getNextFlowNodes().get(0);
			if (nextNode.getNodeId().equals(curentNodeId)) {
				TaskExecutor taskExecutor = (TaskExecutor) executionEnt.getSuperExecution().getVariable("assignee");
				if (taskExecutor != null) {
					assignUser(delegateTask, taskExecutor);
				}
				return true;
			}
			return false;
		}else{
			this.logger.debug("多实例外部调用子流程起始节点后只能跟一个任务节点");
		}
		return false;
	}
	
	/**
	 * 处理分配用户
	 * @param actDefId
	 * @param nodeId
	 * @param delegateTask
	 */
	private void handAssignUserFromDb(String actDefId, String nodeId, DelegateTask delegateTask) {
		BpmNodeParticipantService participantService = SpringContextHolder.getBean(BpmNodeParticipantService.class);
		String actInstId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = this.bpmService.getProcessInstance(actInstId);
		
		List<TaskExecutor> users = null;
		Map<String, Object> vars = delegateTask.getVariables();
		if (processInstance != null) {
			String startUserId = (String) vars.get("startUser");
			String preStepUserId = SecurityContext.getCurrentUserId();
			String preStepOrgId = SecurityContext.getCurrentOrgId();
			vars.put("preOrgId", preStepOrgId);

			if ((StringUtils.isEmpty(startUserId)) && (vars.containsKey("innerPassVars"))) {
				Map localVars = (Map) vars.get("innerPassVars");
				startUserId = (String) localVars.get("startUser");
			}
			users = participantService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preStepUserId, vars);
		} else {
			String startUserId = (String) vars.get("startUser");
			if ((StringUtils.isEmpty(startUserId)) && (vars.containsKey("innerPassVars"))) {
				Map localVars = (Map) vars.get("innerPassVars");
				startUserId = (String) localVars.get("startUser");
			}
			users = participantService.getExeUserIds(actDefId, null, nodeId, startUserId, startUserId, vars);
		}
		assignUser(delegateTask, users);
	}
	 
	/**
	 * 设置节点的执行人
	 * @param delegateTask
	 * @param executors
	 */
	private void assignUser(DelegateTask delegateTask, List<TaskExecutor> executors) {
		if (BeanUtils.isEmpty(executors)) {
			String msg = "节点:" + delegateTask.getName() + ",没有设置执行人";
			throw new BusinessException(msg);
		}
		if (executors.size() == 1) {
			TaskExecutor taskExecutor = (TaskExecutor) executors.get(0);
			TaskOpinion taskOpinion;
			if ("user".equals(taskExecutor.getType())) {
				String userId = taskExecutor.getExecuteId();
				User user = null;
		 
				if (isAllowAgent()) {//是否运行代理，若允许代理取得代理用户
					user = this.bpmAgentSettingService.getAgent(delegateTask, userId);
				}
				if (user != null) {
					/**如果设置了代理则设置任务执行用户为代理用户，任务拥有者为原分配用户**/
		        	delegateTask.setAssignee(user.getZjid().toString());
		        	delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
		        	delegateTask.setOwner(taskExecutor.getExecuteId());
				} else {
		        	delegateTask.setAssignee(taskExecutor.getExecuteId());
				}
		 
				taskOpinion = this.taskOpinionService.getTaskOpinionByTaskId(delegateTask.getId());
				user = userService.getUserById(userId);
				taskOpinion.setExeUserId(user.getZjid());
		        taskOpinion.setExeUserName(user.getYhmc());
		        this.taskOpinionService.updateTaskOpinion(taskOpinion);
			} else {
				delegateTask.setAssignee("0");
				delegateTask.setOwner("0");
				List<TaskExecutor> list = getByTaskExecutor(taskExecutor);
				if (BeanUtils.isEmpty(list)) {
					String msg = getNotAssignMessage(taskExecutor);
					throw new BusinessException(msg);
				}
				for (TaskExecutor ex : list) {
					if (ex.getType().equals("user")) {
						delegateTask.addCandidateUser(ex.getExecuteId());
					} else{
						delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
					}
				}
			}
		} else {
			delegateTask.setAssignee("0");
			delegateTask.setOwner("0");
			Set<TaskExecutor> set = getByTaskExecutors(executors);
			if (BeanUtils.isEmpty(set)) {
				String msg = "没有设置人员,请检查人员配置!";
				throw new BusinessException(msg);
			}
			for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();) {
				TaskExecutor ex = (TaskExecutor) it.next();
				if (ex.getType().equals("user")) {
					delegateTask.addCandidateUser(ex.getExecuteId());
				} else{
					delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
				}
			}
		}
	}
	
	/**
	 * TaskExecutor转换为用户类型
	 * @param taskExecutor
	 * @return
	 */
	protected List<TaskExecutor> getByTaskExecutor(TaskExecutor taskExecutor) {
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		if (taskExecutor.getExactType() == 0) {
			list.add(taskExecutor);
		} else {
			Set<User> userList = taskExecutor.getUser();
			for (User user : userList) {
				list.add(TaskExecutor.getTaskUser(user.getZjid(), user.getYhmc()));
			}
		}
		return list;
	}
	
	/**
	 * TaskExecutor转换为用户类型
	 * @param taskExecutor
	 * @return
	 */
	protected Set<TaskExecutor> getByTaskExecutors(List<TaskExecutor> list){
		Set<TaskExecutor> exSet = new LinkedHashSet<TaskExecutor>();
		for (TaskExecutor ex : list) {
			List<TaskExecutor> tmp = getByTaskExecutor(ex);
			exSet.addAll(tmp);
		}
		return exSet;
	}
	
	@Override
	protected String getScriptType() {
		return BpmConstants.START_SCRIPT;
	}

}
