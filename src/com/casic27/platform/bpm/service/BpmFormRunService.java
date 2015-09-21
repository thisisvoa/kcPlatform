/**
 * @(#)com.casic27.platform.bpm.service.BpmFormRunService
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmDefinitionMapper;
import com.casic27.platform.bpm.dao.IBpmFormRunMapper;
import com.casic27.platform.bpm.dao.IBpmNodeConfigMapper;
import com.casic27.platform.bpm.entity.BpmFormDef;
import com.casic27.platform.bpm.entity.BpmFormRun;
import com.casic27.platform.bpm.entity.BpmFormTable;
import com.casic27.platform.bpm.entity.BpmNodeConfig;
import com.casic27.platform.bpm.entity.FlowNode;
import com.casic27.platform.bpm.entity.FormModel;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.util.BpmUtil;
import com.casic27.platform.core.service.BaseService;

@Service("bpmFormRunService")
public class BpmFormRunService extends BaseService {
	@Autowired
	private IBpmFormRunMapper bpmFormRunMapper;

	@Autowired
	private IBpmNodeConfigMapper bpmNodeConfigMapper;
	
	@Autowired
	private BpmFormDefService bpmFormDefService;
	
	@Autowired
	private BpmFormTableService bpmFormTableService;
	
	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private IBpmDefinitionMapper bpmDefinitionMapper;
	/**
	 * 添加
	 * 
	 * @param actDefId
	 * @param runId
	 * @param actInstanceId
	 */
	public void addBpmFormRun(String actDefId, String runId, String actInstanceId) {
		List<BpmNodeConfig> configList = bpmNodeConfigMapper.getOnlineFormByActDefId(actDefId);
		for (BpmNodeConfig config : configList) {
			BpmFormRun bpmFormRun = getByBpmNodeConfig(runId, actInstanceId, config);
			bpmFormRunMapper.addBpmFormRun(bpmFormRun);
		}
	}

	/**
	 * 获取Task节点的表单设置
	 * @param list
	 * @return
	 */
	public Map<String, BpmNodeConfig> getTaskForm(List<BpmNodeConfig> list) {
		Map<String, BpmNodeConfig> map = new HashMap<String, BpmNodeConfig>();
		for (BpmNodeConfig node : list) {
			if (node.getSetType().equals(BpmNodeConfig.TYPE_TASK)) {
				map.put(node.getNodeId(), node);
			}
		}
		return map;
	}
	
	/**
	 * 获取起始表单
	 * @param actDefId
	 * @param toFirstNode
	 * @return
	 */
	public BpmNodeConfig getStartBpmNodeConfig(String defId, String actDefId) {
		FlowNode flowNode = NodeCache.getFirstNodeId(actDefId);
		String nodeId = flowNode.getNodeId();
		BpmNodeConfig globalNodeConfig = this.bpmNodeConfigMapper.getBySetType(defId, BpmNodeConfig.TYPE_GLOBEL);
		BpmNodeConfig firstNodeConfig = this.bpmNodeConfigMapper.getByDefIdNodeId(defId, nodeId);
		if ((firstNodeConfig != null) && (StringUtils.isNotEmpty(firstNodeConfig.getFormType()))) {
	       return firstNodeConfig;
		}
		if ((globalNodeConfig != null) && (StringUtils.isNotEmpty(globalNodeConfig.getFormType()))) {
			return globalNodeConfig;
		}
		return null;
	}
	
	/**
	 * 获取流程节点表单设置
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeConfig getBpmNodeConfig(String defId, String nodeId) {
		BpmNodeConfig globalNodeConfig = this.bpmNodeConfigMapper.getBySetType(defId, BpmNodeConfig.TYPE_GLOBEL);
		BpmNodeConfig nodeConfig = this.bpmNodeConfigMapper.getByDefIdNodeId(defId, nodeId);
		if ((nodeConfig != null) && (StringUtils.isNotEmpty(nodeConfig.getFormType()))) {
	       return nodeConfig;
		}
		if ((globalNodeConfig != null) && (StringUtils.isNotEmpty(globalNodeConfig.getFormType()))) {
			return globalNodeConfig;
		}
		return null;
	}
	
	/**
	 * 能否直接开始
	 * @param defId
	 * @return
	 */
	public boolean getCanDirectStart(String defId){
	    return false;
	}
	
	/**
	 * 根据
	 */
	public void delByInstanceId(String instId){
		bpmFormRunMapper.delByInstanceId(instId);
	}
	
	/**
	 * 获取流程实例节点的运行表单
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	public BpmFormRun getByInstanceAndNode(String actInstanceId, String actNodeId){
		BpmFormRun bpmFormRun = this.bpmFormRunMapper.getByInstanceAndNode(actInstanceId, actNodeId);
	    if ((bpmFormRun != null) && (bpmFormRun.getFormType() != null) && StringUtils.isNotEmpty(bpmFormRun.getFormType())) {
	      return bpmFormRun;
	    }
	    bpmFormRun = this.bpmFormRunMapper.getGlobalForm(actInstanceId);
	    if ((bpmFormRun != null) && (bpmFormRun.getFormType() != null) && StringUtils.isNotEmpty(bpmFormRun.getFormType())) {
	      return bpmFormRun;
	    }
	    return null;
	}
	
	public FormModel getForm(ProcessRun processRun, String nodeId, String ctxPath, Map<String, Object> variables) throws Exception {
		String instanceId = processRun.getActInstId();
		String defId = processRun.getDefId();
		String businessKey = processRun.getBusinessKey();
		BpmFormRun bpmFormRun = this.getByInstanceAndNode(instanceId, nodeId);
		BpmNodeConfig bpmNodeConfig = getBpmNodeConfig(defId, nodeId);
		FormModel formModel = new FormModel();
		if (bpmFormRun != null) {
			//TODO 处理解析在线表单,后期实现
		}
		if (bpmNodeConfig == null) {
			return formModel;
		}
		if (BpmNodeConfig.NODE_FORM_TYPE_ONLINE.equals(bpmNodeConfig.getFormType())){
			//TODO 处理解析在线表单,后期实现
		} else {
			String formUrl = bpmNodeConfig.getFormUrl();
			if (StringUtils.isNotEmpty(formUrl)) {
				formUrl = getFormUrl(formUrl, businessKey, variables, ctxPath);
				formModel.setFormUrl(formUrl);
				formModel.setFormType(BpmNodeConfig.NODE_FORM_TYPE_URL);
			}
		}
		return formModel;
	}
	
	private BpmFormRun getByBpmNodeConfig(String runId, String actInstanceId, BpmNodeConfig bpmNodeConfig) {
		BpmFormRun bpmFormRun = new BpmFormRun();
		bpmFormRun.setId(CodeGenerator.getUUID32());
		bpmFormRun.setRunId(runId);
		bpmFormRun.setActInstanceId(actInstanceId);
		bpmFormRun.setActDefId(bpmNodeConfig.getActDefId());
		bpmFormRun.setActNodeId(bpmNodeConfig.getNodeId());
		bpmFormRun.setFormDefId(bpmNodeConfig.getFormDefId());
		bpmFormRun.setFormDefKey(bpmNodeConfig.getFormKey());
		bpmFormRun.setFormType(bpmNodeConfig.getFormType());
		bpmFormRun.setFormUrl(bpmNodeConfig.getFormUrl());
		bpmFormRun.setSetType(bpmNodeConfig.getSetType());
		return bpmFormRun;
	}
	
	private String getFormUrl(String formUrl, String bussinessKey, Map<String, Object> variables, String ctxPath) {
		if(variables==null){
			variables = new HashMap<String, Object>();
		}
		variables.put("pk", bussinessKey);
		String url = BpmUtil.parseTextByRule(formUrl, variables);
		if (!formUrl.startsWith("http")) {
			url = ctxPath + url;
		}
		return url;
	}
}