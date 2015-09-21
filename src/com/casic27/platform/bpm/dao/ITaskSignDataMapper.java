/**
 * @(#)com.casic27.platform.bpm.dao.ITaskSignDataMapper
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
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.TaskSignData;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskSignDataMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<TaskSignData> findTaskSignData(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	TaskSignData getTaskSignDataById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addTaskSignData(TaskSignData taskSignData);
	
	/**
	 * 修改
	 */
	void updateTaskSignData(TaskSignData taskSignData);
	
	/**
	 * 物理删除
	 */
	void delById(@Param("id")String id);
	
	/**
	 * 根据任务ID查询
	 * @param taskId
	 * @return
	 */
	TaskSignData getByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 最大会签数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	Integer getMaxSignNums(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId, @Param("isCompleted")Short isCompleted);
	
	/**
	 * 同意的会签数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	Integer getAgreeVoteCount(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	/**
	 * 拒绝会签数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	Integer getRefuseVoteCount(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	/**
	 * 弃权会签数
	 * @param actInstId
	 * @param nodeId
	 * @return
	 */
	Integer getAbortVoteCount(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	/**
	 * 修改为已完成
	 * @param actInstId
	 * @param nodeId
	 */
	void updateCompleted(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	/**
	 * 获取用户会签数据
	 * @param actInstId
	 * @param nodeId
	 * @param voteUserId
	 * @return
	 */
	TaskSignData getUserTaskSign(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId, @Param("voteUserId")String voteUserId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByIdActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据流程实例ID删除
	 * @param actInstId
	 */
	void delByIdActInstId(@Param("actInstId")String actInstId);
	
	/**
	 * 根据流程实例ID和节点ID获取会签数据
	 * @param params
	 * @return
	 */
	List<TaskSignData> getByNodeAndInstanceId(Map<String,Object> params);
	
	/**
	 * 
	 * @param instanceId
	 * @param nodeId
	 * @return
	 */
	Integer getMaxBatch(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	
}