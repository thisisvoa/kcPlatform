package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmDefVar;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmDefVarMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmDefVar> findBpmDefVar(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmDefVar getBpmDefVarById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmDefVar(BpmDefVar bpmDefVar);
	
	/**
	 * 修改
	 */
	void updateBpmDefVar(BpmDefVar bpmDefVar);
	
	/**
	 * 物理删除
	 */
	void deleteBpmDefVar(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmDefVar(BpmDefVar bpmDefVar);
	/**
	 * 根据流程定义ID删除
	 * @param defId
	 */
	void delByDefId(@Param("defId")String defId);
	
}