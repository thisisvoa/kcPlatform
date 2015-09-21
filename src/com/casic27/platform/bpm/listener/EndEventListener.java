package com.casic27.platform.bpm.listener;


import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.service.BpmFormRunService;
import com.casic27.platform.bpm.service.ExecutionStackService;
import com.casic27.platform.bpm.service.ProcessRunService;
import com.casic27.platform.bpm.service.TaskReadService;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.sys.context.SpringContextHolder;
/**
 * 流程结束时候触发，主要用于修改流程状态，删除历史临时数据。
 * @author Administrator
 *
 */
public class EndEventListener extends BaseNodeEventListener {
	private static final long serialVersionUID = 7980482347629584049L;
	
	private BpmFormRunService bpmFormRunService = SpringContextHolder.getBean(BpmFormRunService.class);
	
	private TaskReadService taskReadService = SpringContextHolder.getBean(TaskReadService.class);
	
	private ExecutionStackService executionStackService = SpringContextHolder.getBean(ExecutionStackService.class);
	
	
	@Override
	protected void execute(DelegateExecution execution, String actDefId, String nodeId) {
		ExecutionEntity ent = (ExecutionEntity) execution;
		if (!ent.isEnded()){
			return;
		}
			
		if (ent.getId().equals(ent.getProcessInstanceId()))
			handEnd(ent);
	}

	private void handEnd(ExecutionEntity ent) {
		if (ent.getParentId() == null) {
			updProcessRunStatus(ent);
			delNotifyTask(ent);
		}
	}

	/**
	 * @param ent
	 */
	private void delNotifyTask(ExecutionEntity ent) {
		String instanceId = ent.getProcessInstanceId();
		bpmFormRunService.delByInstanceId(instanceId);//删除运行表单
		taskReadService.delByActInstId(instanceId);//删除任务是否已读标志
		executionStackService.delByActInstId(instanceId);//删除任务堆栈
	}
	
	/**
	 * 修改流程状态
	 * @param ent
	 */
	private void updProcessRunStatus(ExecutionEntity ent) {
		TaskThreadService.setObject(ProcessRun.STATUS_FINISH);
		ProcessRunService processRunService = SpringContextHolder.getBean("processRunService");
		ProcessRun processRun = processRunService.getProcessRunByActInstId(ent.getProcessInstanceId());
		if (processRun==null){
			return;
		}
		processRun.setEndTime(new Date());
		processRun.setStatus(ProcessRun.STATUS_FINISH);
		processRunService.updateProcessRun(processRun);
	}

	@Override
	protected String getScriptType() {
		return BpmConstants.END_SCRIPT;
	}

}
