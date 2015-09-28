package com.kcp.platform.bpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.TaskOpinion;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskOpinionMapper{
	/**
	 * 根据Id进行查询
	 */
	TaskOpinion getTaskOpinionById(@Param("id")String id);
	
	/**
	 * 根据taskId进行查询
	 */
	TaskOpinion getTaskOpinionByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 新增
	 */
	void addTaskOpinion(TaskOpinion bpmTaskOpinion);
	
	/**
	 * 修改
	 */
	void updateTaskOpinion(TaskOpinion bpmTaskOpinion);
	
	/**
	 * 物理删除
	 */
	void deleteTaskOpinion(@Param("id")String id);
	
	/**
	 * 根据流程实例ID和任务Key查询审批意见
	 * @param actInstId
	 * @param taskKey
	 * @return
	 */
	List<TaskOpinion> getByActInstIdTaskKey(@Param("actInstId")String actInstId, @Param("taskKey")String taskKey);
	
	/**
	 * 查询流程实例的所有审批意见
	 * @param actInstIds
	 * @param isAsc
	 * @return
	 */
	List<TaskOpinion> getByActInstId(@Param("actInstIds")List<String> actInstIds, @Param("isAsc")boolean isAsc);
	
	/**
	 * 根据任务ID和用户ID查询
	 * @param taskId
	 * @param userId
	 * @return
	 */
	TaskOpinion getOpinionByTaskId(@Param("taskId")String taskId, @Param("userId")String userId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * actInstId根据流程实例删除
	 * @param actInstId
	 */
	void delByActInstId(@Param("actInstId")String actInstId);
	
	/**
	 * 查询审批中的人物
	 * @param actInstId
	 * @return
	 */
	List<TaskOpinion> getCheckOpinionByInstId(@Param("actInstId")String actInstId);
	
	/**
	 * 获取流程实例中用户参与的审批意见
	 * @param actInstId
	 * @param exeUserId
	 * @return
	 */
	List<TaskOpinion> getByActInstIdExeUserId(@Param("actInstId")String actInstId, @Param("exeUserId")String exeUserId);
	
	/**
	 * 
	 * @param actInstId
	 * @param taskKey
	 * @param checkStatus
	 * @return
	 */
	List<TaskOpinion> getByActInstIdTaskKeyStatus(@Param("actInstId")String actInstId, @Param("taskKey")String taskKey, @Param("checkStatus")Short checkStatus);
	
	/**
	 * 获取节点处理审批中的审批意见
	 * @param taskId
	 * @return
	 */
	TaskOpinion getByTaskId(@Param("taskId")String taskId);
}