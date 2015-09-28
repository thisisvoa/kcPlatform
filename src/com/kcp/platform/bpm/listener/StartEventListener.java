package com.kcp.platform.bpm.listener;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.service.BpmDefinitionService;
import com.kcp.platform.bpm.service.BpmFormRunService;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.bpm.service.thread.TaskThreadService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.CodeGenerator;

public class StartEventListener extends BaseNodeEventListener {
	private static final long serialVersionUID = -6934855844332653133L;
	
	private static final Log logger = LogFactory.getLog(StartEventListener.class);
	
	@Override
	protected void execute(DelegateExecution execution, String actDefId, String nodeId) {
		logger.debug("start nodeId" + nodeId);
		handExtSubProcess(execution);
	}
	
	private void handExtSubProcess(DelegateExecution execution)
	{
	     ExecutionEntity ent = (ExecutionEntity)execution;
	 
	     if (execution.getVariable("innerPassVars") == null) return;
	 
	     BpmFormRunService bpmFormRunService = (BpmFormRunService)SpringContextHolder.getBean(BpmFormRunService.class);
	 
	     Map<String,Object> variables = (Map<String,Object>)ent.getVariable("innerPassVars");
	 
	     Boolean isExtCall = (Boolean)variables.get("isExtCall");
	 
	     String instanceId = ent.getProcessInstanceId();
	 
	     TaskThreadService.addExtSubProcess(instanceId);
	 
	     String actDefId = ent.getProcessDefinitionId();
	 
	     String runId = initProcessRun(actDefId, instanceId, variables);
	     execution.setVariable("flowRunId", runId);
	 
	     variables.put("flowRunId", runId);
	     execution.setVariables(variables);
	 
	     if ((isExtCall != null) && (!isExtCall.booleanValue())){
	    	 bpmFormRunService.addBpmFormRun(actDefId, runId, instanceId);
	     }
	}
	 
	private String initProcessRun(String actDefId, String instanceId, Map<String, Object> variables)
	{
	     String businessKey = (String)variables.get("businessKey");
	     String parentRunId = (String)variables.get("flowRunId");
	 
	     BpmDefinitionService bpmDefinitionService = (BpmDefinitionService)SpringContextHolder.getBean(BpmDefinitionService.class);
	     ProcessRunService processRunService = (ProcessRunService)SpringContextHolder.getBean(ProcessRunService.class);
	     BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
	 
	     ProcessRun processRun = new ProcessRun();
	     User curUser = SecurityContext.getCurrentUser();
	     if(curUser!=null){
	    	 processRun.setCreator(curUser.getYhmc());
		     processRun.setCreatorId(curUser.getZjid());
	     }
	     processRun.setActDefId(bpmDefinition.getActDefId());
	     processRun.setDefId(bpmDefinition.getDefId());
	     processRun.setProcessName(bpmDefinition.getSubject());
	 
	     processRun.setActInstId(instanceId);
	 
	     String subject = (String)variables.get("subject_");
	     processRun.setSubject(subject);
	     processRun.setBusinessKey(businessKey);
	 
	     processRun.setCreateTime(new Date());
	     processRun.setStatus(ProcessRun.STATUS_RUNNING);
	     processRun.setRunId(CodeGenerator.getUUID32());
	     processRun.setParentId(parentRunId);
	     processRunService.addProcessRun(processRun);
	     return processRun.getRunId();
	}

	@Override
	protected String getScriptType() {
		return BpmConstants.START_SCRIPT;
	}

}
