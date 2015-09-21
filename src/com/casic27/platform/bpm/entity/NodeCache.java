package com.casic27.platform.bpm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.casic27.platform.bpm.service.BpmDefinitionService;
import com.casic27.platform.bpm.service.BpmService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.Dom4jUtil;

public class NodeCache implements Serializable {
	
	private static final long serialVersionUID = 7756312208869516482L;

	private static final Log logger = LogFactory.getLog(NodeCache.class);
	
	/**
	 * 流程节点缓存
	 */
	private static Map<String, Map<String, FlowNode>> actNodesMap = new HashMap<String, Map<String, FlowNode>>();

	public void add(String actDefId, Map<String, FlowNode> map) {
		actNodesMap.put(actDefId, map);
	}
	
	/**
	 * 获取流程节点
	 * @param actDefId
	 * @return
	 */
	public static synchronized Map<String, FlowNode> getByActDefId(String actDefId) {
		if (actNodesMap.containsKey(actDefId)) {
			return actNodesMap.get(actDefId);
		}
		Map<String, FlowNode> map = readFromXml(actDefId);
		actNodesMap.put(actDefId, map);
		return map;
	}
	
	/**
	 * 获取流程开始节点
	 * @param actDefId
	 * @return
	 */
	public static FlowNode getStartNode(String actDefId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = actNodesMap.get(actDefId);
		return getStartNode(nodeMap);
	}
	
