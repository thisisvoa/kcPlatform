package com.casic27.platform.bpm.service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.activity.ActivityRequiredException;
import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.bpm.dao.IBpmDefinitionMapper;
import com.casic27.platform.bpm.dao.IBpmMapper;
import com.casic27.platform.bpm.dao.IExecutionExtMapper;
import com.casic27.platform.bpm.dao.IHistoricProcessInstanceMapper;
import com.casic27.platform.bpm.dao.IHistoryActivityInstanceMapper;
import com.casic27.platform.bpm.dao.IProcessExecutionMapper;
import com.casic27.platform.bpm.dao.IProcessTaskHistoryMapper;
import com.casic27.platform.bpm.dao.IProcessTaskMapper;
import com.casic27.platform.bpm.dao.ITaskUserMapper;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmNodeConfig;
import com.casic27.platform.bpm.entity.FlowNode;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.ProcessExecution;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.bpm.entity.ProcessTaskHistory;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.entity.TaskFork;
import com.casic27.platform.bpm.entity.TaskNodeStatus;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.entity.TaskUser;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.bpm.util.BpmUtil;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;

@Service("bpmService")
public class BpmService extends BaseService {
	private Logger logger = LoggerFactory.getLogger(BpmService.class);
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Resource
	ProcessEngineConfiguration processEngineConfiguration;
	
	@Autowired
	private BpmNodeConfigService bpmNodeConfigService;
	
	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private BpmFormRunService bpmFormRunService;
	
	@Autowired
	private BpmProStatusService bpmProStatusService;
	
	@Autowired
	private ExecutionStackService executionStackService;
	
	@Autowired
	private IBpmDefinitionMapper bpmDefinitionMapper;

	@Autowired
	private IBpmMapper bpmMapper;
	
	@Autowired
	private IProcessTaskMapper processTaskMapper;
	
	@Autowired
	private IProcessExecutionMapper processExecutionMapper;
	
	@Autowired
	private IExecutionExtMapper executionExtMapper;
	
	@Autowired
	private IProcessTaskHistoryMapper processTaskHistoryMapper;
	
	@Autowired
	private ITaskUserMapper taskUserMapper;
	
	@Autowired
	private IHistoryActivityInstanceMapper historyActivityInstanceMapper;
	
	@Autowired
	private IHistoricProcessInstanceMapper historicProcessInstanceMapper;
	
	@Autowired
	private TaskForkService taskForkService;
	
	@Autowired
	private BpmNodeParticipantService bpmNodeParticipantService;
	
	/**
	 * 流程流转锁
	 */
	private Lock tranLock = new ReentrantLock();
	/**
	 * 发布为activiti流程
	 * @param name
	 * @param xml
	 * @return
	 */
	public Deployment deploy(String name, String xml) {
		Deployment deploy;
		try {
			deploy = this.repositoryService.createDeployment().name(name).addInputStream("bpmn20.xml", new ByteArrayInputStream(xml.getBytes("UTF-8"))).deploy();
			logger.info("deploy process " + name);
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(e);
		}
		return deploy;
	}
	
	
	public void wirteDefXml(String deployId, String defXml) {
		try {
			this.bpmMapper.wirteDefXml(deployId, defXml.getBytes("UTF-8"));
			((ProcessEngineConfigurationImpl) this.processEngineConfiguration).getProcessDefinitionCache().clear();
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException("更新activiti部署XML失败", e);
		}
	}
	
