package com.kcp.platform.bpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IBpmFormDefMapper;
import com.kcp.platform.bpm.entity.BpmFormDef;
import com.kcp.platform.core.service.BaseService;

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