package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.bpm.entity.BpmNodeConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodeConfigMapper{
	/**
	 * 查询NodeConfig
	 * @param queryMap
	 * @return
	 */
	List<BpmNodeConfig> findNodeConfig(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodeConfig getNodeConfigById(@Param("id")String id);
	
	/**
	 * 根据流程定义ID和节点ID查询节点配置
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	BpmNodeConfig getNodeConfig(@Param("defId")String defId, @Param("nodeId")String nodeId);
	
	/**
	 * 新增
	 */
	void addNodeConfig(BpmNodeConfig nodeConfig);
	
	/**
	 * 修改
	 */
	void updateNodeConfig(BpmNodeConfig nodeConfig);
	
	/**
	 * 物理删除
	 */
	void deleteNodeConfig(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countNodeConfig(BpmNodeConfig nodeConfig);
	
	/**
	 * 删除流程的全局表单和开始表单
	 * @param defId
	 */
	void delGlobalStartNodeConfig(@Param("defId")String defId);
	
	/**
	 * 获取在线表单
	 * @param actDefId
	 * @return
	 */
	List<BpmNodeConfig> getOnlineFormByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据节点类型获取节点设置
	 * @param defId
	 * @param setType
	 * @return
	 */
	BpmNodeConfig getBySetType(@Param("defId")String defId, @Param("setType")String setType);
	
	/**
	 * 根据defId和nodeId获取节点设置
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	BpmNodeConfig getByDefIdNodeId(@Param("defId")String defId, @Param("nodeId")String nodeId);
	
	/**
	 * 
	 * @param actDefId
	 * @param joinTaskKey
	 * @return
	 */
	BpmNodeConfig getByActDefIdJoinTaskKey(@Param("actDefId")String actDefId, @Param("joinTaskKey")String joinTaskKey);
	
	/**
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	BpmNodeConfig getByActDefIdNodeId(@Param("actDefId")String actDefId, @Param("nodeId")String nodeId);
	
	/**
	 * 根据流程定义ID删除 
	 * @param defId
	 */
	void delByDefId(@Param("defId")String defId);
	
}