	/**
	 * 根据流程部署ID获取流程定义
	 * @param deployId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByDeployId(String deployId) {
		ProcessDefinition proDefinition = (ProcessDefinition) this.repositoryService
												.createProcessDefinitionQuery().deploymentId(deployId)
												.singleResult();
		if (proDefinition == null)
			return null;
		return getProcessDefinitionByDefId(proDefinition.getId());
	}
	
	/**
	 * 根绝流程定义ID，获取流程定义
	 * @param actDefId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByDefId(String actDefId) {
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService).getDeployedProcessDefinition(actDefId);
		return ent;
	}

	/**
	 * 根据流程实例ID获取流程定义
	 * @param processInstanceId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByProcessInanceId(String processInstanceId) {
		String processDefinitionId = null;
		ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			HistoricProcessInstance hisProInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			processDefinitionId = hisProInstance.getProcessDefinitionId();
		} else {
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		return getProcessDefinitionByDefId(processDefinitionId);
	}
	
	/**
	 * 获取任务所属流程定义
	 * @param taskId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionByTaskId(String taskId){
		TaskEntity taskEntity = (TaskEntity)this.taskService.createTaskQuery().taskId(taskId).singleResult();
		return getProcessDefinitionByDefId(taskEntity.getProcessDefinitionId());
	}
	
	/**
	 * 获取BpmnModel
	 * @param actDefId
	 * @return
	 */
	public BpmnModel getBpmnModelByDefId(String actDefId){
		BpmnModel model = repositoryService.getBpmnModel(actDefId);
		return model;
	}
	/**
	 * 获取流程定义XML
	 * @param deployId
	 * @return
	 */
	public String getDefXmlByDeployId(String deployId){
	    try {
	    	Blob defXmlBlob = (Blob)(bpmMapper.getDefXmlByDeployId(deployId)).get("BYTES_");
	    	byte[] defXmlByte = defXmlBlob.getBytes(1L, (int)defXmlBlob.length());
			return new String(defXmlByte, "utf-8");
		}catch (SQLException e){
			throw new BusinessException("查询xml失败！",e);
		}catch (UnsupportedEncodingException e) {
			throw new BusinessException("xml编码转化失败！",e);
		}
	}
	/**
	 * 获取流程定义XML
	 * @param defId
	 * @return
	 */
	public String getDefXmlByDefId(String defId){
		BpmDefinition bpmDefinition =  bpmDefinitionMapper.getBpmDefinitionById(defId);
		return getDefXmlByDeployId(bpmDefinition.getActDeployId());
	}
	
	/**
	 * 获取流程定义XML
	 * @param defId
	 * @return
	 */
	public String getDefXmlByProcessDefinitionId(String processDefinitionId) {
		ProcessDefinitionEntity entity = getProcessDefinitionByDefId(processDefinitionId);
		String defXml = getDefXmlByDeployId(entity.getDeploymentId());
		return defXml;
	}
	
	/**
	 * 根据流程实例ID获取流程定义XML
	 * @param processInstanceId
	 * @return
	 */
	public String getDefXmlByProcessProcessInanceId(String processInstanceId) {
		ProcessDefinitionEntity entity = getProcessDefinitionByProcessInanceId(processInstanceId);
		if (entity == null) {
			return null;
		}
		String defXml = getDefXmlByDeployId(entity.getDeploymentId());
		return defXml;
	}
	/**
	 * 保存流程跳转条件
	 * @param defId
	 * @param forkNode
	 * @param map
	 * @throws Exception
	 */
	public void saveCondition(String defId, String forkNode, Map<String, String> map)throws Exception{
	    BpmDefinition bpmDefinition = bpmDefinitionMapper.getBpmDefinitionById(defId);
	    String deployId = bpmDefinition.getActDeployId().toString();
	    String defXml = getDefXmlByDeployId(deployId);
	    String graphXml = bpmDefinition.getDefXml();
	    defXml = BpmUtil.setCondition(forkNode, map, defXml);
	    graphXml = BpmUtil.setGraphXml(forkNode, map, graphXml);
	    bpmDefinition.setDefXml(graphXml);
	    bpmMapper.wirteDefXml(deployId, defXml.getBytes("utf-8"));
	    bpmDefinitionMapper.updateBpmDefinition(bpmDefinition);
	    ((ProcessEngineConfigurationImpl)this.processEngineConfiguration).getProcessDefinitionCache().remove(bpmDefinition.getActDefId());
	}
	
