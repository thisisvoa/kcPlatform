/**
 * @(#)com.casic27.platform.common.log.dao.IInterfaceLogMapper
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
 
package com.casic27.platform.common.log.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.common.log.entity.InterfaceLog;
import com.casic27.platform.core.mybatis.annotation.Pageable;
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