/**
 * @(#)com.casic27.platform.bpm.service.BpmFormDefService
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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmFormDefMapper;
import com.casic27.platform.bpm.entity.BpmFormDef;
import com.casic27.platform.core.service.BaseService;

@Service("bpmFormDefService")
public class BpmFormDefService extends BaseService {
	@Autowired
	private IBpmFormDefMapper bpmFormDefMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmFormDef> findBpmFormDef(Map<String,Object> queryMap){
		return bpmFormDefMapper.findBpmFormDef(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<BpmFormDef> findBpmFormDefPaging(Map<String,Object> queryMap){
		return bpmFormDefMapper.findBpmFormDefPaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmFormDef getBpmFormDefById(String id){
		return bpmFormDefMapper.getBpmFormDefById(id);
	}
	/**
	 * 新增
	 */
	public void addBpmFormDef(BpmFormDef bpmFormDef){
		bpmFormDef.setFormDefId(CodeGenerator.getUUID32());
		bpmFormDefMapper.addBpmFormDef(bpmFormDef);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmFormDef(BpmFormDef bpmFormDef){
		bpmFormDefMapper.updateBpmFormDef(bpmFormDef);
	}
	
	/**
	 *删除
	 */
	public void deleteBpmFormDef(String id){
		bpmFormDefMapper.deleteBpmFormDef(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmFormDef(BpmFormDef bpmFormDef){
		return bpmFormDefMapper.countBpmFormDef(bpmFormDef);
	}
}