	/**
	 * 根据流程ID启动流程
	 * @param porcessDefId
	 * @param businessKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startFlowById(String porcessDefId, String businessKey, Map<String, Object> variables){
		 ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(porcessDefId, businessKey, variables);
		 return processInstance;
	}
	
	/**
	 * 根据流程Key启动流程
	 * @param processDefKey
	 * @param businessKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startFlowByKey(String processDefKey, String businessKey, Map<String, Object> variables){
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(processDefKey, businessKey, variables);
		return processInstance;
	}
	
	/**
	 * 获取流程实例
	 * @param actInstId
	 * @return
	 */
	public ProcessInstance getProcessInstance(String actInstId) {
		ProcessInstance processInstance = (ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(actInstId).singleResult();
		return processInstance;
	}
	
	/**
	 * 获取获选用户
	 * @param taskId
	 * @return
	 */
	public List<TaskUser> getCandidateUsers(String taskId){
		return this.taskUserMapper.getByTaskId(taskId);
	}
	
	/**
	 * 完成任务
	 * @param taskId
	 */
	public void onlyCompleteTask(String taskId) {
		tranLock.lock();
		try {
			TaskEntity task = getTask(taskId);
			ProcessDefinitionEntity processDefinition = getProcessDefinitionByDefId(task.getProcessDefinitionId());
			ActivityImpl curActi = processDefinition.findActivity(task.getTaskDefinitionKey());
			List<PvmTransition> oriPvmTransitionList = clearTransition(curActi);
			this.taskService.complete(task.getId());
			restoreTransition(curActi, oriPvmTransitionList);
		} finally{
			tranLock.unlock();
		}
	}
	
	/**
	 * 跳转至下个任务处理节点
	 * @param taskId
	 * @param toNode
	 * @throws ActivityRequiredException
	 */
	public void transTo(String taskId, String toNode) throws ActivityRequiredException {
		tranLock.lock();
		try {
			TaskEntity task = getTask(taskId);
			ProcessDefinitionEntity processDefinition = getProcessDefinitionByDefId(task.getProcessDefinitionId());
			ActivityImpl curActi = processDefinition.findActivity(task.getTaskDefinitionKey());
			BpmNodeConfig bpmNodeConfig = null;
			ActivityImpl destAct = null;
			boolean isNeedRemoveTran = false;
			if ("_RULE_INVALID".equals(toNode)) {
				isNeedRemoveTran = true;
			} else {
				if (StringUtils.isEmpty(toNode)) {
					for (PvmTransition tran : curActi.getOutgoingTransitions()) {
						String destActId = tran.getDestination().getId();
						bpmNodeConfig = this.bpmNodeConfigService.getByActDefIdJoinTaskKey(task.getProcessDefinitionId(), destActId);
						if (bpmNodeConfig != null) {
							destAct = (ActivityImpl) tran.getDestination();
							break;
						}
					}
				} else{
					destAct = processDefinition.findActivity(toNode);
				}
				if (destAct == null) {
					this.taskService.complete(task.getId());
					return;
				}
				if (bpmNodeConfig == null) {
					bpmNodeConfig = this.bpmNodeConfigService.getByActDefIdJoinTaskKey(task.getProcessDefinitionId(), destAct.getId());
				}
				if (bpmNodeConfig != null) {
					String token = (String) this.taskService.getVariableLocal(task.getId(), TaskFork.TAKEN_VAR_NAME);//处理分发任务
					if (token != null) {
						TaskFork taskFork = this.taskForkService.getByInstIdJoinTaskKeyForkToken(task.getProcessInstanceId(),destAct.getId(), token);
						if (taskFork != null) {
							if (taskFork.getFininshCount().intValue() < taskFork.getForkCount().intValue() - 1) {
								taskFork.setFininshCount(Integer.valueOf(taskFork.getFininshCount().intValue() + 1));
								this.taskForkService.updateTaskFork(taskFork);
								String[] tokenSplits = token.split("[_]");
								if (tokenSplits.length == 2) {
									this.taskService.setVariableLocal(task.getId(), TaskFork.TAKEN_VAR_NAME, null);
								}
								isNeedRemoveTran = true;
							} else {
								String executionId = task.getExecutionId();
								this.taskForkService.deleteById(taskFork.getTaskForkId());
								String[] tokenSplits = token.split("[_]");
								if (tokenSplits.length == 2) {
									this.taskService.setVariableLocal(task.getId(), TaskFork.TAKEN_VAR_NAME, null);
									String instanceId = task.getProcessInstanceId();
									ExecutionEntity ent = executionExtMapper.getById(executionId);
									ActivityImpl curAct = processDefinition.findActivity(ent.getActivityId());
									ExecutionEntity processInstance = executionExtMapper.getById(instanceId);
									processInstance.setActivity(curAct);
									executionExtMapper.update(processInstance);

									this.processTaskMapper.updateTaskExecution(taskId);
									this.processExecutionMapper.delTokenVarByTaskId(taskId, TaskFork.TAKEN_VAR_NAME);

									this.processExecutionMapper.delVarsByExecutionId(executionId);
									this.processExecutionMapper.delExecutionById(executionId);
								} else if (tokenSplits.length >= 3) {
									String newToken = token.substring(0, token.lastIndexOf("_" + tokenSplits[(tokenSplits.length - 1)]));
									this.taskService.setVariableLocal(task.getId(), TaskFork.TAKEN_VAR_NAME, newToken);
								}
							}
						}
					}
				}
			}
			
			List<PvmTransition> oriPvmTransitionList = clearTransition(curActi);
			if (!isNeedRemoveTran) {
				TransitionImpl transitionImpl = curActi.createOutgoingTransition();
				transitionImpl.setDestination(destAct);
			}
			this.taskService.complete(task.getId());
			restoreTransition(curActi, oriPvmTransitionList);
		} finally{
			tranLock.unlock();
		}
	}
    
	/**
	 * 清空指定活动节点流向
	 * @param activityImpl
	 * @return
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}
	
	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}
    
	/**
	 * 获取流程实例任务节点
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessTask> getTasks(String processInstanceId) {
		List<ProcessTask> taskList = this.processTaskMapper.getByInstanceId(processInstanceId);
		return taskList;
	}
	
	/**
	 * 取得任务节点
	 * @param taskId
	 * @return
	 */
    public TaskEntity getTask(String taskId){
    	TaskEntity taskEntity = (TaskEntity)this.taskService.createTaskQuery().taskId(taskId).singleResult();
    	return taskEntity;
    }
    
    /**
     * 任务节点列表
     * @param actDefId
     * @param nodeId
     * @return
     */
    public List<Map<String, String>> getTaskNodes(String actDefId, String nodeId){
    	Map<String, String> nodeMaps = getExecuteNodesMap(actDefId, true);
    	if (nodeMaps.containsKey(nodeId)) {
    		nodeMaps.remove(nodeId);
    	}
    	List<Map<String, String>> result = new ArrayList<Map<String,String>>();
    	Set<String> keySet = nodeMaps.keySet();
    	for(String key:keySet){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("id", key);
    		map.put("name", nodeMaps.get(key));
    		result.add(map);
    	}
    	return result;
    }
    
    /**
     * 获取执行节点
     * @param actDefId
     * @param includeSubProcess
     * @return
     */
	public Map<String, String> getExecuteNodesMap(String actDefId, boolean includeSubProcess){
		Map<String, String> nodeMap = new HashMap<String, String>();
		List<ActivityImpl> acts = getActivityNodes(actDefId);
		for (ActivityImpl actImpl : acts) {
			String nodeType = (String)actImpl.getProperties().get("type");
			if (nodeType.indexOf("Task") != -1) {
				String name = (String)actImpl.getProperties().get("name");
				nodeMap.put(actImpl.getId(), name);
			}
			else if ((includeSubProcess) && ("subProcess".equals(nodeType))) {
				nodeMap.putAll(getExecuteNodes(actImpl));
			}
		}
      return nodeMap;
    }
    
	/**
	 * 获取所有任务节点
	 * @param actDefId
	 * @return
	 */
    public List<ActivityImpl> getActivityNodes(String actDefId){
    	ProcessDefinitionEntity ent = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.repositoryService).getDeployedProcessDefinition(actDefId);
    	return ent.getActivities();
    }
    
