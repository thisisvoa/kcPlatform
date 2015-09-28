package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmNodeSign;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodeSignMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmNodeSign> findNodeSign(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodeSign getNodeSignById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addNodeSign(BpmNodeSign nodeSign);
	
	/**
	 * 修改
	 */
	void updateNodeSign(BpmNodeSign nodeSign);
	
	/**
	 * 物理删除
	 */
	void deleteNodeSign(@Param("id")String id);
	
	/**
	 * 根据流程定义ID和节点ID查询会签设置
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	BpmNodeSign getByDefIdAndNodeId(@Param("actDefId")String actDefId, @Param("nodeId")String nodeId);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	
}