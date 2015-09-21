/**
 * @(#)com.casic27.platform.bpm.service.BpmProStatusService
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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmProStatusMapper;
import com.casic27.platform.bpm.entity.BpmProStatus;
import com.casic27.platform.bpm.entity.FlowNode;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.TaskOpinion;

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