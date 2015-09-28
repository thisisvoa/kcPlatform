package com.kcp.platform.bpm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.dao.IBpmNodeButtonMapper;
import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.bpm.entity.BpmNodeButton;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.entity.FlowNode;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.util.BpmButton;
import com.kcp.platform.bpm.util.BpmButtonParser;
import com.kcp.platform.bpm.util.BpmNodeButtonXml;
import com.kcp.platform.bpm.util.BpmUtil;
import com.kcp.platform.core.exception.BaseException;
import com.kcp.platform.core.service.BaseService;

@Service("bpmNodeButtonService")
public class BpmNodeButtonService extends BaseService {
	@Autowired
	private IBpmNodeButtonMapper nodeButtonMapper;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmNodeButton> findNodeButton(Map<String,Object> queryMap){
		return nodeButtonMapper.findNodeButton(queryMap);
	}
	
	public List<BpmNodeButton> getStartNodeButton(String defId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		queryMap.put("nodeType", "0");
		return findNodeButton(queryMap);
	}
	
	/**
	 * 获取流程节点的按钮
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public List<BpmNodeButton> getNodeButtonByNodeId(String defId, String nodeId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		queryMap.put("nodeId", nodeId);
		return findNodeButton(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmNodeButton getNodeButtonById(String id){
		return nodeButtonMapper.getNodeButtonById(id);
	}
	/**
	 * 新增
	 */
	public void addNodeButton(BpmNodeButton nodeButton){
		nodeButton.setBtnId(CodeGenerator.getUUID32());
		Integer maxSn = nodeButtonMapper.getMaxSn(nodeButton.getDefId(), nodeButton.getNodeId(), nodeButton.getNodeType());
		maxSn = maxSn==null?0:maxSn;
		nodeButton.setSn(maxSn+1);
		nodeButtonMapper.addNodeButton(nodeButton);
	}
	
	/**
	 * 修改
	 */
	public void updateNodeButton(BpmNodeButton nodeButton){
		nodeButtonMapper.updateNodeButton(nodeButton);
	}
	
