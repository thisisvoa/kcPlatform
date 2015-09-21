package com.casic27.platform.bpm.dao;

import java.util.Date;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryActivityInstanceMapper {
	
	void update(HistoricActivityInstanceEntity entity);
	
	void add(HistoricActivityInstanceEntity entity);
	
	List<HistoricActivityInstanceEntity> getByInstanceId(@Param("actInstId")String actInstId, @Param("nodeId")String nodeId);
	
	List<HistoricActivityInstanceEntity> getByExecutionId(@Param("executionId")String executionId, @Param("nodeId")String nodeId);
	
	List<HistoricActivityInstanceEntity> getByFilter(@Param("actInstId")String actInstId, @Param("activityId")String activityId, @Param("endTime")Date endTime);
	
	void updateAssignee(HistoricActivityInstanceEntity entity);
	
}
