package com.kcp.platform.bpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IBpmAgentDefMapper;
import com.kcp.platform.bpm.entity.BpmAgentDef;

@Service
public class BpmAgentDefService {
	@Autowired
	private IBpmAgentDefMapper bpmAgentDefMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmAgentDef> findBpmAgentDef(Map<String,Object> queryMap){
		return bpmAgentDefMapper.findBpmAgentDef(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmAgentDef getBpmAgentDefById(String id){
		return bpmAgentDefMapper.getBpmAgentDefById(id);
	}
	/**
	 * 新增
	 */
	public void addBpmAgentDef(BpmAgentDef bpmAgentDef){
		bpmAgentDef.setId(CodeGenerator.getUUID32());
		bpmAgentDefMapper.addBpmAgentDef(bpmAgentDef);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmAgentDef(BpmAgentDef bpmAgentDef){
		bpmAgentDefMapper.updateBpmAgentDef(bpmAgentDef);
	}
	
	/**
	 *删除
	 */
	public void deleteById(String id){
		bpmAgentDefMapper.deleteById(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmAgentDef(BpmAgentDef bpmAgentDef){
		return bpmAgentDefMapper.countBpmAgentDef(bpmAgentDef);
	}
}