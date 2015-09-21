/**
 * @(#)com.casic27.platform.bpm.service.NodeScriptService
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
import com.casic27.platform.bpm.dao.IBpmNodeScriptMapper;
import com.casic27.platform.bpm.entity.BpmNodeScript;
import com.casic27.platform.core.service.BaseService;

@Service("bpmNodeScriptService")
public class BpmNodeScriptService extends BaseService{
	@Autowired
	private IBpmNodeScriptMapper nodeScriptMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmNodeScript> findNodeScript(Map<String,Object> queryMap){
		return nodeScriptMapper.findNodeScript(queryMap);
	}
	
	
	/**
	 * 根据ID查询
	 */
	public BpmNodeScript getNodeScriptById(String id){
		return nodeScriptMapper.getNodeScriptById(id);
	}
	
	public List<BpmNodeScript> getScriptListByNodeId(String nodeId, String defId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("nodeId", nodeId);
		queryMap.put("defId", defId);
		return findNodeScript(queryMap);
	}

	public Map<String, BpmNodeScript> getScriptMapByNodeId(String nodeId, String defId) {
		List<BpmNodeScript> list = getScriptListByNodeId(nodeId, defId);
		Map<String, BpmNodeScript> map = new HashMap<String, BpmNodeScript>();
		for (BpmNodeScript script : list) {
			map.put("type"+script.getScriptType(), script);
		}
		return map;
	}
	
	/**
	 * 新增
	 */
	public void addNodeScript(BpmNodeScript nodeScript){
		nodeScript.setScriptId(CodeGenerator.getUUID32());
		nodeScriptMapper.addNodeScript(nodeScript);
	}
	
	/**
	 * 修改
	 */
	public void updateNodeScript(BpmNodeScript nodeScript){
		nodeScriptMapper.updateNodeScript(nodeScript);
	}
	
	/**
	 *删除
	 */
	public void deleteNodeScript(String id){
		nodeScriptMapper.deleteNodeScript(id);
	}
	
	public void deleteByNodeId(String nodeId, String defId){
		nodeScriptMapper.deleteByNodeId(nodeId, defId);
	}
	
	public void saveScript(String nodeId, String defId, String actDefId, List<BpmNodeScript> nodeScriptList){
		nodeScriptMapper.deleteByNodeId(nodeId, defId);
		for(BpmNodeScript nodeScript:nodeScriptList){
			nodeScript.setDefId(defId);
			nodeScript.setActDefId(actDefId);
			nodeScript.setNodeId(nodeId);
			addNodeScript(nodeScript);
		}
	}
	/**
	 * 根据流程定义ID和节点ID获取脚本
	 * @param nodeId
	 * @param actDefId
	 * @param scriptType
	 * @return
	 */
	public BpmNodeScript getScriptByType(String nodeId, String actDefId, String scriptType){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("nodeId", nodeId);
		queryMap.put("actDefId", actDefId);
		queryMap.put("scriptType", scriptType);
		List<BpmNodeScript> list = findNodeScript(queryMap);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}