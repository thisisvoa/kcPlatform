/**
 * @(#)com.casic27.platform.bpm.dao.IProcessTypeMapper
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

import com.casic27.platform.bpm.entity.BpmCatalog;

import org.springframework.stereotype.Repository;

@Repository
public interface IBpmCatalogMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmCatalog> findBpmCatalog(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmCatalog getBpmCatalogById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmCatalog(BpmCatalog bpmCatalog);
	
	/**
	 * 修改
	 */
	void updateBpmCatalog(BpmCatalog bpmCatalog);
	
	/**
	 * 物理删除
	 */
	void deleteBpmCatalog(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmCatalog(BpmCatalog bpmCatalog);
	
	/**
	 * 获取某一层的最大层级代码
	 * @param parentId
	 * @return
	 */
	String getMaxLayerCode(@Param("parentId")String parentId);
	
	/**
	 * 取得某层的最大排序号
	 * @param parentId
	 * @return
	 */
	Integer getMaxOrderNo(@Param("parentId")String parentId);
}