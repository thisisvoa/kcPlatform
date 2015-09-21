/**
 * @(#)com.casic27.platform.bpm.dao.IBpmFormTableMapper
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

import com.casic27.platform.bpm.entity.BpmFormTable;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmFormTableMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmFormTable> findBpmFormTable(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmFormTable> findBpmFormTablePaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmFormTable getBpmFormTableById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmFormTable(BpmFormTable bpmFormTable);
	
	/**
	 * 修改
	 */
	void updateBpmFormTable(BpmFormTable bpmFormTable);
	
	/**
	 * 发布
	 */
	void publishBpmFormTable(BpmFormTable bpmFormTable);
	
	/**
	 * 发布子表
	 */
	void publishSubBpmFormTable(BpmFormTable bpmFormTable);
	
	/**
	 * 物理删除
	 */
	void deleteBpmFormTable(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmFormTable(BpmFormTable bpmFormTable);
	
	List<Map<String,Object>> getAllTables();
	
	List<Map<String,Object>> getTableColumns(@Param("tableName")String tableName);
	
}