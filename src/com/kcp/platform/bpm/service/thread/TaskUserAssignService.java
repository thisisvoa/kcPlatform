package com.kcp.platform.bpm.service.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.bpm.entity.FlowNode;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.bpm.service.BpmNodeParticipantService;
import com.kcp.platform.bpm.util.BpmUtil;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.BeanUtils;
import com.kcp.platform.util.StringUtils;

/**
 * TaskUserAssignService用于保存处理当前任务的用户，以便在Listener Service之间进行数据传递
 * @author Administrator
 *
 */
@Service
public class TaskUserAssignService {

	private Logger logger = LoggerFactory.getLogger(TaskUserAssignService.class);
	
	/**
	 * 某个任务节点的处理人员
	 */
	private static ThreadLocal<Map<String, List<TaskExecutor>>> nodeUserMapLocal = new ThreadLocal<Map<String, List<TaskExecutor>>>();
	
	/**
	 * 存储下个任务的处理人员，一般用户储存界面传递过来的用户
	 */
	private static ThreadLocal<List<TaskExecutor>> taskExecutors = new ThreadLocal<List<TaskExecutor>>();

	@Autowired
	private BpmNodeParticipantService bpmNodeUserService;

	public void setNodeUser(Map<String, List<TaskExecutor>> map) {
		nodeUserMapLocal.set(map);
	}
	
	/**
	 * 添加节点的处理人员
	 * @param nodeId
	 * @param userIds
	 */
	public void addNodeUser(String nodeId, String userIds) {
		if (StringUtils.isEmpty(userIds))
			return;
		List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
		addNodeUser(nodeId, executorList);
	}
	
	/**
	 * 添加节点处理人员
	 * @param nodeId
	 * @param executors
	 */
	public void addNodeUser(String nodeId, List<TaskExecutor> executors) {
		if (BeanUtils.isEmpty(executors))
			return;
		Map<String, List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
		if (nodeUserMap == null)
			nodeUserMap = new HashMap<String, List<TaskExecutor>>();
		nodeUserMap.remove(nodeId);
		nodeUserMap.put(nodeId, executors);
		nodeUserMapLocal.set(nodeUserMap);
	}
	
	/**
	 * 添加节点处理人员
	 * @param nodeId
	 * @param executors
	 */
	public void addNodeUser(String[] aryNodeId, String[] aryUserIds) {
		if (BeanUtils.isEmpty(aryUserIds)) return;
		Map<String, List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
		if (nodeUserMap == null) nodeUserMap = new HashMap<String, List<TaskExecutor>>();
		for (int i = 0; i < aryNodeId.length; i++) {
			String nodeId = aryNodeId[i];
			String userIds = aryUserIds[i];
			if (userIds != null) {
				List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
				nodeUserMap.put(nodeId, executorList);
			}
		}
		nodeUserMapLocal.set(nodeUserMap);
	}

	public Map<String, List<TaskExecutor>> getNodeUserMap() {
		return nodeUserMapLocal.get();
	}

	public void clearNodeUserMap() {
		nodeUserMapLocal.remove();
	}
	
	/**
	 * 获取子流程处理用户
	 * @param execution
	 * @return
	 * @throws Exception
	 */
	public List<TaskExecutor> getMultipleUser(ActivityExecution execution)throws Exception {
		String nodeId = execution.getActivity().getId();
		ExecutionEntity executionEnt = (ExecutionEntity) execution;

		String multiInstance = (String) executionEnt.getActivity().getProperty("multiInstance");
		String varName = nodeId + "_" + "subAssignIds";

		if ("sequential".equals(multiInstance)) {
			List<TaskExecutor> list = (List<TaskExecutor>) execution.getVariable(varName);
			if (list != null) return list;
		}

		Map<String, FlowNode> nodeMap = NodeCache.getByActDefId(executionEnt.getProcessDefinitionId());
		FlowNode subProcessNode = nodeMap.get(nodeId);
		FlowNode firstNode = subProcessNode.getSubFirstNode();
		FlowNode secondeNode = firstNode.getNextFlowNodes().get(0);

		List<TaskExecutor> userList = getExecutors();

		if ((BeanUtils.isEmpty(userList)) && (nodeUserMapLocal.get() != null)) {
			userList = (nodeUserMapLocal.get()).get(secondeNode.getNodeId());
		}

		if (BeanUtils.isEmpty(userList)) {
			String actInstId = execution.getProcessInstanceId();
			Map<String,Object> variables = execution.getVariables();
			String startUserId = variables.get("startUser").toString();
			String actDefId = execution.getProcessDefinitionId();
			String preTaskUser = SecurityContext.getCurrentUserId();
			userList = this.bpmNodeUserService.getExeUserIds(actDefId, actInstId, secondeNode.getNodeId(), startUserId, preTaskUser, variables);
		}

		if (BeanUtils.isEmpty(userList)) {
			throw new BusinessException("请设置子流程:[" + secondeNode.getNodeName() + "]的人员!");
		}
		this.logger.debug("userList size:" + userList.size());

		if ((BeanUtils.isNotEmpty(userList)) && ("sequential".equals(multiInstance))) {
			executionEnt.setVariable(varName, userList);
		}
		return userList;
	}
	
