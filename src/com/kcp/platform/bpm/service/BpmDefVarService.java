package com.kcp.platform.bpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IBpmDefVarMapper;
import com.kcp.platform.bpm.entity.BpmDefVar;
import com.kcp.platform.core.service.BaseService;

@Service("bpmDefVarService")
public class BpmDefVarService extends BaseService {
	@Autowired
	private IBpmDefVarMapper bpmDefVarMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmDefVar> findBpmDefVar(Map<String,Object> queryMap){
		return bpmDefVarMapper.findBpmDefVar(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmDefVar getBpmDefVarById(String id){
		return bpmDefVarMapper.getBpmDefVarById(id);
	}
	/**
	 * 新增
	 */
	public void addBpmDefVar(BpmDefVar bpmDefVar){
		bpmDefVar.setVarId(CodeGenerator.getUUID32());
		bpmDefVarMapper.addBpmDefVar(bpmDefVar);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmDefVar(BpmDefVar bpmDefVar){
		bpmDefVarMapper.updateBpmDefVar(bpmDefVar);
	}
	
	/**
	 *删除
	 */
	public void deleteBpmDefVar(String id){
		bpmDefVarMapper.deleteBpmDefVar(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmDefVar(BpmDefVar bpmDefVar){
		return bpmDefVarMapper.countBpmDefVar(bpmDefVar);
	}
}