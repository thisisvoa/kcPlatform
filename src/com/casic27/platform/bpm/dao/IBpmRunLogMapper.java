/**
 * @(#)com.casic27.platform.bpm.dao.IBpmRunLogMapper
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
 
package com.casic27.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.BpmRunLog;
import com.casic27.platform.core.mybatis.annotation.Pageable;
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