    /**
     * 新建Activiti Task
     * @param orgTaskId
     * @param assignee
     * @return
     */
	public ProcessTask newTask(String orgTaskId, String assignee) {
		return newTask(orgTaskId, assignee, null, null);
	}

	/**
	 * 创建新任务
	 * @param orgTaskId
	 * @param assignee
	 * @param newNodeId
	 * @param newNodeName
	 * @return
	 */
	public ProcessTask newTask(String orgTaskId, String assignee, String newNodeId, String newNodeName) {
		String newExecutionId = this.idGenerator.getNextId();
		String newTaskId = this.idGenerator.getNextId();

		TaskEntity taskEntity = getTask(orgTaskId);
		ExecutionEntity executionEntity = null;
		executionEntity = executionExtMapper.getById(taskEntity.getExecutionId());

		ProcessExecution newExecution = new ProcessExecution(executionEntity);
		newExecution.setId(newExecutionId);

		ProcessTask newTask = new ProcessTask();
		BeanUtils.copyProperties(newTask, taskEntity);
		newTask.setId(newTaskId);
		newTask.setExecutionId(newExecutionId);
		newTask.setCreateTime(new Date());

		newTask.setAssignee(assignee);
		newTask.setOwner(assignee);

		ProcessTaskHistory newTaskHistory = new ProcessTaskHistory(taskEntity);
		newTaskHistory.setAssignee(assignee);
		newTaskHistory.setStartTime(new Date());
		newTaskHistory.setId(newTaskId);
		newTaskHistory.setOwner(assignee);

		if (newNodeId != null) {
			newExecution.setActivityId(newNodeId);
			newTask.setTaskDefinitionKey(newNodeId);
			newTask.setName(newNodeName);
			newTaskHistory.setTaskDefinitionKey(newNodeId);
			newTaskHistory.setName(newNodeName);
		}
		this.processExecutionMapper.addExecution(newExecution);
		this.processTaskMapper.addTask(newTask);
		this.processTaskHistoryMapper.addTaskHistory(newTaskHistory);
		return newTask;
	}
	
