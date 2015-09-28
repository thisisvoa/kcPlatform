package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmNodePrivilege;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmNodePrivilegeMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmNodePrivilege> getPrivilegesByDefIdAndNodeId(@Param("actDefId")String actDefId, @Param("nodeId")String nodeId);
	
	/**
	 * 根据Id进行查询
	 */
	BpmNodePrivilege getBpmNodePrivilegeById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmNodePrivilege(BpmNodePrivilege bpmNodePrivilege);
	
	/**
	 * 修改
	 */
	void updateBpmNodePrivilege(BpmNodePrivilege bpmNodePrivilege);
	
	/**
	 * 根据流程定义ID和节点ID删除
	 * @param actDefId
	 * @param nodeId
	 */
	void delByDefIdAndNodeId(@Param("actDefId")String actDefId, @Param("nodeId")String nodeId);
	
	/**
	 * 根据流程定义ID、节点ID、权限模式获取会签特殊权限
	 * @param actDefId
	 * @param nodeId
	 * @param privilegeMode
	 * @return
	 */
	List<BpmNodePrivilege> getPrivilegesByDefIdAndNodeIdAndMode(@Param("actDefId")String actDefId, @Param("nodeId")String nodeId, @Param("privilegeMode")Short privilegeMode);
	
	/**
	 * 根据流程定义ID删除
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
}