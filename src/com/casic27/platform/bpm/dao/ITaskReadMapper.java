/**
 * @(#)com.casic27.platform.bpm.dao.ITaskReadMapper
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
 
package com.casic27.platform.bpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.TaskRead;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskReadMapper{
	/**
	 * 根据Id进行查询
	 */
	TaskRead getTaskReadById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addTaskRead(TaskRead taskRead);
	
	/**
	 * 修改
	 */
	void updateTaskRead(TaskRead taskRead);
	
	/**
	 * 物理删除
	 */
	void delById(@Param("id")String id);
	
	/**
	 * 是否已读
	 * @param taskId
	 * @param userId
	 * @return
	 */
	int isTaskRead(@Param("taskId")String taskId, @Param("userId")String userId);
	
	/**
	 * 根据流程实例删除
	 * @param actInstId
	 */
	void delByActInstId(@Param("actInstId")String actInstId);
	
	/**
	 * 根据任务ID删除
	 * @param taskId
	 */
	void delByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	
	List<TaskRead> getTaskRead(@Param("actInstId")String actInstId, @Param("taskId")String taskId);
}