	/**
	 * 新建任务对象
	 * @param taskEntity
	 * @param taskExecutor
	 * @return
	 */
	protected ProcessTask newTask(TaskEntity taskEntity, TaskExecutor taskExecutor) {
		String newExecutionId = this.idGenerator.getNextId();
		String newTaskId = this.idGenerator.getNextId();
		ProcessExecution newExecution = new ProcessExecution(taskEntity.getExecution());
		newExecution.setId(newExecutionId);
		ProcessTask newTask = new ProcessTask();
		BeanUtils.copyProperties(newTask, taskEntity);
		newTask.setId(newTaskId);
		newTask.setExecutionId(newExecutionId);
		newTask.setCreateTime(new Date());
		ProcessTaskHistory newTaskHistory = new ProcessTaskHistory(taskEntity);

		TaskUser taskUser = null;

		String executorId = taskExecutor.getExecuteId();

		if ("user".equals(taskExecutor.getType())) {
			newTask.setAssignee(executorId);
			newTask.setOwner(executorId);
			newTaskHistory.setAssignee(executorId);
			newTaskHistory.setOwner(executorId);
		} else {
			taskUser = new TaskUser();
			taskUser.setId(this.idGenerator.getNextId());
			taskUser.setGroupId(executorId);
			taskUser.setType(taskExecutor.getType());
			taskUser.setReversion(Integer.valueOf(1));
			taskUser.setTaskId(newTaskId);
		}

		newTaskHistory.setStartTime(new Date());
		newTaskHistory.setId(newTaskId);

		this.processExecutionMapper.addExecution(newExecution);
		this.processTaskMapper.addTask(newTask);
		this.processTaskHistoryMapper.addTaskHistory(newTaskHistory);
		if (taskUser != null) {
			this.taskUserMapper.addTaskUser(taskUser);
		}

		return newTask;
	}
	
	/**
	 * 新建分发任务
	 * @param taskEntity
	 * @param uIds
	 */
    public void newForkTasks(TaskEntity taskEntity, List<TaskExecutor> uIds){
      String token = (String)taskEntity.getVariableLocal(TaskFork.TAKEN_VAR_NAME);
      if (token == null) token = TaskFork.TAKEN_PRE;
      Iterator<TaskExecutor> uIt = uIds.iterator();
      int i = 0;
      while (uIt.hasNext())
        if (i++ == 0) {
          assignTask(taskEntity, (TaskExecutor)uIt.next());
          taskEntity.setVariableLocal(TaskFork.TAKEN_VAR_NAME, token + "_" + i);
          changeTaskExecution(taskEntity);
        } else {
          ProcessTask processTask = newTask(taskEntity, (TaskExecutor)uIt.next());
  
          TaskEntity newTask = getTask(processTask.getId());
  
          TaskThreadService.addTask(newTask);
  
          this.taskService.setVariableLocal(processTask.getId(), TaskFork.TAKEN_VAR_NAME, token + "_" + i);
  
          TaskOpinion taskOpinion = new TaskOpinion(processTask);
          taskOpinion.setOpinionId(CodeGenerator.getUUID32());
          taskOpinion.setTaskToken(token);
          this.taskOpinionService.addTaskOpinion(taskOpinion);
        }
    }
    
    /**
     * 设置任务执行用户为null
     * @param taskId
     */
    public void updateTaskAssigneeNull(String taskId){
      this.processTaskMapper.updateTaskAssigneeNull(taskId);
    }
    
    /**
     * 获取流程实例第一个任务
     * @param instanceId
     * @return
     */
	public ProcessTask getFirstNodeTask(String instanceId) {
		List<ProcessTask> tasks = this.processTaskMapper.getTaskByActDefId(instanceId);
		if (BeanUtils.isEmpty(tasks))
			return null;
		return (ProcessTask) tasks.get(0);
	}
	
	/**
	 * 查询获取所有任务列表
	 * @param queryMap
	 * @return
	 */
	public List<ProcessTask> getAllTask(Map<String,Object> queryMap){
		return processTaskMapper.getAllTask(queryMap);
	}
    
	/**
	 * 获取我的任务列表
	 * @param queryMap
	 * @return
	 */
	public List<ProcessTask> getMyTask(Map<String,Object> queryMap){
		return processTaskMapper.getMyTask(queryMap);
	}
	
    /**
     * 根据ID查询Execution
     * @param ExecutionId
     * @return
     */
    public ExecutionEntity getExecution(String ExecutionId){
    	return executionExtMapper.getById(ExecutionId);
    }
    /**
     * 获取Task的所属Execution
     * @param taskId
     * @return
     */
	public ExecutionEntity getExecutionByTaskId(String taskId) {
		TaskEntity taskEntity = getTask(taskId);
		if (taskEntity.getExecutionId() == null){
			return null;
		}
			
		return executionExtMapper.getById(taskEntity.getExecutionId());
	}