	/**
	 *删除
	 */
	public void deleteNodeButtonById(String id){
		if(StringUtils.isNotEmpty(id)){
			BpmNodeButton nodeButton = new BpmNodeButton();
			nodeButton.setBtnId(id);
			nodeButtonMapper.deleteNodeButton(nodeButton);
		}
	}
	/**
	 * 删除某个流程的按钮
	 * @param defId
	 */
	public void deleteNodeButtonByDefId(String defId){
		if(StringUtils.isNotEmpty(defId)){
			BpmNodeButton nodeButton = new BpmNodeButton();
			nodeButton.setDefId(defId);
			nodeButtonMapper.deleteNodeButton(nodeButton);
		}
	}
	/**
	 * 删除某个节点的按钮
	 * @param defId
	 * @param nodeId
	 * @param nodeType
	 */
	public void deleteNodeButtonByNodeId(String defId, String nodeId, String nodeType){
		BpmNodeButton nodeButton = new BpmNodeButton();
		nodeButton.setDefId(defId);
		nodeButton.setNodeId(nodeId);
		nodeButton.setNodeType(nodeType);
		nodeButtonMapper.deleteNodeButton(nodeButton);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countNodeButton(BpmNodeButton nodeButton){
		return nodeButtonMapper.countNodeButton(nodeButton);
	}
	
	/**
	 * 初始化流程按钮
	 * @param defId
	 */
	public void initAll(String defId)throws Exception{
		deleteNodeButtonByDefId(defId);
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		String actDefId = bpmDefinition.getActDefId();
		initStartForm(actDefId, defId);
		Map<String,FlowNode> map = NodeCache.getByActDefId(actDefId);
		Set<Map.Entry<String,FlowNode>> set = map.entrySet();
		for(Iterator<Map.Entry<String,FlowNode>> it = set.iterator(); it.hasNext(); ) {
	    	 Map.Entry<String,FlowNode> entry = it.next();
	    	 FlowNode flowNode = (FlowNode)entry.getValue();
	    	 if(("userTask".equals(flowNode.getNodeType()))){
		    	 boolean isSignTask = flowNode.getIsSignNode();
		    	 boolean isFirstNode = flowNode.getIsFirstNode();
		    	 initNodeId(actDefId, defId, (String)entry.getKey(), isSignTask, isFirstNode);
	    	 }
	     }
	}
	
	/**
	 * 初始化单节点按钮
	 * @param defId
	 * @param configId
	 * @param nodeType
	 */
	public void init(String defId, String nodeId, String nodeType)throws Exception{
		deleteNodeButtonByNodeId(defId, nodeId, nodeType);
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		String actDefId = bpmDefinition.getActDefId();
		if("0".equals(nodeType)){
			initStartForm(actDefId, defId);
		}else{
			boolean isSignTask = this.bpmService.isSignTask(actDefId, nodeId);
			boolean isFirstNode = NodeCache.isFirstNode(actDefId, nodeId);
			initNodeId(actDefId, defId, nodeId, isSignTask, isFirstNode);
		}
	}
	
	/**
	 * 获取流程的所有按钮定义
	 */
	public List<BpmNodeButton> getNodeButtonByDefId(String defId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("defId", defId);
		return findNodeButton(queryMap);
	}
	/**
	 * 开始节点
	 * @param defId
	 * @return
	 */
	public List<Map<String,Object>> getNodeButtonsByDefId(String defId)throws Exception{
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		List<BpmNodeButton> btnList =  getNodeButtonByDefId(defId);
		btnList = (btnList==null)?new ArrayList<BpmNodeButton>():btnList;
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> startNode = new HashMap<String, Object>();
		startNode.put("nodeId", "_startNode_");
		startNode.put("nodeName", "起始节点");
		startNode.put("nodeTypeName", "开始节点");
		startNode.put("nodeType", "0");
		startNode.put("defId", defId);
		startNode.put("btnDesc", getStartNodeBtnDesc(btnList));
		result.add(startNode);
		
		String actDefId = bpmDefinition.getActDefId();
		Map<String,FlowNode> map = NodeCache.getByActDefId(actDefId);
		Set<Map.Entry<String,FlowNode>> set = map.entrySet();
		for(Iterator<Map.Entry<String,FlowNode>> it = set.iterator(); it.hasNext();) {
	    	 Map.Entry<String,FlowNode> entry = it.next();
	    	 String nodeId = entry.getKey();
	    	 FlowNode flowNode = (FlowNode)entry.getValue();
	    	 if(("userTask".equals(flowNode.getNodeType()))){
	    		 boolean isSignTask = flowNode.getIsSignNode();
		    	 boolean isFirstNode = flowNode.getIsFirstNode();
		    	 boolean isSubProcess = flowNode.getIsSubProcess();
		    	 Map<String,Object> node = new HashMap<String, Object>();
		    	 node.put("nodeId", nodeId);
		    	 node.put("nodeName", flowNode.getNodeName());
		    	 node.put("defId", defId);
		    	 if(isSignTask){
		    		 node.put("nodeTypeName", "会签节点");
		    		 node.put("nodeType", "2");
		    		 node.put("btnDesc", getNodeBtnDesc(btnList, nodeId, isSignTask, isFirstNode));
		    	 }else if(isSubProcess){
		    		 node.put("nodeTypeName", "子流程");
		    		 node.put("nodeType", "3");
		    	 }else{
		    		 node.put("nodeTypeName", "普通节点");
		    		 node.put("nodeType", "1");
		    		 node.put("btnDesc", getNodeBtnDesc(btnList, nodeId, isSignTask, isFirstNode));
		    	 }
		    	 result.add(node);
	    	 }
		}
		return result;
	}
	
	/**
	 * 批量更新序号
	 * @param btnList
	 */
	public void updateSns(List<BpmNodeButton> btnList){
		for(BpmNodeButton btn:btnList){
			nodeButtonMapper.updateSn(btn);
		}
	}
	
	/**
	 * 初始化开始节点
	 * @param actDefId
	 * @param defId
	 * @throws Exception
	 */
	private void initStartForm(String actDefId, String defId)throws Exception{
		List<BpmButton> startBtnList = BpmButtonParser.parse().getStartBtnList();
		int i = 1;
		for (BpmButton button : startBtnList){
			BpmNodeButton bpmNodeButton = new BpmNodeButton();
			bpmNodeButton.setDefId(defId);
			bpmNodeButton.setActDefId(actDefId);
			bpmNodeButton.setBtnName(button.getText());
			bpmNodeButton.setType(button.getOperatortype().toString());
			bpmNodeButton.setNodeType("0");
			addNodeButton(bpmNodeButton);
			i++;
		}
	}
	
	/**
	 * 初始化一般任务节点
	 * @param actDefId
	 * @param defId
	 * @param nodeId
	 * @param isSignTask
	 * @param isFirstNode
	 * @throws Exception
	 */
	private void initNodeId(String actDefId, String defId, String nodeId, boolean isSignTask, boolean isFirstNode)throws Exception {
		List<BpmButton> btnList = null;
		String nodeType = isSignTask ? "2" : "1";
		if (isFirstNode) {
			btnList = BpmButtonParser.parse().getFirstNodeBtnList();
		} else if (isSignTask) {
			btnList = BpmButtonParser.parse().getSignBtnList();
		} else{
			btnList = BpmButtonParser.parse().getCommonBtnList();
		}
		int i = 1;
		for (BpmButton button : btnList){
			BpmNodeButton bpmNodeButton = new BpmNodeButton();
			bpmNodeButton.setDefId(defId);
			bpmNodeButton.setNodeId(nodeId);
			bpmNodeButton.setActDefId(actDefId);
			bpmNodeButton.setBtnName(button.getText());
			bpmNodeButton.setType(button.getOperatortype().toString());
			bpmNodeButton.setNodeType(nodeType);
			addNodeButton(bpmNodeButton);
			i++;
		}
	}
	
	/**
	 * 开始节点按钮描述
	 * @param btnList
	 * @return
	 * @throws Exception
	 */
	private String getStartNodeBtnDesc(List<BpmNodeButton> btnList)throws Exception{
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmNodeButton btn:btnList){
			if("0".equals(btn.getNodeType())){
				sb.append(spliter).append(btn.getBtnName());
				spliter = ",";
			}
		}
		if(StringUtils.isEmpty(sb.toString())){
			return BpmButtonParser.parse().startBtnDesc();
		}else{
			return sb.toString();
		}
	}
	/**
	 * 普通节点描述
	 * @param btnList
	 * @param nodeId
	 * @param isSignTask
	 * @param isFirstNode
	 * @return
	 * @throws Exception
	 */
	private String getNodeBtnDesc(List<BpmNodeButton> btnList, String nodeId, boolean isSignTask, boolean isFirstNode)throws Exception{
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmNodeButton btn:btnList){
			if(nodeId.equals(btn.getNodeId())){
				sb.append(spliter).append(btn.getBtnName());
				spliter = ",";
			}
		}
		if(StringUtils.isEmpty(sb.toString())){
			if(isSignTask){
				return BpmButtonParser.parse().signBtnDesc();
			}
			if(isFirstNode){
				return BpmButtonParser.parse().firstBtnDesc();
			}
			return BpmButtonParser.parse().commonBtnDesc();
		}else{
			return sb.toString();
		}
	}
}