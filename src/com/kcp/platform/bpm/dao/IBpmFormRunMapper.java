package com.kcp.platform.bpm.dao;


import com.kcp.platform.bpm.entity.BpmFormRun;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmFormRunMapper{
	
	/**
	 * 新增
	 */
	void addBpmFormRun(BpmFormRun bpmFormRun);
	
	/**
	 * 根据流程实例和节点ID查询运行表单
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	BpmFormRun getByInstanceAndNode(@Param("actInstanceId")String actInstanceId, @Param("actNodeId")String actNodeId);
	
	/**
	 * 获取流程实例全局表单
	 * @param actInstanceId
	 * @return
	 */
	BpmFormRun getGlobalForm(@Param("actInstanceId")String actInstanceId);
	
	/**
	 * 根据流程实例ID删除流程运行表单
	 * @param actInstanceId
	 */
	void delByInstanceId(@Param("actInstanceId")String actInstanceId);
	
	/**
	 * 根据流程定义ID删除流程运行表单
	 * @param actInstanceId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
}