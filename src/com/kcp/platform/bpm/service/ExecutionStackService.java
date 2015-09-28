package com.kcp.platform.bpm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.BeanUtils;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IExecutionStackMapper;
import com.kcp.platform.bpm.entity.ExecutionStack;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.entity.ProcessCmd;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.service.thread.TaskThreadService;
import com.kcp.platform.bpm.service.thread.TaskUserAssignService;
import com.kcp.platform.common.user.entity.User;

@Service
public class ExecutionStackService {
	@Autowired
	private IExecutionStackMapper executionStackMapper;

	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private TaskUserAssignService taskUserAssignService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private BpmProStatusService bpmProStatusService;

	/**
	 * 查询列表(不支持分页)
	 * 
	 * @param queryMap
	 */
	public List<ExecutionStack> findExecutionStack(Map<String, Object> queryMap) {
		return executionStackMapper.findExecutionStack(queryMap);
	}
	
	/**
	 * 初始化任务堆栈
	 * @param actInstId
	 * @param taskList
	 * @throws Exception
	 */
	public void initStack(String actInstId, List<Task> taskList){
		if (BeanUtils.isEmpty(taskList)) return;
		Map<String, ExecutionStack> nodeIdStackMap = new HashMap<String, ExecutionStack>();
		for (Task task : taskList) {
			TaskEntity taskEntity = (TaskEntity) task;
			String nodeId = taskEntity.getTaskDefinitionKey();
			if (!nodeIdStackMap.containsKey(nodeId)) {
				ExecutionStack stack = new ExecutionStack();
				stack.setActInstId(actInstId);
				stack.setActDefId(taskEntity.getProcessDefinitionId());
				stack.setAssignees(task.getAssignee());
				stack.setDepth(Integer.valueOf(1));
				stack.setStartTime(new Date());
				stack.setNodeId(nodeId);
				stack.setNodeName(taskEntity.getName());
				stack.setTaskIds(taskEntity.getId());
				String stackId = CodeGenerator.getUUID32();
				stack.setStackId(stackId);
				stack.setParentId("0");
				stack.setNodePath("0." + stackId + ".");
				nodeIdStackMap.put(nodeId, stack);
			} else {
				ExecutionStack stack = (ExecutionStack) nodeIdStackMap.get(nodeId);
				stack.setIsMultiTask(ExecutionStack.MULTI_TASK);
				stack.setAssignees(stack.getAssignees() + "," + task.getAssignee());
				stack.setTaskIds(stack.getTaskIds() + "," + task.getId());
			}
		}

		Iterator<ExecutionStack> stackIt = nodeIdStackMap.values().iterator();
		while (stackIt.hasNext()) {
			ExecutionStack exeStack = (ExecutionStack) stackIt.next();
			executionStackMapper.addExecutionStack(exeStack);
		}
	}
	
	public void addStack(String actInstId, String destNodeId, String oldTaskToken) throws Exception{
		List<Task> taskList = TaskThreadService.getNewTasks();
		if (taskList != null){
			pushNewTasks(actInstId, destNodeId, taskList, oldTaskToken);
		}
	}

	public void pushNewTasks(String actInstId, String destNodeId, List<Task> newTasks, String oldTaskToken) throws Exception {
		String curUserId = SecurityContext.getCurrentUser().getZjid();
		ExecutionStack curExeNode = getLastestStack(actInstId, destNodeId, oldTaskToken);
		if (curExeNode != null) {
			if (curExeNode.getAssignees() == null) {
				curExeNode.setAssignees(curUserId.toString());
			}
			curExeNode.setEndTime(new Date());
			executionStackMapper.updateExecutionStack(curExeNode);
		}
		ProcessDefinitionEntity processDef = null;
		if (newTasks.size() > 0) {
			Map<String, ExecutionStack> nodeIdStackMap = new HashMap<String, ExecutionStack>();
			for (Task task : newTasks) {
				TaskEntity taskEntity = (TaskEntity) task;
				String nodeId = taskEntity.getTaskDefinitionKey();

				if (processDef == null) {
					processDef = this.bpmService.getProcessDefinitionByDefId(taskEntity.getProcessDefinitionId());
				}

				ActivityImpl taskAct = processDef.findActivity(nodeId);
				if (taskAct != null) {
					String multiInstance = (String) taskAct.getProperty("multiInstance");
					ExecutionStack stack = (ExecutionStack) nodeIdStackMap.get(nodeId);
					if ((StringUtils.isEmpty(multiInstance)) || (stack == null)) {
						String stackId = CodeGenerator.getUUID32();
						stack = new ExecutionStack();
						stack.setActInstId(taskEntity.getProcessInstanceId());
						stack.setAssignees(taskEntity.getAssignee());
						stack.setActDefId(taskEntity.getProcessDefinitionId());
						if (curExeNode == null) {
							stack.setDepth(1);
							stack.setParentId("0");
							stack.setNodePath("0." + stackId + ".");
						} else {
							stack.setDepth(Integer.valueOf(curExeNode.getDepth() == null ? 1 : curExeNode.getDepth().intValue() + 1));
							stack.setParentId(curExeNode.getStackId());
							stack.setNodePath(curExeNode.getNodePath() + stackId + ".");
						}
						stack.setStartTime(new Date());
						stack.setNodeId(nodeId);
						stack.setNodeName(taskEntity.getName());
						stack.setTaskIds(taskEntity.getId());
						stack.setStackId(stackId);
						nodeIdStackMap.put(nodeId, stack);
					} else if (stack != null) {
						stack.setIsMultiTask(ExecutionStack.MULTI_TASK);
						stack.setAssignees(stack.getAssignees() + ","
								+ task.getAssignee());
						stack.setTaskIds(stack.getTaskIds() + ","
								+ task.getId());
					}
				}
			}
			Iterator<ExecutionStack> stackIt = nodeIdStackMap.values().iterator();
			while (stackIt.hasNext()) {
				ExecutionStack exeStack = (ExecutionStack) stackIt.next();
				executionStackMapper.addExecutionStack(exeStack);
			}
		}
  }
	
