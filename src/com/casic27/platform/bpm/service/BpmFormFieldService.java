/**
 * @(#)com.casic27.platform.bpm.service.BpmFormFieldService
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
import com.casic27.platform.bpm.dao.IBpmFormFieldMapper;
import com.casic27.platform.bpm.entity.BpmFormField;
import com.casic27.platform.core.service.BaseService;

@Service("bpmFormFieldService")
public class BpmFormFieldService extends BaseService {
	@Autowired
	private IBpmFormFieldMapper bpmFormFieldMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmFormField> findBpmFormField(Map<String,Object> queryMap){
		return bpmFormFieldMapper.findBpmFormField(queryMap);
	}
	
	
	public List<BpmFormField> getBpmFormFieldByTableId(String tableId){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("tableId", tableId);
		return findBpmFormField(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmFormField getBpmFormFieldById(String id){
		return bpmFormFieldMapper.getBpmFormFieldById(id);
	}
	/**
	 * 新增
	 */
	public void addBpmFormField(BpmFormField bpmFormField){
		bpmFormField.setFieldId(CodeGenerator.getUUID32());
		bpmFormFieldMapper.addBpmFormField(bpmFormField);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmFormField(BpmFormField bpmFormField){
		bpmFormFieldMapper.updateBpmFormField(bpmFormField);
	}
	
	/**
	 *删除
	 */
	public void deleteBpmFormField(String id){
		bpmFormFieldMapper.deleteBpmFormField(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmFormField(BpmFormField bpmFormField){
		return bpmFormFieldMapper.countBpmFormField(bpmFormField);
	}
}