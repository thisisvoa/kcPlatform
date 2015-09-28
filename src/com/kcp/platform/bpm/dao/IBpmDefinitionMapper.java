package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmDefinitionMapper{
	/**
	 * 查询列表
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmDefinition> findBpmDefinition(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmDefinition getBpmDefinitionById(@Param("id")String id);
	
	/**
	 * 根据actDefId进行查询
	 * @param actDefId
	 * @return
	 */
	BpmDefinition getBpmDefinitionByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据actDefKey进行查询
	 * @param actDefKey
	 * @return
	 */
	List<BpmDefinition> getBpmDefinitionByActDefKey(@Param("actDefKey")String actDefKey);
	
	/**
	 * 根据defKey获取最新版本的流程定义
	 * @param defKey
	 * @return
	 */
	BpmDefinition getMainBpmDefinitionByKey(@Param("defKey")String defKey);
	
	/**
	 * 根据actDefId获取流程
	 * @param actDefId
	 * @return
	 */
	BpmDefinition getByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 根据actDefKey获取流程
	 * @param actDefKey
	 * @param isMain
	 * @return
	 */
	BpmDefinition getByActDefKeyIsMain(@Param("actDefKey")String actDefKey, String isMain);
	
	/**
	 * 新增
	 */
	void addBpmDefinition(BpmDefinition bpmDefinition);
	
	/**
	 * 修改
	 */
	void updateBpmDefinition(BpmDefinition bpmDefinition);
	
	/**
	 * 物理删除
	 */
	void deleteBpmDefinition(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmDefinition(BpmDefinition bpmDefinition);
	
	/**
	 * 修改发布状态
	 * @param defId
	 * @param status
	 */
	void updateUsable(@Param("defId")String defId, @Param("usable")String usable);
	
	/**
	 * 将流程其他版本修改为非默认流程
	 * @param parentDefId
	 * @param defKey
	 */
	void updateSubVersions(@Param("parentDefId")String parentDefId, @Param("defKey")String defKey);
	
}