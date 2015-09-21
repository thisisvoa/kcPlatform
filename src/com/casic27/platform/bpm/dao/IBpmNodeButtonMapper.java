/**
 * @(#)com.casic27.platform.bpm.dao.INodeButtonMapper
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

import com.casic27.platform.bpm.entity.BpmNodeButton;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodeButtonMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmNodeButton> findNodeButton(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodeButton getNodeButtonById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addNodeButton(BpmNodeButton nodeButton);
	
	/**
	 * 修改
	 */
	void updateNodeButton(BpmNodeButton nodeButton);
	
	/**
	 * 物理删除
	 */
	void deleteNodeButton(BpmNodeButton nodeButton);
	
	/**
	 * 根据条件统计记录数
	 */
	int countNodeButton(BpmNodeButton nodeButton);
	
	/**
	 * 获取节点最大按钮排序号
	 * @param defId
	 * @param configId
	 * @param nodeType
	 * @return
	 */
	Integer getMaxSn(@Param("defId")String defId, @Param("nodeId")String nodeId, @Param("nodeType")String nodeType);
	
	
	void updateSn(BpmNodeButton nodeButton);
	
	/**
	 * 根据Act流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
}