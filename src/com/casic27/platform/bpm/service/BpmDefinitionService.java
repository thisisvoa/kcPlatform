/**
 * @(#)com.casic27.platform.bpm.service.BpmDefinitionService
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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.IBpmDefVarMapper;
import com.casic27.platform.bpm.dao.IBpmDefinitionMapper;
import com.casic27.platform.bpm.dao.IBpmFormRunMapper;
import com.casic27.platform.bpm.dao.IBpmNodeButtonMapper;
import com.casic27.platform.bpm.dao.IBpmNodeConfigMapper;
import com.casic27.platform.bpm.dao.IBpmNodeParticipantMapper;
import com.casic27.platform.bpm.dao.IBpmNodePrivilegeMapper;
import com.casic27.platform.bpm.dao.IBpmNodeScriptMapper;
import com.casic27.platform.bpm.dao.IBpmNodeSignMapper;
import com.casic27.platform.bpm.dao.IBpmProStatusMapper;
import com.casic27.platform.bpm.dao.IExecutionStackMapper;
import com.casic27.platform.bpm.dao.IProcessExecutionMapper;
import com.casic27.platform.bpm.dao.IProcessRunMapper;
import com.casic27.platform.bpm.dao.IProcessTaskMapper;
import com.casic27.platform.bpm.dao.ITaskForkMapper;
import com.casic27.platform.bpm.dao.ITaskOpinionMapper;
import com.casic27.platform.bpm.dao.ITaskReadMapper;
import com.casic27.platform.bpm.dao.ITaskSignDataMapper;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmNodeConfig;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.graph.GraphUtil;
import com.casic27.platform.bpm.util.BPMN20Util;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;

@Service("bpmDefinitionService")
public class BpmDefinitionService extends BaseService {
	@Autowired
	private IBpmDefinitionMapper bpmDefinitionMapper;
	
	@Autowired
	private IBpmNodeConfigMapper nodeConfigMapper;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private IBpmFormRunMapper bpmFormRunMapper;
	
	@Autowired
	private IBpmNodeButtonMapper bpmNodeButtonMapper;
	
	@Autowired
	private IBpmNodePrivilegeMapper bpmNodePrivilegeMapper;
	
	@Autowired
	private IBpmNodeScriptMapper bpmNodeScriptMapper;
	
	@Autowired
	private IBpmProStatusMapper bpmProStatusMapper;
	
	@Autowired
	private ITaskReadMapper taskReadMapper;
	
	@Autowired
	private ITaskForkMapper taskForkMapper;
	
	@Autowired
	private IProcessRunMapper processRunMapper;
	
	@Autowired
	private IBpmNodeParticipantMapper bpmNodeParticipantMapper;
	
	@Autowired
	private IExecutionStackMapper executionStackMapper;
	
	@Autowired
	private ITaskSignDataMapper taskSignDataMapper;
	
	@Autowired
	private IBpmNodeSignMapper bpmNodeSignMapper;
	
	@Autowired
	private IBpmDefVarMapper bpmDefVarMapper;
	
	@Autowired
	private IBpmNodeConfigMapper bpmNodeConfigMapper;
	
	@Autowired
	private ITaskOpinionMapper taskOpinionMapper;
	
	@Autowired
	private IProcessTaskMapper processTaskMapper;
	
	@Autowired
	private IProcessExecutionMapper processExecutionMapper;
	
	
	
	/**
	 * 查询列表
	 * @param queryMap
	 */
	public List<BpmDefinition> findBpmDefinition(Map<String,Object> queryMap){
		return bpmDefinitionMapper.findBpmDefinition(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmDefinition getBpmDefinitionById(String id){
		return bpmDefinitionMapper.getBpmDefinitionById(id);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmDefinition getBpmDefinitionByActDefId(String actDefId){
		return bpmDefinitionMapper.getBpmDefinitionByActDefId(actDefId);
	}
	
	/**
	 * 查询不同版本的流程定义
	 * @param actDefKey
	 * @return
	 */
	public List<BpmDefinition> getBpmDefinitionByActDefKey(String actDefKey){
		return bpmDefinitionMapper.getBpmDefinitionByActDefKey(actDefKey);
	}
	/**
	 * 根据actDefKey查询主版本流程定义
	 * @param actDefKey
	 * @return
	 */
	public BpmDefinition getMainBpmDefinitionByKey(String actDefKey){
		return bpmDefinitionMapper.getMainBpmDefinitionByKey(actDefKey);
	}
	/**
	 * 新增
	 */
	public void addBpmDefinition(BpmDefinition bpmDefinition){
		bpmDefinition.setDefId(CodeGenerator.getUUID32());
		bpmDefinition.setCreator(SecurityContext.getCurrentUser().getZjid());
		bpmDefinition.setModifyUser(SecurityContext.getCurrentUser().getZjid());
		BpmDefinition mainBpmDefinition = bpmDefinitionMapper.getMainBpmDefinitionByKey(bpmDefinition.getDefKey());
		if(mainBpmDefinition==null){
			bpmDefinition.setVersion(1);
		}else{
			bpmDefinition.setVersion(mainBpmDefinition.getVersion()+1);
		}
		bpmDefinitionMapper.addBpmDefinition(bpmDefinition);
	}
	
	/**
	 * 修改流程定义基础信息
	 */
	public void save(BpmDefinition bpmDefinition, Boolean isDeploy)throws Exception{
		if(isDeploy){
			String oldDefId = bpmDefinition.getDefId();
			String newDefId = CodeGenerator.getUUID32();
			String actBpmnXml = GraphUtil.transform(bpmDefinition.getDefKey(), bpmDefinition.getSubject(), bpmDefinition.getDefXml());
			bpmDefinitionMapper.updateSubVersions(newDefId, bpmDefinition.getDefKey());
	 
			Deployment deployment = this.bpmService.deploy(bpmDefinition.getSubject(), actBpmnXml);
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByDeployId(deployment.getId());
			String actDefId = ent.getId();

			BpmDefinition newBpmDefinition = (BpmDefinition) bpmDefinition.clone();
			newBpmDefinition.setVersion(Integer.valueOf(ent.getVersion()));
			newBpmDefinition.setActDeployId(deployment.getId());
			newBpmDefinition.setActDefId(actDefId);
			newBpmDefinition.setActDefKey(ent.getKey());
			newBpmDefinition.setDefId(newDefId);
			newBpmDefinition.setParentDefId(newDefId);
			newBpmDefinition.setModifyTime(new Date());
			newBpmDefinition.setIsMain(CommonConst.YES);
			bpmDefinitionMapper.addBpmDefinition(newBpmDefinition);

			saveOrUpdateNodeSet(actBpmnXml, newBpmDefinition, true);
			syncStartGlobal(oldDefId, newDefId, actDefId);
		}else{
			if (StringUtils.isNotEmpty(bpmDefinition.getActDeployId())) {
				String actBpmnXml = GraphUtil.transform(bpmDefinition.getDefKey(), bpmDefinition.getSubject(), bpmDefinition.getDefXml());
				this.bpmService.wirteDefXml(bpmDefinition.getActDeployId(), actBpmnXml);
				saveOrUpdateNodeSet(actBpmnXml, bpmDefinition, false);
				String actDefId = bpmDefinition.getActDefId();
				NodeCache.clear(actDefId);
			}
			updateBpmDefinition(bpmDefinition);
		}
		
	}
	
	/**
	 * 修改
	 * @param bpmDefinition
	 */
	public void updateBpmDefinition(BpmDefinition bpmDefinition){
		bpmDefinitionMapper.updateBpmDefinition(bpmDefinition);
	}
	
	/**
	 * 删除流程节点
	 * @param defId
	 * @param isOnlyVersion
	 */
	public void deleteBpmDefinition(String defId, boolean isOnlyVersion) {
		if (BeanUtils.isEmpty(defId)) return;
		BpmDefinition definition = this.bpmDefinitionMapper.getBpmDefinitionById(defId);
		if (StringUtils.isEmpty(definition.getActDeployId())) {//未发布
			this.bpmDefinitionMapper.deleteBpmDefinition(defId);
			return;
		}
		if (isOnlyVersion) {
			delBpmDefinition(definition);
			return;
		}
		String actFlowKey = definition.getActDefKey();
		List<BpmDefinition> list = this.bpmDefinitionMapper.getBpmDefinitionByActDefKey(actFlowKey);
		for (BpmDefinition bpmDefinition : list) {
			delBpmDefinition(bpmDefinition);
		}
	}
	
	/**
	 * 删除流程实例及其关联数据
	 */
	private void delBpmDefinition(BpmDefinition bpmDefinition) {
		String actDeployId = bpmDefinition.getActDeployId();
		String defId = bpmDefinition.getDefId();
		String actDefId = bpmDefinition.getActDefId();
		if (StringUtils.isNotEmpty(actDefId)) {
			this.bpmFormRunMapper.delByActDefId(actDefId);
			this.bpmNodeButtonMapper.delByActDefId(actDefId);
			this.bpmNodePrivilegeMapper.delByActDefId(actDefId);
			this.bpmNodeScriptMapper.delByActDefId(actDefId);
			this.bpmProStatusMapper.delByActDefId(actDefId);
			this.taskReadMapper.delByActDefId(actDefId);
			this.taskForkMapper.delByActDefId(actDefId);
			this.processRunMapper.delByActDefId(actDefId);
			this.taskOpinionMapper.delByActDefId(actDefId);
			this.bpmNodeParticipantMapper.delByActDefId(actDefId);
			this.executionStackMapper.delByActDefId(actDefId);
			this.taskSignDataMapper.delByIdActDefId(actDefId);
			this.bpmNodeSignMapper.delByActDefId(actDefId);
			this.processTaskMapper.delCandidateByActDefId(actDefId);
			this.processTaskMapper.delByActDefId(actDefId);
			this.processExecutionMapper.delVariableByActDefId(actDefId);
			this.processExecutionMapper.delExecutionByActDefId(actDefId);
		}
		if (StringUtils.isNotEmpty(actDeployId)) {
			repositoryService.deleteDeployment(actDeployId,false);
		}
		this.bpmDefVarMapper.delByDefId(defId);
		this.bpmNodeConfigMapper.delByDefId(defId);
		this.bpmDefinitionMapper.deleteBpmDefinition(defId);
	}
	
	/**
	 * 发布流程
	 * @param defId
	 */
	public void deploy(String defId){
		BpmDefinition bpmDefinition = getBpmDefinitionById(defId);
		if(bpmDefinition==null){
			throw new BusinessException("流程定义不存在！");
		}
		try {
			//转化为标准的bpmn xml语言
			String actBpmnXml = GraphUtil.transform(bpmDefinition.getDefKey(), bpmDefinition.getSubject(), bpmDefinition.getDefXml());
			Deployment deployment = this.bpmService.deploy(bpmDefinition.getSubject(), actBpmnXml);
			
			ProcessDefinitionEntity processDef  = (ProcessDefinitionEntity)bpmService.getProcessDefinitionByDeployId(deployment.getId());
			bpmDefinition.setActDefId(processDef.getId());
			bpmDefinition.setActDefKey(processDef.getKey());
			bpmDefinition.setActDeployId(deployment.getId());
			bpmDefinition.setStatus(CommonConst.YES);
			bpmDefinitionMapper.updateBpmDefinition(bpmDefinition);
			
			saveOrUpdateNodeSet(actBpmnXml, bpmDefinition, true);
		} catch (Exception e) {
			throw new BusinessException("流程定义解析部署失败！",e);
		}
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmDefinition(BpmDefinition bpmDefinition){
		return bpmDefinitionMapper.countBpmDefinition(bpmDefinition);
	}
	
	/**
	 * 启用||禁用
	 * @param defId
	 * @param usable
	 */
	public void updateUsable(String[] defIds, String usable){
		for(String defId:defIds){
			bpmDefinitionMapper.updateUsable(defId, usable);
		}
	}
	
	/**
	 * 能否不填写表单直接启动
	 * @param defId
	 * @return
	 */
	public boolean getCanDirectStart(String defId){
		BpmDefinition bpmDef = bpmDefinitionMapper.getBpmDefinitionById(defId);
		if(CommonConst.YES.equals(bpmDef.getDirectStart())){
			return true;
		}
		return false;
	}
	
	/**
	 * 设置为主版本
	 * @param defId
	 */
	public void setMain(String defId){
		BpmDefinition bpmDefinition = getBpmDefinitionById(defId);
		bpmDefinitionMapper.updateSubVersions(defId, bpmDefinition.getDefKey());
		bpmDefinition.setIsMain(CommonConst.YES);
		bpmDefinitionMapper.updateBpmDefinition(bpmDefinition);
	}
	
	/**
	 * 修改节点设置
	 * @param actFlowDefXml
	 * @param bpmDefinition
	 * @param isAdd
	 * @throws Exception
	 */
	private void saveOrUpdateNodeSet(String actFlowDefXml, BpmDefinition bpmDefinition, boolean isAdd){
		BpmnModel model = BPMN20Util.convertToBpmnModel(actFlowDefXml);
		List<org.activiti.bpmn.model.Process> processes = model.getProcesses();
		if (processes.size() == 0) {
			return;
		}
		Class[] classes = {UserTask.class, CallActivity.class };
		//获取流程节点
		List<FlowElement> flowElements = BPMN20Util.getFlowElement(processes.get(0), classes);
		if (isAdd) {
			for (FlowElement flowElement : flowElements){
				addNodeConfig(bpmDefinition, flowElement);
			}
		} else {
			Map<String, BpmNodeConfig> nodeSetMap = getMapByDefId(bpmDefinition.getDefId());//获取旧的节点设置
			delNodeConfig(nodeSetMap, flowElements);//删除已经删除节点的节点设置
			updNodeConfig(bpmDefinition, nodeSetMap, flowElements);//修改还存在的节点设置
		}
	}
	
	/**
	 * 添加默认的节点设置信息
	 * @param bpmDefinition
	 * @param defId
	 * @param actNodeId
	 * @param actNodeName
	 * @throws Exception
	 */
	private void addNodeConfig(BpmDefinition bpmDefinition, FlowElement flowElement){
		String defId = bpmDefinition.getDefId();
		String actDefId = bpmDefinition.getActDefId();
		Integer nodeOrder = BPMN20Util.getOrder(flowElement);
		BpmNodeConfig nodeConfig = new BpmNodeConfig();
	    nodeConfig.setConfigId(CodeGenerator.getUUID32());
	    nodeConfig.setFormType(BpmNodeConfig.NODE_FORM_TYPE_DEFAULT);
		nodeConfig.setActDefId(actDefId);
		nodeConfig.setDefId(defId);
		nodeConfig.setNodeId(flowElement.getId());
		nodeConfig.setNodeName(flowElement.getName());
		nodeConfig.setNodeType(BpmNodeConfig.NODE_TYPE_NORMAL);
		nodeConfig.setSn(nodeOrder);
		this.nodeConfigMapper.addNodeConfig(nodeConfig);
	}
	
	/**
	 * 删除旧的节点设置
	 * @param oldSetMap
	 * @param flowNodes
	 */
	private void delNodeConfig(Map<String, BpmNodeConfig> oldSetMap, List<FlowElement> flowNodes) {
		Iterator<String> keys = oldSetMap.keySet().iterator();
		while (keys.hasNext()) {
			String nodeId = (String) keys.next();
			boolean inflag = false;
			for (FlowElement flowNode : flowNodes) {
				if (flowNode.getId().equals(nodeId)) {
					inflag = true;
					break;
				}
			}
			if (!inflag) {
				BpmNodeConfig bpmNodeSet = (BpmNodeConfig) oldSetMap.get(nodeId);
				this.nodeConfigMapper.deleteNodeConfig(bpmNodeSet.getConfigId());
			}
		}
	}
	
	/**
	 * 修改节点设置
	 * @param bpmDefinition
	 * @param oldSetMap
	 * @param flowNodes
	 * @throws Exception
	 */
	private void updNodeConfig(BpmDefinition bpmDefinition, Map<String, BpmNodeConfig> oldSetMap, List<FlowElement> flowNodes){
		for (FlowElement flowElement : flowNodes){
			if (oldSetMap.containsKey(flowElement.getId())) {
				Integer nodeOrder = BPMN20Util.getOrder(flowElement);
				BpmNodeConfig bpmNodeSet = oldSetMap.get(flowElement.getId());
				bpmNodeSet.setNodeName(flowElement.getName());
				bpmNodeSet.setSn(nodeOrder);
				this.nodeConfigMapper.updateNodeConfig(bpmNodeSet);
			} else {
				addNodeConfig(bpmDefinition, flowElement);
			}
		}
	}
	/**
	 * 组装成Map
	 * @param defId
	 * @return
	 */
	private Map<String, BpmNodeConfig> getMapByDefId(String defId){
		Map<String, BpmNodeConfig> map = new HashMap<String, BpmNodeConfig>();
	    List<BpmNodeConfig> list = getConfigByDefId(defId);
	    for (BpmNodeConfig bpmNodeSet : list) {
	    	if(!BpmNodeConfig.TYPE_GLOBEL.equals(bpmNodeSet.getSetType())){
	    		map.put(bpmNodeSet.getNodeId(), bpmNodeSet);
	    	}
	    }
	    return map;
	}
	
	/**
	 * 获取流程的节点定义
	 * @param defId
	 * @return
	 */
	private List<BpmNodeConfig> getConfigByDefId(String defId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		return nodeConfigMapper.findNodeConfig(queryMap);
	}
	
	/**
	 * 同步全局设置
	 * @param oldDefId
	 * @param newDefId
	 * @param newActDefId
	 * @throws Exception
	 */
	private void syncStartGlobal(String oldDefId, String newDefId, String newActDefId) throws Exception {
		BpmNodeConfig nodeconfig = this.nodeConfigMapper.getBySetType(oldDefId, BpmNodeConfig.TYPE_GLOBEL);
		if(nodeconfig!=null){
			nodeconfig.setConfigId(CodeGenerator.getUUID32());
			nodeconfig.setDefId(newDefId);
			nodeconfig.setActDefId(newActDefId);
			this.nodeConfigMapper.addNodeConfig(nodeconfig);
		}
  }
}