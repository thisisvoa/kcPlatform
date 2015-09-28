package com.kcp.platform.bpm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IBpmFormTemplateMapper;
import com.kcp.platform.bpm.entity.BpmFormTemplate;
import com.kcp.platform.core.service.BaseService;

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