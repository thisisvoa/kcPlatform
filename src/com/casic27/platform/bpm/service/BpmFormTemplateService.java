/**
 * @(#)com.casic27.platform.bpm.service.BpmFormTemplateService
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.bpm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmFormTemplateMapper;
import com.casic27.platform.bpm.entity.BpmFormTemplate;
import com.casic27.platform.core.service.BaseService;

@Service("bpmFormTemplateService")
public class BpmFormTemplateService extends BaseService{
	@Autowired
	private IBpmFormTemplateMapper bpmFormTemplateMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmFormTemplate> findBpmFormTemplate(Map<String,Object> queryMap){
		return bpmFormTemplateMapper.findBpmFormTemplate(queryMap);
	}
	
	public List<BpmFormTemplate> getMainTableTemplate(){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("templateType", "1");
		return findBpmFormTemplate(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<BpmFormTemplate> findBpmFormTemplatePaging(Map<String,Object> queryMap){
		return bpmFormTemplateMapper.findBpmFormTemplatePaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmFormTemplate getBpmFormTemplateById(String id){
		return bpmFormTemplateMapper.getBpmFormTemplateById(id);
	}
	
	
	public BpmFormTemplate getBpmFormTemplateByAlias(String alias){
		return bpmFormTemplateMapper.getBpmFormTemplateByAlias(alias);
	}
	
	/**
	 * 新增
	 */
	public void addBpmFormTemplate(BpmFormTemplate bpmFormTemplate){
		bpmFormTemplate.setTemplateId(CodeGenerator.getUUID32());
		bpmFormTemplateMapper.addBpmFormTemplate(bpmFormTemplate);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmFormTemplate(BpmFormTemplate bpmFormTemplate){
		bpmFormTemplateMapper.updateBpmFormTemplate(bpmFormTemplate);
	}
	
	/**
	 *删除
	 */
	public void deleteBpmFormTemplate(String id){
		bpmFormTemplateMapper.deleteBpmFormTemplate(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmFormTemplate(BpmFormTemplate bpmFormTemplate){
		return bpmFormTemplateMapper.countBpmFormTemplate(bpmFormTemplate);
	}
}