    /**
     * 分配任务执行者
     * @param taskEntity
     * @param taskExecutor
     */
	public void assignTask(TaskEntity taskEntity, TaskExecutor taskExecutor){
    	if ("user".equals(taskExecutor.getType())) {
    		taskEntity.setAssignee(taskExecutor.getExecuteId());
    		taskEntity.setOwner(taskExecutor.getExecuteId());
    	} else {
    		taskEntity.addGroupIdentityLink(taskExecutor.getExecuteId(), taskExecutor.getType());
    	}
    }
	
	/**
	 * 设置任务变量
	 * @param taskId
	 * @param variableName
	 * @param varVal
	 */
	public void setTaskVariable(String taskId, String variableName, Object varVal) {
		this.taskService.setVariable(taskId, variableName, varVal);
	}
	
	/**
	 * 改变ACTIVITI任务所属实例
	 * @param taskEntity
	 */
	protected void changeTaskExecution(TaskEntity taskEntity) {
		String newExecutionId = this.idGenerator.getNextId();
		ProcessExecution newExecution = new ProcessExecution(taskEntity.getExecution());
		newExecution.setId(newExecutionId);
		this.processExecutionMapper.addExecution(newExecution);
		taskEntity.setExecutionId(newExecutionId);
	}
    
	protected Map<String, String> getExecuteNodes(ActivityImpl actImpl){
		Map<String, String> nodeMap = new HashMap<String, String>();
		List<ActivityImpl> acts = actImpl.getActivities();
		if (acts.size() == 0) return nodeMap;
		for (ActivityImpl act : acts) {
			String nodeType = (String) act.getProperties().get("type");
			if (nodeType.indexOf("Task") != -1) {
				String name = (String) act.getProperties().get("name");
				nodeMap.put(act.getId(), name);
			} else if ("subProcess".equals(nodeType)) {
				nodeMap.putAll(getExecuteNodes(act));
			}
		}
		return nodeMap;
    }
	/**
	 * 判断阶段是否会签节点
	 * @param taskEntity
	 * @return
	 */
	public boolean isSignTask(TaskEntity taskEntity) {
		RepositoryServiceImpl impl = (RepositoryServiceImpl) this.repositoryService;
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
		ActivityImpl taskAct = ent.findActivity(taskEntity.getTaskDefinitionKey());
		String multiInstance = (String) taskAct.getProperty("multiInstance");
		if (multiInstance != null)
			return true;
		return false;
	}
	/**
	 * 判断节点是否是会签节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean isSignTask(String actDefId, String nodeId) {
		ProcessDefinitionEntity ent = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService).getDeployedProcessDefinition(actDefId);
		List<ActivityImpl> list = ent.getActivities();
		for (ActivityImpl actImpl : list) {
			if (actImpl.getId().equals(nodeId)) {
				String multiInstance = (String) actImpl.getProperty("multiInstance");
				if (multiInstance != null){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 根据ID查询TaskHistory
	 * @param id
	 * @return
	 */
	public ProcessTaskHistory getTaskHistoryById(String id){
		return processTaskHistoryMapper.getTaskHistoryById(id);
	}
	
	/**
	 * 判断节点是否可以回退
	 * @param task
	 * @return
	 */
	public boolean getIsAllowBackByTask(TaskEntity task) {
		return getIsAllowBackByTask(task.getProcessDefinitionId(),
				task.getTaskDefinitionKey());
	}

	/**
	 * 判断节点是否可以回退
	 * @param task
	 * @return
	 */
	public boolean getIsAllowBackByTask(ProcessTask task) {
		return getIsAllowBackByTask(task.getProcessDefinitionId(),
				task.getTaskDefinitionKey());
	}