	/**
	 * 取得开始节点
	 * @param nodeMap
	 * @return
	 */
	public static FlowNode getStartNode(Map<String, FlowNode> nodeMap) {
		for (FlowNode flowNode : nodeMap.values()) {
			if ("startEvent".equals(flowNode.getNodeType())) {
				FlowNode parentNode = flowNode.getParentNode();
				if ((parentNode == null) || ("callActivity".equals(parentNode.getNodeType()))) {
					return flowNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取第一个节点
	 * @param actDefId
	 * @return
	 */
	private static List<FlowNode> getFirstNode(String actDefId) {
		FlowNode startNode = getStartNode(actDefId);
		if (startNode == null)
			return new ArrayList<FlowNode>();
		return startNode.getNextFlowNodes();
	}
	
	/**
	 * 获取第一个节点
	 * @param actDefId
	 * @return
	 * @throws Exception
	 */
	public static FlowNode getFirstNodeId(String actDefId){
		FlowNode startNode = getStartNode(actDefId);
		if (startNode == null)
			return null;
		List<FlowNode> list = startNode.getNextFlowNodes();
		if (list.size() > 1) {
			throw new BusinessException("流程定义:" + actDefId + ",起始节点后只能设置一个节点!");
		}
		if (list.size() == 0) {
			throw new BusinessException("流程定义:" + actDefId + ",起始节点没有后续节点，请检查流程图设置!");
		}
		return (FlowNode) list.get(0);
	}
	
	/**
	 * 判断节点是否第一个节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public static boolean isFirstNode(String actDefId, String nodeId) {
		List<FlowNode> list = getFirstNode(actDefId);
		for (FlowNode flowNode : list) {
			if (nodeId.equals(flowNode.getNodeId()))
				return true;
		}
		return false;
	}
	
	/**
	 * 判断节点是否会签节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public static boolean isSignTaskNode(String actDefId, String nodeId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = actNodesMap.get(actDefId);
		FlowNode flowNode = (FlowNode) nodeMap.get(nodeId);
		if ((flowNode.getIsMultiInstance().booleanValue())
				&& (flowNode.getNodeType().equals("userTask"))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取结束节点
	 * @param actDefId
	 * @return
	 */
	public static List<FlowNode> getEndNode(String actDefId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = actNodesMap.get(actDefId);
		return getEndNode(nodeMap);
	}
	
	/**
	 * 获取节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public static FlowNode getNodeByActNodeId(String actDefId, String nodeId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = actNodesMap.get(actDefId);
		return (FlowNode) nodeMap.get(nodeId);
	}
	
	/**
	 * 获取结束节点
	 * @param nodeMap
	 * @return
	 */
	private static List<FlowNode> getEndNode(Map<String, FlowNode> nodeMap) {
		List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
		for (FlowNode flowNode : nodeMap.values()) {
			if (("endEvent".equals(flowNode.getNodeType()))
					&& (BeanUtils.isEmpty(flowNode.getNextFlowNodes()))) {
				flowNodeList.add(flowNode);
			}
		}
		return flowNodeList;
	}
	
	/**
	 * 是否有外部子流程
	 * @param actDefId
	 * @return
	 */
	public static boolean hasExternalSubprocess(String actDefId) {
		getByActDefId(actDefId);
		Map<String, FlowNode> nodeMap = actNodesMap.get(actDefId);
		for (FlowNode flowNode : nodeMap.values()) {
			if ("callActivity".equals(flowNode.getNodeType())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 清除流程定义缓存
	 * @param actDefId
	 */
	public static void clear(String actDefId) {
		actNodesMap.remove(actDefId);
	}
	
	/**
	 * 
	 * @param actDefId
	 * @return
	 */
	private static Map<String, FlowNode> readFromXml(String actDefId) {
		BpmService bpmService =  SpringContextHolder.getBean(BpmService.class);
		BpmDefinitionService bpmDefinitionService = SpringContextHolder.getBean(BpmDefinitionService.class);
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
		if (bpmDefinition == null) {
			return new HashMap<String, FlowNode>();
		}
		String deployId = bpmDefinition.getActDeployId();
		String xml = bpmService.getDefXmlByDeployId(deployId);
		return parseXml(xml, null);
	}
	
	/**
	 * 根据流程定义Key查询
	 * @param actDefkey
	 * @return
	 */
	private static String getXmlByDefKey(String actDefkey) {
		BpmService bpmService =  SpringContextHolder.getBean(BpmService.class);
		BpmDefinitionService bpmDefinitionService = SpringContextHolder.getBean(BpmDefinitionService.class);
		BpmDefinition bpmDefinition = bpmDefinitionService.getMainBpmDefinitionByKey(actDefkey);
		String deployId = bpmDefinition.getActDeployId();
		String xml = bpmService.getDefXmlByDeployId(deployId.toString());
		return xml;
	}
	
	/**
	 * 解析流程定义XML
	 * @param xml
	 * @param parentNode
	 * @return
	 */
	private static Map<String, FlowNode> parseXml(String xml, FlowNode parentNode) {
		xml = xml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document document = Dom4jUtil.loadXml(xml);
		Element el = document.getRootElement();
		Map<String, FlowNode> map = new HashMap<String, FlowNode>();
		
		/*************************解析流程节点结构**********************/
		Element processEl = (Element) el.selectSingleNode("./process");
		Iterator<Element> its = processEl.elementIterator();
		while (its.hasNext()) {
			Element nodeEl = (Element) its.next();
			String nodeType = nodeEl.getName();

			String nodeId = nodeEl.attributeValue("id");
			String nodeName = nodeEl.attributeValue("name");

			boolean isMultiInstance = nodeEl.selectSingleNode("./multiInstanceLoopCharacteristics") != null;

			if (("startEvent".equals(nodeType))
					|| ("userTask".equals(nodeType))
					|| ("parallelGateway".equals(nodeType))
					|| ("inclusiveGateway".equals(nodeType))
					|| ("exclusiveGateway".equals(nodeType))
					|| ("endEvent".equals(nodeType))
					|| ("serviceTask".equals(nodeType))) {
				FlowNode flowNode = new FlowNode(nodeId, nodeName, nodeType, isMultiInstance);
				if ("startEvent".equalsIgnoreCase(nodeType)) {
					flowNode.setParentNode(parentNode);
				}
				map.put(nodeId, flowNode);
			} else if ("subProcess".equals(nodeType)) {
				FlowNode subProcessNode = new FlowNode(nodeId, nodeName, nodeType, new ArrayList<FlowNode>(), isMultiInstance);
				map.put(nodeId, subProcessNode);
				List<Element> subElements = nodeEl.elements();
				for (Element subEl : subElements) {
					String subNodeType = subEl.getName();
					if (("startEvent".equals(subNodeType))
							|| ("userTask".equals(subNodeType))
							|| ("parallelGateway".equals(subNodeType))
							|| ("inclusiveGateway".equals(subNodeType))
							|| ("exclusiveGateway".equals(subNodeType))
							|| ("endEvent".equals(subNodeType))
							|| ("serviceTask".equals(subNodeType))) {
						String subNodeName = subEl.attributeValue("name");
						String subNodeId = subEl.attributeValue("id");
						FlowNode flowNode = new FlowNode(subNodeId, subNodeName, subNodeType, false, subProcessNode);
						subProcessNode.getSubFlowNodes().add(flowNode);
						map.put(subNodeId, flowNode);
					}
				}
			} else if ("callActivity".equals(nodeType)) {
				String flowKey = nodeEl.attributeValue("calledElement");

				String subProcessXml = getXmlByDefKey(flowKey);

				FlowNode flowNode = new FlowNode(nodeId, nodeName, nodeType,
						isMultiInstance);
				Map<String, FlowNode> subChildNodes = parseXml(subProcessXml, flowNode);
				flowNode.setAttribute("subFlowKey", flowKey);
				map.put(nodeId, flowNode);
				flowNode.setSubProcessNodes(subChildNodes);
			}

		}
		
		/**************解析流程的上下节点信息*****************/
		List<Element> seqFlowList = document.selectNodes("/definitions/process//sequenceFlow");
		for (int i = 0; i < seqFlowList.size(); i++) {
			Element seqFlow = (Element) seqFlowList.get(i);
			String sourceRef = seqFlow.attributeValue("sourceRef");
			String targetRef = seqFlow.attributeValue("targetRef");
			FlowNode sourceFlowNode = (FlowNode) map.get(sourceRef);
			FlowNode targetFlowNode = (FlowNode) map.get(targetRef);
			if ((sourceFlowNode != null) && (targetFlowNode != null)) {
				logger.debug("sourceRef:" + sourceFlowNode.getNodeName() + " targetRef:" + targetFlowNode.getNodeName());
				if (targetFlowNode.getParentNode() != null) {
					logger.debug("parentNode:" + targetFlowNode.getParentNode().getNodeName());
				}
				if (("startEvent".equals(sourceFlowNode.getNodeType())) && (sourceFlowNode.getParentNode() != null)) {
					sourceFlowNode.setFirstNode(true);
					sourceFlowNode.getParentNode().setSubFirstNode(sourceFlowNode);
					if ((targetFlowNode.getParentNode() != null) && (targetFlowNode.getParentNode().getIsMultiInstance().booleanValue())) {
						targetFlowNode.setIsMultiInstance(Boolean.valueOf(true));
					}
				}
				sourceFlowNode.getNextFlowNodes().add(targetFlowNode);
				targetFlowNode.getPreFlowNodes().add(sourceFlowNode);
			}
		}
		return map;
	}
	/**
	 * 
	 * @param xml
	 * @return
	 */
	public static Set<String> parseXmlBySubKey(String xml) {
		xml = xml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document document = Dom4jUtil.loadXml(xml);
		Element el = document.getRootElement();
		Element processEl = (Element) el.selectSingleNode("./process");
		if (BeanUtils.isEmpty(processEl))
			return null;
		Iterator its = processEl.elementIterator();
		Set<String> keySet = new HashSet<String>();
		while (its.hasNext()) {
			Element nodeEl = (Element) its.next();
			String nodeType = nodeEl.getName();
			if ("callActivity".equals(nodeType)) {
				String flowKey = nodeEl.attributeValue("calledElement");
				keySet.add(flowKey);
				String subProcessXml = getXmlByDefKey(flowKey);
				Set<String> kSet = parseXmlBySubKey(subProcessXml);
				for (String key : kSet) {
					keySet.add(key);
				}
			}
		}
		return keySet;
	}
}
