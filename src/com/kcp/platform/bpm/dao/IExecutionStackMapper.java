package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.ExecutionStack;
import org.springframework.stereotype.Repository;

@Repository
public interface IExecutionStackMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<ExecutionStack> findExecutionStack(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	ExecutionStack getExecutionStackById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addExecutionStack(ExecutionStack executionStack);
	
	/**
	 * 修改
	 */
	void updateExecutionStack(ExecutionStack executionStack);
	
	/**
	 * 物理删除
	 */
	void deleteExecutionStack(@Param("id")String id);
	
	/**
	 * 根据父ID查询子执行堆栈
	 * @param parentId
	 * @return
	 */
	List<ExecutionStack> getByParentId(@Param("parentId")String parentId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据流程实例ID删除
	 * @param actDefId
	 */
	void delByActInstId(@Param("actInstId")String actInstId);
}