package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmTaskExe;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmTaskExeMapper{
	/**
	 * 根据Id进行查询
	 */
	BpmTaskExe getBpmTaskExeById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmTaskExe(BpmTaskExe bpmTaskExe);
	
	/**
	 * 修改
	 */
	void updateBpmTaskExe(BpmTaskExe bpmTaskExe);
	
	/**
	 * 根据任务ID和状态获取代理
	 * @param taskId
	 * @param status
	 * @return
	 */
	BpmTaskExe getByTaskIdStatus(@Param("taskId")String taskId, @Param("status")int status);
	
	/**
	 * 查询任务的代理列表
	 * @param taskId
	 * @return
	 */
	List<BpmTaskExe> getByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 我的代理事宜
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmTaskExe> accordingMattersList(Map<String,Object> queryMap);
	
	/**
	 *  查找任务是否分配
	 * @param taskId
	 * @return
	 */
	Integer getByIsAssign(@Param("taskId")String taskId);
	
	/**
	 * 删除流程的任务代理
	 * @param runId
	 * @return
	 */
	int delByRunId(@Param("runId")String runId);
	
	/**
	 * 获取流程实例的代理任务
	 * @param runId
	 * @return
	 */
	List<BpmTaskExe> getByRunId(@Param("runId")String runId);
	
	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	void delById(@Param("id")String id);
}