package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmFormField;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmFormFieldMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmFormField> findBpmFormField(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmFormField getBpmFormFieldById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmFormField(BpmFormField bpmFormField);
	
	/**
	 * 修改
	 */
	void updateBpmFormField(BpmFormField bpmFormField);
	
	/**
	 * 物理删除
	 */
	void deleteBpmFormField(@Param("id")String id);
	
	void deleteBpmFormFieldByTableId(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmFormField(BpmFormField bpmFormField);
	
}