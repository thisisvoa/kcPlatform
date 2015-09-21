package com.casic27.platform.bpm.listener;

import java.util.Map;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.entity.BpmNodeScript;
import com.casic27.platform.bpm.entity.ProcessCmd;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.service.BpmNodeScriptService;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.core.engine.GroovyScriptEngine;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.util.StringUtils;

public abstract class BaseTaskListener implements TaskListener {
	protected Logger logger = LoggerFactory.getLogger(BaseTaskListener.class);
	public void notify(DelegateTask delegateTask) {
		this.logger.debug("执行 baseTaskListener.notify 方法...");
		TaskEntity taskEnt = (TaskEntity) delegateTask;
		String nodeId = taskEnt.getExecution().getActivityId();
		String actDefId = taskEnt.getProcessDefinitionId();
		execute(delegateTask, actDefId, nodeId);
		String scriptType = getScriptType();
		exeEventScript(delegateTask, scriptType, actDefId, nodeId);
	}

	protected abstract void execute(DelegateTask paramDelegateTask,
			String actDefId, String nodeId);

	protected abstract String getScriptType();
	/**
	 * 执行流程脚本
	 * @param delegateTask
	 * @param scriptType
	 * @param actDefId
	 * @param nodeId
	 */
	private void exeEventScript(DelegateTask delegateTask, String scriptType, String actDefId, String nodeId) {
		this.logger.debug("执行 baseTaskListener.exeEventScript 方法...");
		BpmNodeScriptService bpmNodeScriptService = (BpmNodeScriptService)SpringContextHolder.getBean(BpmNodeScriptService.class);
		BpmNodeScript model = bpmNodeScriptService.getScriptByType(nodeId, actDefId, scriptType);
		if (model == null) return;

		String script = model.getScript();
		if (StringUtils.isEmpty(script)) return;
		String instId = delegateTask.getProcessInstanceId();
		TaskThreadService.setTempLocal(instId);
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) SpringContextHolder.getBean(GroovyScriptEngine.class);
		Map<String, Object> vars = delegateTask.getVariables();
		vars.put("task", delegateTask);
		scriptEngine.execute(script, vars);
		TaskThreadService.resetTempLocal(instId);
	}
	
	protected String getNotAssignMessage(TaskExecutor taskExecutor) {
		String message = "{0}:【{1}】没有配置人员!";
		String type = "";
		if ("org".equals(taskExecutor.getType())) {
			type = "部门";
		} else if ("role".equals(taskExecutor.getType())) {
			type = "角色";
		}
		return StringUtils.formatParamMsg(message,
				new Object[] { type, taskExecutor.getExecutor() }).toString();
	}
	
	/**
	 * 是否允许代理
	 */
	protected boolean isAllowAgent() {
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((BpmConstants.TASK_BACK_TOSTART.equals(processCmd.isBack()))
				|| (BpmConstants.TASK_BACK.equals(processCmd.isBack()))) {
			return false;
		}

		String toFirstNode = (String)TaskThreadService.getToFirstNode();
		TaskThreadService.removeToFirstNode();
		if ((toFirstNode != null) && (Short.valueOf(toFirstNode).shortValue() == 1)) {
			return false;
		}

		return true;
	}
}