package com.kcp.platform.bpm.dao;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoricProcessInstanceMapper {
	/**
	 * 修改
	 * @param entity
	 */
	void update(HistoricProcessInstanceEntity entity);
	
	/**
	 * 根据流程实例ID和节点ID查询
	 * @param actInstanceId
	 * @param nodeId
	 * @return
	 */
	HistoricProcessInstanceEntity getByInstanceIdAndNodeId(@Param("actInstanceId")String actInstanceId, @Param("nodeId")String nodeId);
}
