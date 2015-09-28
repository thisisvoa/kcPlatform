package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmAgentDef;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmAgentDefMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmAgentDef> findBpmAgentDef(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmAgentDef getBpmAgentDefById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmAgentDef(BpmAgentDef bpmAgentDef);
	
	/**
	 * 修改
	 */
	void updateBpmAgentDef(BpmAgentDef bpmAgentDef);
	
	/**
	 * 物理删除
	 */
	void deleteById(@Param("id")String id);
	
	/**
	 * 
	 * @param id
	 */
	void deleteBySettingId(@Param("settingId")String settingId);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmAgentDef(BpmAgentDef bpmAgentDef);
	
}