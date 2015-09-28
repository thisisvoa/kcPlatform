package com.kcp.platform.common.log.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.common.log.entity.InterfaceLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IInterfaceLogMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<InterfaceLog> findInterfaceLog(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<InterfaceLog> findInterfaceLogPaging(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<InterfaceLog> findInterfaceLogByTablePaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	InterfaceLog getInterfaceLogById(@Param("id")String id);
	
	/**
	 * 根据Id进行查询
	 */
	InterfaceLog getInterfaceLogByTable(Map<String,Object> queryMap);
	
	/**
	 * 新增
	 */
	void addInterfaceLog(InterfaceLog interfaceLog);
}