	/**
	 * 判断节点是否可以回退
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean getIsAllowBackByTask(String actDefId, String nodeId) {
		boolean rtn = NodeCache.isFirstNode(actDefId, nodeId);
		if (rtn) {
			return false;
		}
		Map<String,FlowNode> map = NodeCache.getByActDefId(actDefId);
		FlowNode flowNode = (FlowNode) map.get(nodeId);
		List<FlowNode> preFlowNodeList = flowNode.getPreFlowNodes();
		for (FlowNode preNode : preFlowNodeList) {
			if ((preNode.getIsMultiInstance().booleanValue()) && (!"userTask".equals(preNode.getNodeType()))){
				return false;
			}
			if ((!"exclusiveGateway".equals(preNode.getNodeType())) && (!"userTask".equals(preNode.getNodeType()))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 人工结束流程
	 * @param instanceId
	 * @param nodeId
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	public ProcessRun endProcessByInstanceId(String instanceId, String nodeId, String memo) throws Exception {
		User sysUser = SecurityContext.getCurrentUser();
		ProcessRun processRun = this.processRunService.getProcessRunByActInstId(instanceId);
		processRun.setStatus(ProcessRun.STATUS_MANUAL_FINISH);
		processRun.setEndTime(new Date());

		long duration = this.processRunService.getProcessDuration(processRun).longValue();
		long lastSubmitDuration = this.processRunService.getProcessLastSubmitDuration(processRun).longValue();
		processRun.setDuration(Long.valueOf(duration));
		processRun.setLastSubmitDuration(Long.valueOf(lastSubmitDuration));

		this.processRunService.updateProcessRun(processRun);//修改流程实例信息
		this.processTaskMapper.delCandidateByInstanceId(instanceId);//删除任务执行人ID
		this.processTaskMapper.delByInstanceId(instanceId);//删除任务
		this.taskUserMapper.delByInstanceId(instanceId);//删除流程任务分配人员
		this.bpmFormRunService.delByInstanceId(instanceId);//删除表单
		this.executionStackService.delByActInstId(processRun.getActInstId());//删除堆栈信息
		updHistoryActInst(instanceId, nodeId, sysUser.getZjid().toString());

		HistoricProcessInstanceEntity processInstance = (HistoricProcessInstanceEntity) this.historyService
															.createHistoricProcessInstanceQuery().processInstanceId(instanceId.toString()).singleResult();
		processInstance.setEndTime(new Date());
		processInstance.setDurationInMillis(Long.valueOf(System.currentTimeMillis() - processInstance.getStartTime().getTime()));
		processInstance.setEndActivityId(nodeId);
		
		this.historicProcessInstanceMapper.update(processInstance);
		this.processExecutionMapper.delVariableByProcInstId(instanceId);//删除变量
		this.processExecutionMapper.delExecutionByProcInstId(instanceId);//删除Execution

		this.bpmProStatusService.updStatus(instanceId, nodeId,TaskOpinion.STATUS_ENDPROCESS);
		updateTaskOpinion(instanceId, memo, sysUser);//修改审批记录
		return processRun;
	}
	
	/**
	 * 修改历史活动实例
	 * @param actInstId
	 * @param nodeId
	 * @param assignee
	 */
	public void updHistoryActInst(String actInstId, String nodeId, String assignee) {
		List<HistoricActivityInstanceEntity> hisList = this.historyActivityInstanceMapper.getByInstanceId(actInstId, nodeId);
		for (HistoricActivityInstanceEntity hisActInst : hisList) {
			Date endTime = new Date();
			hisActInst.setEndTime(endTime);
			hisActInst.setDurationInMillis(Long.valueOf(System.currentTimeMillis() - hisActInst.getStartTime().getTime()));
			if ((StringUtils.isEmpty(hisActInst.getAssignee())) || (!hisActInst.getAssignee().equals(assignee))) {
				hisActInst.setAssignee(assignee);
			}
			this.historyActivityInstanceMapper.update(hisActInst);
		}
	}
	
	/**
	 * 修改流程审批
	 * @param instanceId
	 * @param memo
	 * @param sysUser
	 */
	private void updateTaskOpinion(String instanceId, String memo, User sysUser) {
		List<TaskOpinion> list = this.taskOpinionService.getCheckOpinionByInstId(instanceId);
		for (TaskOpinion taskOpion : list) {
			taskOpion.setCheckStatus(TaskOpinion.STATUS_ENDPROCESS);
			taskOpion.setExeUserId(sysUser.getZjid());
			taskOpion.setExeUserName(sysUser.getYhmc());
			taskOpion.setEndTime(new Date());
			taskOpion.setOpinion(memo);
			Long duration = taskOpion.getEndTime().getTime()-taskOpion.getStartTime().getTime();
			taskOpion.setDurTime(duration);
			this.taskOpinionService.updateTaskOpinion(taskOpion);
		}
	}
	/**
	 * 逻辑删除流程实例
	 * @param instanceId
	 * @param memo
	 * @return
	 */
	public ProcessRun delProcessByInstanceId(String instanceId, String memo) {
		User sysUser = SecurityContext.getCurrentUser();
		List<ProcessTask> list = getTasks(instanceId.toString());
		String nodeId = "";
		if (BeanUtils.isNotEmpty(list)) {
			ProcessTask task = (ProcessTask) list.get(0);
			nodeId = task.getTaskDefinitionKey();
		}

		ProcessRun processRun = this.processRunService.getProcessRunByActInstId(instanceId);
		processRun.setStatus(ProcessRun.STATUS_DELETE);//修改流程状态
		processRun.setEndTime(new Date());
		Long duration = this.processRunService.getProcessDuration(processRun);
		processRun.setDuration(duration);
		Long lastSubmitDuration = this.processRunService.getProcessLastSubmitDuration(processRun);
		processRun.setLastSubmitDuration(lastSubmitDuration);
		this.processRunService.updateProcessRun(processRun);
		this.processTaskMapper.delCandidateByInstanceId(instanceId);
		this.processTaskMapper.delByInstanceId(instanceId);
		this.taskUserMapper.delByInstanceId(instanceId);
		this.bpmFormRunService.delByInstanceId(instanceId);//删除表单
		
		updHistoryActInst(instanceId, nodeId, sysUser.getZjid().toString());
		HistoricProcessInstanceEntity processInstance = (HistoricProcessInstanceEntity) this.historyService.createHistoricProcessInstanceQuery()
																		.processInstanceId(instanceId.toString()).singleResult();
		processInstance.setEndTime(new Date());
		processInstance.setDurationInMillis(Long.valueOf(System.currentTimeMillis() - processInstance.getStartTime().getTime()));
		processInstance.setEndActivityId(nodeId);
		this.historicProcessInstanceMapper.update(processInstance);
		this.processExecutionMapper.delVariableByProcInstId(instanceId);
		this.processExecutionMapper.delExecutionByProcInstId(instanceId);
		this.bpmProStatusService.updStatus(instanceId, nodeId, TaskOpinion.STATUS_ENDPROCESS);
		updateTaskOpinion(instanceId, memo, sysUser);

		return processRun;
	}
	
