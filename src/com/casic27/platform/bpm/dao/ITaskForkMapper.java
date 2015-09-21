/**
 * @(#)com.casic27.platform.bpm.dao.ITaskForkMapper
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

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.TaskFork;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskForkMapper{
	/**
	 * 
	 * @param actInstId
	 * @return
	 */
	Integer getMaxSn(@Param("actInstId")String actInstId);
	
	/**
	 * 
	 * @param actInstId
	 * @param joinTaskKey
	 * @return
	 */
	TaskFork getByInstIdJoinTaskKey(@Param("actInstId")String actInstId, @Param("joinTaskKey")String joinTaskKey);
	
	/**
	 * 
	 * @param actInstId
	 * @param joinTaskKey
	 * @param forkToken
	 * @return
	 */
	TaskFork getByInstIdJoinTaskKeyForkToken(@Param("actInstId")String actInstId, @Param("joinTaskKey")String joinTaskKey, @Param("forkToken")String forkToken);
	
	/**
	 * 删除流程实例的分发
	 * @param actInstId
	 */
	void delByActInstId(@Param("actInstId")String actInstId);
	
	
	/**
	 * 添加分发
	 * @param taskFork
	 */
	void addTaskFork(TaskFork taskFork);
	
	/**
	 * 修改
	 * @param taskFork
	 */
	void updateTaskFork(TaskFork taskFork);
	
	/**
	 * 根据ID删除
	 * @param id
	 */
	void deleteById(@Param("id")String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	TaskFork getTaskForkById(@Param("id")String id);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
}