package com.kcp.platform.bpm.entity;

import java.util.Date;

public class BpmProStatus{

	/**
	 * ID
	 */
	private String id;

	/**
	 * 流程实例ID
	 */
	private String actInstId;

	/**
	 * 流程节点ID
	 */
	private String nodeId;

	/**
	 * 流程节点名称
	 */
	private String nodeName;

	/**
	 * 状态
	 */
	private Short status;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	/**
	 * 流程定义ID
	 */
	private String actDefId;

	/**
	 * DEF_ID
	 */
	private String defId;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getActInstId(){
		return this.actInstId;
	}
	
	public void setActInstId(String actInstId){
		this.actInstId = actInstId;
	}

	public String getNodeId(){
		return this.nodeId;
	}
	
	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getNodeName(){
		return this.nodeName;
	}
	
	public void setNodeName(String nodeName){
		this.nodeName = nodeName;
	}

	public Short getStatus(){
		return this.status;
	}
	
	public void setStatus(Short status){
		this.status = status;
	}

	public Date getLastUpdateTime(){
		return this.lastUpdateTime;
	}
	
	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getActDefId(){
		return this.actDefId;
	}
	
	public void setActDefId(String actDefId){
		this.actDefId = actDefId;
	}

	public String getDefId(){
		return this.defId;
	}
	
	public void setDefId(String defId){
		this.defId = defId;
	}
}
