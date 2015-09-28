package com.kcp.platform.bpm.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmProStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmProStatusMapper{
	
	/**
	 * 根据流程实例Id和节点ID进行查询
	 */
	BpmProStatus getByInstNodeId(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	/**
	 * 根据流程实例查询
	 * @param instanceId
	 * @return
	 */
	List<BpmProStatus> getByActInstanceId(@Param("actInstId")String actInstId);
	
	/**
	 * 新增
	 */
	void addBpmProStatus(BpmProStatus bpmProStatus);
	
	/**
	 * 修改
	 */
	void updateBpmProStatus(BpmProStatus bpmProStatus);
	
	/**
	 * 修改状态
	 * @param bpmProStatus
	 */
	void updStatus(BpmProStatus bpmProStatus);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据流程实例ID删除
	 * @param actInstId
	 */
	void delByActInstId(@Param("actInstId")String actInstId);
	
}