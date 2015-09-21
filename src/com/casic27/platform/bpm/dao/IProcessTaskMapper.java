package com.casic27.platform.bpm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.core.mybatis.annotation.Pageable;

@Repository
public interface IProcessTaskMapper {
	/**
	 * 添加
	 * @param processTask
	 */
	void addTask(ProcessTask processTask);
	
	/**
	 * 修改execution_id_
	 * @param id
	 */
	void updateTaskExecution(@Param("taskId")String taskId);
	
	/**
	 * 设置到期时间
	 * @param taskId
	 * @param dueDate
	 * @return
	 */
	int setDueDate(@Param("taskId")String taskId, @Param("dueDate")Date dueDate);
	
	/**
	 * 根据流程实例RUNID获取任务列表
	 * @param runId
	 * @return
	 */
	List<ProcessTask> getTasksByRunId(@Param("runId")String runId);
	
	/**
	 * 
	 * @param userId
	 * @param taskName
	 * @param taskId
	 * @return
	 */
	@Pageable
	List<ProcessTask> getByTaskNameOrTaskIds(String userId, String taskName, String[] taskId);
	
	/**
	 * 修改任务执行人
	 * @param taskId
	 * @param userId
	 */
	void updateTaskAssignee(@Param("taskId")String taskId, @Param("userId")String userId);
	
	/**
	 * 修改任务description
	 * @param description
	 * @param taskId
	 */
	void updateTaskDescription(@Param("description")String description, @Param("taskId")String taskId);
	
	/**
	 * 修改任务执行为null
	 * @param taskId
	 * @param userId
	 */
	void updateTaskAssigneeNull(@Param("taskId")String taskId);
	
	/**
	 * 
	 * @param newTaskDefKey
	 * @param oldTaskDefKey
	 * @param actInstId
	 */
	void updateNewTaskDefKeyByInstIdNodeId(@Param("newTaskDefKey")String newTaskDefKey, @Param("oldTaskDefKey")String oldTaskDefKey, @Param("actInstId")String actInstId);
	
	/**
	 * 
	 * @param newTaskDefKey
	 * @param oldTaskDefKey
	 * @param actInstId
	 */
	void updateOldTaskDefKeyByInstIdNodeId(@Param("newTaskDefKey")String newTaskDefKey, @Param("oldTaskDefKey")String oldTaskDefKey, @Param("actInstId")String actInstId);
	
	/**
	 * 修改owner
	 * @param taskId
	 * @param userId
	 */
	void updateTaskOwner(@Param("taskId")String taskId, @Param("userId")String userId);
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	ProcessTask getByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 根据流程实例ACT_INST_ID获取任务列表
	 * @param instanceId
	 * @return
	 */
	List<ProcessTask> getByInstanceId(@Param("instanceId")String instanceId);
	
	/**
	 * 删除流程实例下的任务列表
	 * @param instanceId
	 */
	void delByInstanceId(@Param("instanceId")String instanceId);
	
	/**
	 * 
	 * @param instanceId
	 */
	void delCandidateByInstanceId(@Param("instanceId")String instanceId);
	
	/**
	 * 根据流程定义ID删除
	 * @param instanceId
	 */
	void delCandidateByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 查找是否有获选执行者
	 * @param taskIds
	 * @return
	 */
	List<Map<String,Object>> getHasCandidateExecutor(String[] taskIds);
	
	/**
	 * 修改
	 * @param taskId
	 * @param userId
	 * @param description
	 */
	void updateTask(@Param("taskId")String taskId, @Param("userId")String userId, @Param("description")String description);
	
	/**
	 * 删除指定人某个节点的沟通任务
	 * @param instInstId
	 * @param nodeId
	 * @param userId
	 */
	void delCommuTaskByInstNodeUser(@Param("proInstId")String proInstId, @Param("nodeId")String nodeId, @Param("userId")String userId);
	
	/**
	 * 删除某个任务的沟通任务
	 * @param parentTaskId
	 */
	void delCommuTaskByParentTaskId(@Param("parentTaskId")String parentTaskId);
	
	/**
	 * 删除指定parentTaskId的流转任务 
	 * @param parentTaskId
	 */
	void delTransToTaskByParentTaskId(@Param("parentTaskId")String parentTaskId);
	
	/**
	 * 根据流程实例ID和任务Id查询任务
	 * @param instanceId
	 * @param taskDefKey
	 * @return
	 */
	List<TaskEntity> getByInstanceIdTaskDefKey(@Param("instanceId")String instanceId, @Param("taskDefKey")String taskDefKey);
	
	/**
	 * 
	 * @param parentTaskId
	 * @param description
	 * @return
	 */
	List<TaskEntity> getByParentTaskIdAndDesc(@Param("parentTaskId")String parentTaskId, @Param("description")String description);
	
	/**
	 * 根据流程实例ACT_INST_ID获取任务列表
	 * @param instanceId
	 * @return
	 */
	List<ProcessTask> getTaskByActDefId(@Param("instanceId")String instanceId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 查询任务
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessTask> getAllTask(Map<String,Object> queryMap);
	
	@Pageable
	List<ProcessTask> getMyTask(Map<String,Object> queryMap);
}
