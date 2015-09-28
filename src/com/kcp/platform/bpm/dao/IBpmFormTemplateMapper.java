package com.kcp.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.bpm.entity.BpmFormTemplate;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmFormTemplateMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmFormTemplate> findBpmFormTemplate(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmFormTemplate> findBpmFormTemplatePaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmFormTemplate getBpmFormTemplateById(@Param("id")String id);
	
	/**
	 * 根据别名获取模板
	 * @param alias
	 * @return
	 */
	BpmFormTemplate getBpmFormTemplateByAlias(@Param("alias")String alias);
	/**
	 * 新增
	 */
	void addBpmFormTemplate(BpmFormTemplate bpmFormTemplate);
	
	/**
	 * 修改
	 */
	void updateBpmFormTemplate(BpmFormTemplate bpmFormTemplate);
	
	/**
	 * 物理删除
	 */
	void deleteBpmFormTemplate(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmFormTemplate(BpmFormTemplate bpmFormTemplate);
	
}