	/**
	 * 获取会签用户
	 * @param execution
	 * @return
	 * @throws Exception
	 */
	public List<TaskExecutor> getSignUser(ActivityExecution execution)throws Exception {
		String nodeId = execution.getActivity().getId();
		String nodeName = (String) execution.getActivity().getProperty("name");
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		List<TaskExecutor> userIds = null;
		//会签节点执行用户会存储在nodeId + "_" + "signUsers"为名称的流程变量中
		String varName = nodeId + "_" + "signUsers";
		if ("sequential".equals(multiInstance)) {
			userIds = (List<TaskExecutor>) execution.getVariable(varName);
			if (userIds != null) {
				return userIds;
			}
		}
		//如果下个节点处理用户已经从数据库读取过则使用读取过的用户
		Map<String,List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
		if ((nodeUserMap != null)&& (BeanUtils.isNotEmpty(nodeUserMap.get(nodeId)))) {
			userIds = nodeUserMap.get(nodeId);
			saveExecutorVar(execution, userIds);
			return userIds;
		}
		//如果会签人员是从界面上传递过来的则使用界面上数据
		userIds = getExecutors();
		if (BeanUtils.isNotEmpty(userIds)) {
			saveExecutorVar(execution, userIds);
			addNodeUser(nodeId, userIds);
			return userIds;
		}
		//根据配置信息，从数据库中获取审批人员
		ExecutionEntity ent = (ExecutionEntity) execution;
		String actDefId = ent.getProcessDefinitionId();
		String actInstId = execution.getProcessInstanceId();
		Map<String,Object> variables = execution.getVariables();
		String startUserId = variables.get("startUser").toString();
		String preTaskUser = SecurityContext.getCurrentUserId();
		List<TaskExecutor> list = (List<TaskExecutor>)this.bpmNodeUserService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preTaskUser, variables);
		if (BeanUtils.isEmpty(list)) {
			throw new BusinessException("请设置会签节点:[" + nodeName + "]的人员!");
		}
		
		if (BeanUtils.isNotEmpty(list)) {
			saveExecutorVar(execution, list);
		}
		addNodeUser(nodeId, list);
		return list;
	}
	
	/**
	 * 如果是串行会签将会签节点执行用户保存在nodeId + "_" + "signUsers"流程变量中，以便创建下个会签任务时候不需重新到数据库查询
	 * @param execution
	 * @param userIds
	 */
	private void saveExecutorVar(ActivityExecution execution, List<TaskExecutor> userIds) {
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		if ("sequential".equals(multiInstance)) {
			String nodeId = execution.getActivity().getId();
			String varName = nodeId + "_" + "signUsers";
			execution.setVariable(varName, userIds);
		}
	}
	
	/**
	 * 设置节点处理用户，用于处理用户是从界面上指定的场景
	 * @param users
	 */
	public void setExecutors(String users) {
		if (StringUtils.isEmpty(users)) {
			return;
		}
		String[] aryUsers = users.split(",");
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		for (String userId : aryUsers) {
			TaskExecutor executor = new TaskExecutor(userId);
			list.add(executor);
		}
		taskExecutors.set(list);
	}
	
	/**
	 * 设置节点处理用户，用于处理用户是从界面上指定的场景
	 * @param users
	 */
	public void setExecutors(List<TaskExecutor> users) {
		taskExecutors.set(users);
	}

	public List<TaskExecutor> getExecutors() {
		return taskExecutors.get();
	}

	public void clearExecutors() {
		taskExecutors.remove();
	}

	public static void clearAll() {
		taskExecutors.remove();
		nodeUserMapLocal.remove();
	}
}
