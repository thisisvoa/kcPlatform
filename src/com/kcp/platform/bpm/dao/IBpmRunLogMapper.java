package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmRunLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmRunLogMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmRunLog> findBpmRunLog(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmRunLog> findBpmRunLogPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmRunLog getBpmRunLogById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmRunLog(BpmRunLog bpmRunLog);
	
	/**
	 * 修改
	 */
	void updateBpmRunLog(BpmRunLog bpmRunLog);
	
	/**
	 * 物理删除
	 */
	void delById(@Param("id")String id);
	
}