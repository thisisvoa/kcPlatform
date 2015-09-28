package com.kcp.platform.bpm.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.bpm.entity.ProcessExecution;

@Repository
public interface IProcessExecutionMapper {
	/**
	 * 添加
	 * @param execution
	 */
	void addExecution(ProcessExecution execution);
	
	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	ProcessExecution getExecutionById(@Param("id")String id);
	
	/**
	 * 根据executionId删除
	 * @param id
	 * @return
	 */
	void delExecutionById(@Param("executionId")String executionId);
	
	/**
	 * 根据procInstId删除
	 * @param id
	 * @return
	 */
	void delExecutionByProcInstId(@Param("procInstId")String procInstId);
	
	/**
	 * 删除子流程
	 * @param id
	 * @return
	 */
	void delSubExecutionByProcInstId(@Param("procInstId")String procInstId);
	
	/**
	 * 删除流程参数
	 * @param procInstId
	 */
	void delVariableByProcInstId(@Param("procInstId")String procInstId);
	
	/**
	 * 删除节点流程参数
	 * @param procInstId
	 */
	void delTokenVarByTaskId(@Param("taskId")String taskId, @Param("name")String name);
	
	/**
	 * 删除流程实例的参数
	 * @param procInstId
	 */
	void delVarsByExecutionId(@Param("procInstId")String procInstId);
	
	/**
	 * 删除非主线程流程
	 * @param executionId
	 */
	void delNotMainThread(@Param("executionId")String executionId);
	
	/**
	 * 修改节点的executionId
	 * @param executionId
	 * @param taskId
	 */
	void updateTaskToMainThreadId(@Param("taskId")String taskId,@Param("executionId")String executionId);
	
	/**
	 * 根据流程定义ID删除流程变量
	 * @param actDefId
	 */
	void delVariableByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据流程定义ID删除流程实例
	 * @param actDefId
	 */
	void delExecutionByActDefId(@Param("actDefId")String actDefId);
}