package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmNodeScript;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodeScriptMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmNodeScript> findNodeScript(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodeScript getNodeScriptById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addNodeScript(BpmNodeScript nodeScript);
	
	/**
	 * 修改
	 */
	void updateNodeScript(BpmNodeScript nodeScript);
	
	/**
	 * 删除
	 */
	void deleteNodeScript(@Param("id")String id);
	
	/**
	 * 根据节点ID删除
	 * @param nodeId
	 * @param defId
	 */
	void deleteByNodeId(@Param("nodeId")String nodeId, @Param("defId")String defId);
	
	void delByActDefId(@Param("actDefId")String actDefId);
}