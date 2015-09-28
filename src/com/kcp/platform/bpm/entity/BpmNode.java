package com.kcp.platform.bpm.entity;

public class BpmNode {
	private String nodeId;
	private String nodeName;
	private String nodeType;
	private Boolean isMultiple = Boolean.valueOf(false);

	private String condition = "";

	public BpmNode() {
	}

	public BpmNode(String nodeId, String nodeName, String nodeType,
			Boolean isMultiple) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.isMultiple = isMultiple;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Boolean getIsMultiple() {
		return this.isMultiple;
	}

	public void setIsMultiple(Boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}