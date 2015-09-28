package com.kcp.platform.common.log.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.common.log.entity.SysLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysLogMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<SysLog> findSysLog(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<SysLog> findSysLogPaging(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<SysLog> findSysLogByTablePaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	SysLog getSysLogById(@Param("id")String id);
	
	/**
	 * 根据Id进行查询
	 */
	SysLog getSysLogByTable(Map<String,Object> queryMap);
	
	/**
	 * 新增
	 */
	void addSysLog(SysLog sysLog);
	
}