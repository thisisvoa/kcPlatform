package com.casic27.platform.bpm.dao;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IExecutionExtMapper {
	/**
	 * 添加
	 * @param execution
	 */
	void add(ExecutionEntity execution);
	
	/**
	 * 修改
	 * @param execution
	 */
	void update(ExecutionEntity execution);
	
	/**
	 * 删除
	 * @param id
	 */
	void delById(@Param("id")String id);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	ExecutionEntity getById(@Param("id")String id);
}
