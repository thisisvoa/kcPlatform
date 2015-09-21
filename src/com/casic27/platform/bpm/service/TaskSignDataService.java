/**
 * @(#)com.casic27.platform.bpm.service.TaskSignDataService
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.ITaskSignDataMapper;
import com.casic27.platform.bpm.entity.BpmRunLog;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.entity.TaskSignData;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;

@Service
public class TaskSignDataService {
	@Autowired
	private ITaskSignDataMapper taskSignDataMapper;

	@Resource
	private BpmService bpmService;

	@Resource
	private UserService userService;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private ProcessRunService processRunService;

	@Resource
	private BpmRunLogService bpmRunLogService;

	public Integer getMaxSignNums(String actInstId, String nodeId, Short isCompleted) {
		return this.taskSignDataMapper.getMaxSignNums(actInstId, nodeId, isCompleted);
	}
	
	/**
	 * 会签投票
	 * @param taskId
	 * @param content
	 * @param isAgree
	 */
	public void signVoteTask(String taskId, String content, Short isAgree) {
		User sysUser = SecurityContext.getCurrentUser();
		String userId = "";
		String fullname = "系统";
		if (BeanUtils.isNotEmpty(sysUser)) {
			userId = sysUser.getZjid();
			fullname = sysUser.getYhmc();
		}
		TaskSignData taskSignData = this.taskSignDataMapper.getByTaskId(taskId);
		if (taskSignData != null) {
			taskSignData.setIsAgree(new Short(isAgree.shortValue()));
			taskSignData.setContent(content);
			taskSignData.setVoteTime(new Date());
			taskSignData.setVoteUserId(userId);
			taskSignData.setVoteUserName(fullname);
			taskSignDataMapper.updateTaskSignData(taskSignData);
		}
	}
	
	/**
	 * 同意票数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getAgreeVoteCount(String actInstId, String nodeId) {
		return this.taskSignDataMapper.getAgreeVoteCount(actInstId, nodeId);
	}
	
	/**
	 * 拒绝票数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getRefuseVoteCount(String actInstId, String nodeId) {
		return this.taskSignDataMapper.getRefuseVoteCount(actInstId, nodeId);
	}
	
	/**
	 * 弃权票数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public Integer getAbortVoteCount(String actInstId, String nodeId) {
		return this.taskSignDataMapper.getAbortVoteCount(actInstId, nodeId);
	}
	
	/**
	 * 更新是否完成
	 * @param actInstId
	 * @param nodeId
	 */
	public void batchUpdateCompleted(String actInstId, String nodeId) {
		this.taskSignDataMapper.updateCompleted(actInstId, nodeId);
	}

	/**
	 * 补签
	 * @param userIds
	 * @param taskId
	 */
	public void addSign(String userIds, String taskId) {
		if (StringUtils.isEmpty(userIds)) return;
		TaskEntity taskEntity = this.bpmService.getTask(taskId);
		ExecutionEntity executionEntity = this.bpmService.getExecution(taskEntity.getExecutionId());
		String actDefId =  executionEntity.getProcessDefinitionId();
		String processInstanceId = executionEntity.getProcessInstanceId();
		ProcessDefinitionEntity proDefEntity = this.bpmService.getProcessDefinitionByProcessInanceId(processInstanceId);
		ActivityImpl actImpl = proDefEntity.findActivity(executionEntity.getActivityId());
		String multiInstance = (String) actImpl.getProperty("multiInstance");
		if (multiInstance == null)
			return;
		Integer maxSignNums = this.taskSignDataMapper.getMaxSignNums(processInstanceId, executionEntity.getActivityId(), TaskSignData.NOT_COMPLETED);
		Integer signNums = Integer.valueOf(maxSignNums.intValue() == 0 ? 1 : maxSignNums.intValue());
		
		List<String> uidlist = new ArrayList<String>();
		List<TaskSignData> existTaskSignDatas = getByNodeAndInstanceId(processInstanceId, taskEntity.getTaskDefinitionKey(), Integer.valueOf(0));
		Integer curBatch = Integer.valueOf(1);
		for (TaskSignData taskSignData : existTaskSignDatas) {
			curBatch = Integer.valueOf(taskSignData.getBatch());
			uidlist.add(taskSignData.getVoteUserId());
		}
		String[] uIds = userIds.split("[,]");
		List<String> addUsers = getCanAddUsers(uidlist, uIds);//过滤掉已经会签过的用户
		int userAmount = addUsers.size();
		Integer nrOfInstances = (Integer) this.runtimeService.getVariable(executionEntity.getId(), "nrOfInstances");
		if (nrOfInstances != null) {
			this.runtimeService.setVariable(executionEntity.getId(), "nrOfInstances", Integer.valueOf(nrOfInstances.intValue() + userAmount));
		}
		//串行处理
		if ("sequential".equals(multiInstance)) {
			String nodeId = executionEntity.getActivityId();
			String varName = nodeId + "_" + "signUsers";
			String exeId = executionEntity.getId();
			List<TaskExecutor> addList = new ArrayList<TaskExecutor>();
			for (int i = 0; i < userAmount; i++) {
				String userId = addUsers.get(i);
				int sn = signNums.intValue() + 1;
				addSignData("", nodeId, taskEntity.getName(), actDefId, processInstanceId, Integer.valueOf(sn), userId, curBatch);
				addList.add(TaskExecutor.getTaskUser(userId.toString(), ""));
			}
			List<TaskExecutor> list = (List<TaskExecutor>) this.runtimeService.getVariable(exeId, varName);//需要会签的用户保存在流程变量里面
			list.addAll(addList);
			this.runtimeService.setVariable(executionEntity.getId(), varName, list);
		} else {//并行处理
			Integer loopCounter = (Integer) this.runtimeService.getVariable(executionEntity.getId(), "loopCounter");
			Integer nrOfActiveInstances = (Integer) this.runtimeService.getVariable(executionEntity.getId(), "nrOfActiveInstances");
			this.runtimeService.setVariable(executionEntity.getId(), "nrOfActiveInstances", Integer.valueOf(nrOfActiveInstances.intValue() + userAmount));
			for (int i = 0; i < userAmount; i++) {
				String userId = addUsers.get(i);
				ProcessTask newProcessTask = this.bpmService.newTask(taskId, (String) addUsers.get(i));
				String executionId = newProcessTask.getExecutionId();
				this.runtimeService.setVariableLocal(executionId, "loopCounter", Integer.valueOf(loopCounter.intValue() + i + 1));
				this.runtimeService.setVariableLocal(executionId, "assignee", TaskExecutor.getTaskUser(userId.toString(), ""));
				int sn = signNums.intValue() + 1;
				addSignData(newProcessTask.getId(), executionEntity.getActivityId(), taskEntity.getName(), actDefId,
											processInstanceId, Integer.valueOf(sn), userId, curBatch);
			}
		}
		ProcessRun processRun = this.processRunService.getProcessRunByActInstId(processInstanceId);
		String memo = "用户在任务[" + taskEntity.getName() + "]执行了补签操作。";
		this.bpmRunLogService.addBpmRunLog(processRun.getRunId(), BpmRunLog.OPERATOR_TYPE_SIGN, memo);
	}
	/**
	 * 添加会签数据
	 * @param taskId
	 * @param nodeId
	 * @param nodeName
	 * @param instanceId
	 * @param signNums
	 * @param userId
	 * @param batch
	 */
	private void addSignData(String taskId, String nodeId, String nodeName, String actDefId, String instanceId, Integer signNums, String userId, Integer batch) {
		String dataId = CodeGenerator.getUUID32();
		TaskSignData newSignData = new TaskSignData();
		newSignData.setTaskId(taskId);
		newSignData.setDataId(dataId);
		newSignData.setNodeId(nodeId);
		newSignData.setNodeName(nodeName);
		newSignData.setActDefId(actDefId);
		newSignData.setActInstId(instanceId);
		newSignData.setSignNums(signNums);
		newSignData.setIsCompleted(TaskSignData.NOT_COMPLETED);
		newSignData.setIsAgree(null);
		newSignData.setBatch(batch.intValue());
		newSignData.setContent(null);
		newSignData.setVoteTime(null);
		newSignData.setVoteUserId(userId);
		User sysUser = this.userService.getUserById(userId);
		newSignData.setVoteUserName(sysUser.getYhmc());
		this.taskSignDataMapper.addTaskSignData(newSignData);
	}
	
	public void addSignData(TaskSignData newSignData){
		this.taskSignDataMapper.addTaskSignData(newSignData);
	}
	public TaskSignData getUserTaskSign(String actInstId, String nodeId, String executorId) {
		return this.taskSignDataMapper.getUserTaskSign(actInstId, nodeId, executorId);
	}

	public int getMaxBatch(String instanceId, String nodeId) {
		Integer result = this.taskSignDataMapper.getMaxBatch(instanceId, nodeId);
		if(null==result){
			return 0;
		}
		return result;
	}

	public TaskSignData getByTaskId(String taskId) {
		return this.taskSignDataMapper.getByTaskId(taskId);
	}
	
	/**
	 * 根据实例ID和节点ID查询会签数据
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	public List<TaskSignData> getByNodeAndInstanceId(String actInstId, String nodeId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("actInstId", actInstId);
		params.put("nodeId", nodeId);
		return taskSignDataMapper.getByNodeAndInstanceId(params);
	}
	
	/**
	 * 根据实例ID和节点ID查询是否已经完成的会签数据
	 * @param actInstId
	 * @param nodeId
	 * @param isComplete
	 * @return
	 */
	public List<TaskSignData> getByNodeAndInstanceId(String actInstId, String nodeId, Integer isComplete){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("actInstId", actInstId);
		params.put("nodeId", nodeId);
		params.put("isComplete", isComplete);
		return taskSignDataMapper.getByNodeAndInstanceId(params);
	}
	
	/**
	 * 获取能够添加的用户，需过滤掉目前已经在会签的用户列表
	 * @param curUserList
	 * @param addUsers
	 * @return
	 */
	private List<String> getCanAddUsers(List<String> curUserList, String[] addUsers) {
		List<String> users = new ArrayList<String>();
		for (String userId : addUsers) {
			if (!curUserList.contains(userId)) {
				users.add(userId);
			}
		}
		return users;
	}
	/**
	 * 修改会签数据
	 * @param taskSignData
	 */
	public void updateTaskSignData(TaskSignData taskSignData){
		taskSignDataMapper.updateTaskSignData(taskSignData);
	}
	
	/**
	 * 根据流程实例ID删除
	 * @param actInstId
	 */
	public void delByIdActInstId(String actInstId){
		taskSignDataMapper.delByIdActInstId(actInstId);
	}
}