package com.kcp.platform.bpm.delegate;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.BpmNodeScript;
import com.kcp.platform.bpm.service.BpmNodeScriptService;
import com.kcp.platform.core.engine.GroovyScriptEngine;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.util.StringUtils;


/**
 * 脚本节点
 * @author Administrator
 */
public class ScriptTask implements JavaDelegate {
	private Log logger = LogFactory.getLog(ScriptTask.class);
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity ent = (ExecutionEntity)execution;
	    String nodeId = ent.getActivityId();
	    String actDefId = ent.getProcessDefinitionId();

	    BpmNodeScriptService bpmNodeScriptService = (BpmNodeScriptService)SpringContextHolder.getBean(BpmNodeScriptService.class);
	    BpmNodeScript model = bpmNodeScriptService.getScriptByType(nodeId, actDefId, BpmConstants.SCRIPTNODE_SCRIPT);
	    if (model == null) return;

	    String script = model.getScript();
	    if (StringUtils.isEmpty(script)) return;

	    GroovyScriptEngine scriptEngine = (GroovyScriptEngine)SpringContextHolder.getBean(GroovyScriptEngine.class);
	    Map vars = execution.getVariables();
	    vars.put("execution", execution);
	    scriptEngine.execute(script, vars);

	    this.logger.debug("execution script :" + script);

	}

}
