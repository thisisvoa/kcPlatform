/**
 * @(#)com.casic27.platform.bpm.dao.INodeParticipantMapper
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

import com.casic27.platform.bpm.entity.BpmNodeParticipant;

import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodeParticipantMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmNodeParticipant> findNodeParticipant(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodeParticipant getNodeParticipantById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addNodeParticipant(BpmNodeParticipant nodeParticipant);
	
	/**
	 * 修改
	 */
	void updateNodeParticipant(BpmNodeParticipant nodeParticipant);
	
	/**
	 * 物理删除
	 */
	void deleteNodeParticipant(@Param("id")String id);
	
	/**
	 * 获取最大排序号
	 * @param configId
	 * @return
	 */
	Integer getMaxSn(@Param("configId")String configId);
	
	/**
	 * 修改排序号
	 * @param nodeParticipant
	 */
	void updateSn(BpmNodeParticipant nodeParticipant);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
}