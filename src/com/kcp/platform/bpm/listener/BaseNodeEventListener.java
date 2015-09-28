package com.kcp.platform.bpm.listener;

import java.util.Map;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kcp.platform.bpm.entity.BpmNodeScript;
import com.kcp.platform.bpm.service.BpmNodeScriptService;
import com.kcp.platform.core.engine.GroovyScriptEngine;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.util.StringUtils;

public abstract class BaseNodeEventListener implements ExecutionListener {
	
	private static final long serialVersionUID = -2018880572064495538L;
	
	private Log logger = LogFactory.getLog(GroovyScriptEngine.class);

	public void notify(DelegateExecution execution) throws Exception {
		this.logger.debug("enter the node event listener.." + execution.getId());

		ExecutionEntity ent = (ExecutionEntity) execution;

		if (ent.getActivityId() == null)
			return;

		String actDefId = ent.getProcessDefinitionId();
		String nodeId = ent.getActivityId();

		execute(execution, actDefId, nodeId);

		String scriptType = getScriptType();

		exeEventScript(execution, scriptType, actDefId, nodeId);
	}

	protected abstract void execute(DelegateExecution paramDelegateExecution,
			String actDefId, String nodeId);

	protected abstract String getScriptType();
	
	/**
	 * 执行事件groovy脚本
	 * @param execution
	 * @param scriptType
	 * @param actDefId
	 * @param nodeId
	 */
	private void exeEventScript(DelegateExecution execution, String scriptType,
			String actDefId, String nodeId) {
		BpmNodeScriptService bpmNodeScriptService = (BpmNodeScriptService) SpringContextHolder.getBean(BpmNodeScriptService.class);
		BpmNodeScript model = bpmNodeScriptService.getScriptByType(nodeId, actDefId, scriptType);
		if (model == null)
			return;

		String script = model.getScript();
		if (StringUtils.isEmpty(script))
			return;

		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) SpringContextHolder.getBean(GroovyScriptEngine.class);
		Map<String, Object> vars = execution.getVariables();
		vars.put("execution", execution);
		scriptEngine.execute(script, vars);

		this.logger.debug("execution script :" + script);
	}
}