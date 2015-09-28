package com.kcp.platform.bpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.bpm.entity.TaskUser;

@Repository
public interface ITaskUserMapper {
	/**
	 * 添加
	 * @param taskUser
	 */
	void addTaskUser(TaskUser taskUser);
	
	/**
	 * 根据TaskId查询
	 * @param taskId
	 * @return
	 */
	List<TaskUser> getByTaskId(String taskId);
	
	/**
	 * 删除流程实例的任务节点用户
	 * @param instanceId
	 */
	void delByInstanceId(@Param("instanceId")String instanceId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
}