	/**
	 * 最新执行堆栈
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public ExecutionStack getLastestStack(String actInstId, String nodeId) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("actInstId", actInstId);
		queryMap.put("nodeId", nodeId);
		List<ExecutionStack> list = findExecutionStack(queryMap);
	    if (list.size() > 0) {
	      return list.get(0);
	    }
	    return null;
	}
	
	/**
	 * 最新执行堆栈
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public ExecutionStack getLastestStack(String actInstId, String nodeId, String taskToken) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("actInstId", actInstId);
		queryMap.put("nodeId", nodeId);
		queryMap.put("taskToken", taskToken);
		List<ExecutionStack> list = findExecutionStack(queryMap);
	    if (list.size() > 0) {
	      return list.get(0);
	    }
	    return null;
	}
	/**
	 * 修改执行栈
	 * @param executionStack
	 */
	public void updateExecutionStack(ExecutionStack executionStack){
		executionStackMapper.updateExecutionStack(executionStack);
	}
	
	/**
	 * 回退时候，同时回退执行堆栈
	 * @param processCmd
	 * @param taskEntity
	 * @param taskToken
	 * @return
	 */
	public ExecutionStack backPrepared(ProcessCmd processCmd, TaskEntity taskEntity, String taskToken) {
		ExecutionStack parentStack = null;
		String instanceId = taskEntity.getProcessInstanceId();
		String actDefId = taskEntity.getProcessDefinitionId();
		String nodeId = taskEntity.getTaskDefinitionKey();

		if (StringUtils.isNotEmpty(processCmd.getStackId())) {
			parentStack = (ExecutionStack) this.executionStackMapper.getExecutionStackById(processCmd.getStackId());
		} else if (StringUtils.isEmpty(processCmd.getDestTask())) {
			ExecutionStack executionStack = getLastestStack(instanceId, nodeId, taskToken);//获取最后执行的任务
			if ((executionStack != null) && (StringUtils.isNotEmpty(executionStack.getParentId()))) {
				parentStack = (ExecutionStack) this.executionStackMapper.getExecutionStackById(executionStack.getParentId());
				while (nodeId.equals(parentStack.getNodeId())) {
					parentStack = (ExecutionStack) this.executionStackMapper.getExecutionStackById(parentStack.getParentId());
				}
			}
		}

		if (parentStack != null) {
			processCmd.setDestTask(parentStack.getNodeId());
			boolean rtn = NodeCache.isSignTaskNode(actDefId, parentStack.getNodeId());
			if (!rtn) {
				String assignee = parentStack.getAssignees();
				String[] aryAssignee = assignee.split(",");
				List<TaskExecutor> list = new ArrayList<TaskExecutor>();
				for (String userId : aryAssignee) {
					list.add(TaskExecutor.getTaskUser(userId, ""));
				}
				this.taskUserAssignService.addNodeUser(parentStack.getNodeId(), list);
			}

		}

		return parentStack;
	}
	
	/**
	 * 堆栈pop操作，同时更新任务状态和流程状态
	 * @param parentStack
	 * @param isRecover
	 * @param isBack
	 */
	public void pop(ExecutionStack parentStack, boolean isRecover, Integer isBack) {
		String instanceId = parentStack.getActInstId();
		List<ExecutionStack> subChilds = this.executionStackMapper.getByParentId(parentStack.getStackId());
		User curUser = SecurityContext.getCurrentUser();
		
		if (BeanUtils.isEmpty(subChilds)) return;
		ExecutionStack executionStack = (ExecutionStack) subChilds.get(0);
		String prevNodeId = executionStack.getNodeId();
		Short status = TaskOpinion.STATUS_REJECT;

		if (isBack.intValue() == 1) {
			TaskOpinion taskOpinion = this.taskOpinionService.getLatestTaskOpinion(instanceId, prevNodeId);
			if (taskOpinion != null) {
				taskOpinion.setExeUserId(curUser.getZjid());
				taskOpinion.setExeUserName(curUser.getYhmc());
				taskOpinion.setEndTime(new Date());
				taskOpinion.setDurTime(Long.valueOf(taskOpinion.getEndTime().getTime() - taskOpinion.getStartTime().getTime()));
				if (isRecover) {
					status = TaskOpinion.STATUS_RECOVER;
				}
				taskOpinion.setCheckStatus(status);
				this.taskOpinionService.updateTaskOpinion(taskOpinion);//更新任务审批状态
			}
			this.bpmProStatusService.updStatus(instanceId, prevNodeId, status);//更新流程状态
		}
		this.executionStackMapper.deleteExecutionStack(executionStack.getStackId());
	}
	/**
	 * 根据流程实例ID删除
	 * @param actInstId
	 */
	public void delByActInstId(String actInstId){
		this.executionStackMapper.delByActInstId(actInstId);
	}
}