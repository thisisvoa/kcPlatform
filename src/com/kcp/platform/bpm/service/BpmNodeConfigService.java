package com.kcp.platform.bpm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.dao.IBpmNodeConfigMapper;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.core.service.BaseService;

@Service("bpmNodeConfigService")
public class BpmNodeConfigService extends BaseService{
	@Autowired
	private IBpmNodeConfigMapper nodeConfigMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmNodeConfig> findNodeConfig(Map<String,Object> queryMap){
		return nodeConfigMapper.findNodeConfig(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmNodeConfig getNodeConfigById(String id){
		return nodeConfigMapper.getNodeConfigById(id);
	}
	
	/**
	 * 根据流程定义ID和节点ID查询节点配置
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeConfig getNodeConfig(String defId, String nodeId){
		return nodeConfigMapper.getNodeConfig(defId, nodeId);
	}
	
	/**
	 * 查找某个流程定义下的流程节点配置
	 * @param defId
	 * @return
	 */
	public List<BpmNodeConfig> getNodeConfigByDefId(String defId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		return findNodeConfig(queryMap);
	}
	
	/**
	 * 查找某个流程定义下的流程节点配置
	 * @param defId
	 * @return
	 */
	public List<BpmNodeConfig> getNodeConfigByDefId(String defId, String nodeType){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		queryMap.put("nodeType", nodeType);
		return findNodeConfig(queryMap);
	}
	
	/**
	 * 新增
	 */
	public void addNodeConfig(BpmNodeConfig nodeConfig){
		nodeConfig.setConfigId(CodeGenerator.getUUID32());
		nodeConfigMapper.addNodeConfig(nodeConfig);
	}
	
	/**
	 * 修改
	 */
	public void updateNodeConfig(BpmNodeConfig nodeConfig){
		nodeConfigMapper.updateNodeConfig(nodeConfig);
	}
	
	/**
	 *删除
	 */
	public void deleteNodeConfig(String id){
		nodeConfigMapper.deleteNodeConfig(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countNodeConfig(BpmNodeConfig nodeConfig){
		return nodeConfigMapper.countNodeConfig(nodeConfig);
	}
	
	public void delGlobalStartNodeConfig(String defId){
		nodeConfigMapper.delGlobalStartNodeConfig(defId);
	}
	
	/**
	 * 保存节点设置
	 * @param defId
	 * @param nodeConfigList
	 */
	public void saveNodeConfig(String defId, List<BpmNodeConfig> nodeConfigList){
		nodeConfigMapper.delGlobalStartNodeConfig(defId);
		for(BpmNodeConfig nodeConfig:nodeConfigList){
			if(StringUtils.isEmpty(nodeConfig.getConfigId())){
				addNodeConfig(nodeConfig);
			}else{
				saveConfig(nodeConfig);//保存修改
			}
		}
	}
	
	public BpmNodeConfig getByActDefIdJoinTaskKey(String actDefId, String joinTaskKey){
		return nodeConfigMapper.getByActDefIdJoinTaskKey(actDefId, joinTaskKey);
	}
	
	public BpmNodeConfig getByActDefIdNodeId(String actDefId, String nodeId){
		return nodeConfigMapper.getByActDefIdNodeId(actDefId, nodeId);
	}
	
	/**
	 * 根据流程定义ID和节点设置类型查询
	 * @param defId
	 * @param setType
	 * @return
	 */
	public BpmNodeConfig getBySetType(String defId, String setType){
		BpmNodeConfig bpmNodeConfig = this.nodeConfigMapper.getBySetType(defId, setType);
	     return bpmNodeConfig;
	}
	/**
	 * 保存配置信息
	 * @param nodeConfig
	 */
	private void saveConfig(BpmNodeConfig nodeConfig){
		BpmNodeConfig config = getNodeConfigById(nodeConfig.getConfigId());
		config.setFormType(nodeConfig.getFormType());
		config.setFormUrl(nodeConfig.getFormUrl());
		config.setFormDefName(nodeConfig.getFormDefName());
		config.setFormKey(nodeConfig.getFormKey());
		config.setBeforeHandler(nodeConfig.getBeforeHandler());
		config.setAfterHandler(nodeConfig.getAfterHandler());
		nodeConfigMapper.updateNodeConfig(config);
	}
}