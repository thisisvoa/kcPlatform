package com.kcp.platform.bpm.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.bpm.dao.IBpmProStatusMapper;
import com.kcp.platform.bpm.entity.BpmProStatus;
import com.kcp.platform.bpm.entity.FlowNode;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.entity.TaskOpinion;

@Service
public class BpmProStatusService {
	@Autowired
	private IBpmProStatusMapper bpmProStatusMapper;

	public void addOrUpd(String actDefId, String processInstanceId, String nodeId) {
		addOrUpd(actDefId, processInstanceId, nodeId,
				TaskOpinion.STATUS_CHECKING);
	}

	public void addOrUpd(String actDefId, String processInstanceId, String nodeId, Short status) {
		BpmProStatus bpmProStatus = this.bpmProStatusMapper.getByInstNodeId(processInstanceId,nodeId);
		if (bpmProStatus == null) {
			Map<String,FlowNode> mapNode = NodeCache.getByActDefId(actDefId);
			BpmProStatus tmp = new BpmProStatus();
			tmp.setId(CodeGenerator.getUUID32());
			tmp.setActDefId(actDefId);
			tmp.setActInstId(processInstanceId);
			tmp.setLastUpdateTime(new Date());
			tmp.setNodeId(nodeId);
			tmp.setStatus(status);
			FlowNode flowNode = (FlowNode) mapNode.get(nodeId);
			tmp.setNodeName(flowNode.getNodeName());
			this.bpmProStatusMapper.addBpmProStatus(tmp);
		} else {
			bpmProStatus.setStatus(status);
			bpmProStatus.setLastUpdateTime(new Date());
			this.bpmProStatusMapper.updStatus(bpmProStatus);
		}
	}
	
	public void updStatus(String actInstanceId, String nodeId, Short status) {
		BpmProStatus bpmProStatus = new BpmProStatus();
		bpmProStatus.setActInstId(actInstanceId);
		bpmProStatus.setNodeId(nodeId);
		bpmProStatus.setStatus(status);
		bpmProStatus.setLastUpdateTime(new Date());
		bpmProStatusMapper.updStatus(bpmProStatus);
	}
	
	public void delByActInstId(String actInstId){
		this.bpmProStatusMapper.delByActInstId(actInstId);
	}
	
}