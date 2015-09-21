package com.casic27.platform.bpm.listener;

import org.activiti.engine.delegate.DelegateExecution;

import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.service.BpmProStatusService;
import com.casic27.platform.sys.context.SpringContextHolder;

public class AutoTaskListener extends BaseNodeEventListener
{
	protected void execute(DelegateExecution execution, String actDefId, String nodeId){
		BpmProStatusService bpmProStatusService = (BpmProStatusService)SpringContextHolder.getBean(BpmProStatusService.class);
		String actInstanceId = execution.getProcessInstanceId();
		bpmProStatusService.addOrUpd(actDefId, actInstanceId, nodeId, TaskOpinion.STATUS_EXECUTED);
	}

	protected String getScriptType()
	{
		return BpmConstants.START_SCRIPT;
	}
}