	/**
	 * 获取流程节点审批状态
	 * @param actInstId
	 * @return
	 */
	public List<TaskNodeStatus> getNodeCheckStatusInfo(String actInstId) {
		List<TaskNodeStatus> taskNodeStatusList = new ArrayList<TaskNodeStatus>();
		List<TaskOpinion> taskOpinionList = this.taskOpinionService.getByActInstId(actInstId);
		Map<TaskNodeStatus,TaskNodeStatus> map = new HashMap<TaskNodeStatus,TaskNodeStatus>();
		for (TaskOpinion taskOpinion : taskOpinionList) {
			TaskNodeStatus taskNodeStatus = new TaskNodeStatus();
			taskNodeStatus.setActInstId(taskOpinion.getActInstId());
			taskNodeStatus.setTaskKey(taskOpinion.getTaskKey());
			if (map.containsKey(taskNodeStatus)) {
				TaskNodeStatus tmp = (TaskNodeStatus) map.get(taskNodeStatus);
				tmp.getTaskOpinionList().add(taskOpinion);
			} else {
				taskNodeStatus.getTaskOpinionList().add(taskOpinion);
				map.put(taskNodeStatus, taskNodeStatus);
			}
		}
		taskNodeStatusList.addAll(map.values());
		return taskNodeStatusList;
	}
	
	/**
	 * 获取流程节点审批状态
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public TaskNodeStatus getNodeCheckStatusInfo(String actInstId, String nodeId) {
		TaskNodeStatus taskNodeStatus = new TaskNodeStatus();
		List<TaskOpinion> taskOpinionList = this.taskOpinionService.getByActInstIdTaskKey(actInstId, nodeId);
		if (BeanUtils.isNotEmpty(taskOpinionList)) {
			Collections.sort(taskOpinionList, new Comparator<TaskOpinion>() {
				public int compare(TaskOpinion o1, TaskOpinion o2) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				}
			});
			Collections.reverse(taskOpinionList);
			taskNodeStatus.setActInstId(actInstId);
			taskNodeStatus.setTaskKey(nodeId);
			taskNodeStatus.setTaskOpinionList(taskOpinionList);

			TaskOpinion opinion = (TaskOpinion) taskOpinionList.get(taskOpinionList.size() - 1);
			taskNodeStatus.setLastCheckStatus(opinion.getCheckStatus());
		} else {
			ProcessRun processRun = processRunService.getProcessRunByActInstId(actInstId);
			if(processRun==null){
				throw new BusinessException("流程还没有走向！");
			}
			if(processRun.getStatus() != ProcessRun.STATUS_FINISH && processRun.getStatus() != ProcessRun.STATUS_MANUAL_FINISH){
				Map<String,Object> vars = this.runtimeService.getVariables(actInstId);
				String actDefId = processRunService.getProcessRunByActInstId(actInstId).getActDefId();
				List<TaskExecutor> taskExecutorList = (List<TaskExecutor>)this.bpmNodeParticipantService.getExeUserIds(actDefId, actInstId, nodeId, 
							(String) vars.get("startUser"), (String) vars.get("startUser"), vars);
				taskNodeStatus.setTaskExecutorList(taskExecutorList);
			}
		}
		return taskNodeStatus;
	}
}
