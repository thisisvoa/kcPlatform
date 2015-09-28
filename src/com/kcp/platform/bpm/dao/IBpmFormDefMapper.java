package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmFormDef;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmFormDefMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmFormDef> findBpmFormDef(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmFormDef> findBpmFormDefPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmFormDef getBpmFormDefById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmFormDef(BpmFormDef bpmFormDef);
	
	/**
	 * 修改
	 */
	void updateBpmFormDef(BpmFormDef bpmFormDef);
	
	/**
	 * 物理删除
	 */
	void deleteBpmFormDef(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmFormDef(BpmFormDef bpmFormDef);
	
}