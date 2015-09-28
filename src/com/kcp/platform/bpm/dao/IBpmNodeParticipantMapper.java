package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.bpm.entity.BpmNodeParticipant;

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