package com.casic27.platform.bpm.listener;

import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.apache.commons.lang.StringUtils;

import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.dao.IHistoryActivityInstanceMapper;
import com.casic27.platform.bpm.dao.IProcessTaskMapper;
import com.casic27.platform.bpm.entity.ExecutionStack;
import com.casic27.platform.bpm.entity.ProcessCmd;
import com.casic27.platform.bpm.entity.TaskFork;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.service.BpmProStatusService;
import com.casic27.platform.bpm.service.BpmService;
import com.casic27.platform.bpm.service.ExecutionStackService;
import com.casic27.platform.bpm.service.TaskOpinionService;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.bpm.util.BpmUtil;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;

/**
 * 任务执行完毕监听器
 * @author Administrator
 *
 */
public class TaskCompleteListener extends BaseTaskListener {
	private static final long serialVersionUID = 169867536031676540L;

	private TaskOpinionService taskOpinionService = SpringContextHolder.getBean(TaskOpinionService.class);
	
	private BpmProStatusService bpmProStatusService = SpringContextHolder.getBean(BpmProStatusService.class);
	
	private ExecutionStackService executionStackService = SpringContextHolder.getBean(ExecutionStackService.class);
	
	private BpmService bpmService = SpringContextHolder.getBean(BpmService.class);
	
	private IProcessTaskMapper processTaskMapper = SpringContextHolder.getBean(IProcessTaskMapper.class);
	
	private IHistoryActivityInstanceMapper historyActivityInstanceMapper = SpringContextHolder.getBean(IHistoryActivityInstanceMapper.class);
	
	@Override
	protected void execute(DelegateTask delegateTask, String actDefId, String nodeId) {
		String token = (String) delegateTask.getVariableLocal(TaskFork.TAKEN_VAR_NAME);//处理分发任务
		if (token != null) {
			TaskThreadService.setToken(token);
		}
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((processCmd != null) && ((processCmd.isBack().intValue() > 0) || (StringUtils.isNotEmpty(processCmd.getDestTask())))) {
			processTaskMapper.updateNewTaskDefKeyByInstIdNodeId(delegateTask.getTaskDefinitionKey() + "_1", delegateTask.getTaskDefinitionKey(), delegateTask.getProcessInstanceId());//修改流程变量Key
		}
		//更新任务堆栈信息
		updateExecutionStack(delegateTask.getProcessInstanceId(), delegateTask.getTaskDefinitionKey(), token);//修改任务执行栈
		//修改任务审批状态、结束时间、持续时间等属性
		updOpinion(delegateTask);
		//修改节点状态
		updNodeStatus(nodeId, delegateTask);
		//修改流程活动的执行人
		setActHisAssignee(delegateTask);
	}
	/**
	 * 修改任务执行栈
	 * @param instanceId
	 * @param nodeId
	 * @param token
	 */
	private void updateExecutionStack(String instanceId, String nodeId, String token){
	     ExecutionStack executionStack = this.executionStackService.getLastestStack(instanceId, nodeId, token);
	     if (executionStack != null) {
	    	 User curUser = SecurityContext.getCurrentUser();
	    	 String userId = "";
	    	 if (curUser != null) {
	    		 userId = curUser.getZjid();
	    	 }
	    	 executionStack.setAssignees(userId);
	    	 executionStack.setEndTime(new Date());
	    	 this.executionStackService.updateExecutionStack(executionStack);
	     }
	}
	
	/**
	 * 修改流程节点状态
	 * @param nodeId
	 * @param delegateTask
	 */
	private void updNodeStatus(String nodeId, DelegateTask delegateTask) {
		boolean isMuliti = BpmUtil.isMultiTask(delegateTask);
		if (!isMuliti) {
			String actInstanceId = delegateTask.getProcessInstanceId();
			Short approvalStatus = (Short) delegateTask.getVariable("approvalStatus_" + delegateTask.getTaskDefinitionKey());
			this.bpmProStatusService.updStatus(actInstanceId, nodeId,approvalStatus);
		}
	}
	/**
	 * 修改任务审批状态、结束时间、持续时间等属性
	 * @param delegateTask
	 * @return
	 */
	private Long updOpinion(DelegateTask delegateTask){
		TaskOpinion taskOpinion = this.taskOpinionService.getTaskOpinionByTaskId(delegateTask.getId());
		if (taskOpinion == null) return Long.valueOf(0L);
	 
		User curUser = SecurityContext.getCurrentUser();
		String userId = "";
		String userName = "系统";
		if (curUser != null) {
			userId = curUser.getZjid();
			userName = curUser.getYhmc();
		}
	 
		taskOpinion.setExeUserId(userId);
		taskOpinion.setExeUserName(userName);
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		String approvalContent = cmd.getVoteContent();
		Short status = getStatus(cmd);
		taskOpinion.setCheckStatus(status);
		String fieldName = cmd.getVoteFieldName();
		if (StringUtils.isNotEmpty(fieldName)) {
			taskOpinion.setFieldName(fieldName);
		}
		taskOpinion.setOpinion(approvalContent);
		taskOpinion.setEndTime(new Date());
		Long duration = taskOpinion.getEndTime().getTime()-taskOpinion.getStartTime().getTime();
		taskOpinion.setDurTime(duration);
		taskOpinionService.updateTaskOpinion(taskOpinion);
		return duration;
	}
	
	/**
	 * 获取任务状态
	 * @param cmd
	 * @return
	 */
	private Short getStatus(ProcessCmd cmd) {
		Short status = TaskOpinion.STATUS_AGREE;
		int isBack = cmd.isBack().intValue();
		boolean isRevover = cmd.isRecover();
		int vote = cmd.getVoteAgree().shortValue();//是否会签
		switch (isBack) {
		case 0:
			switch (vote) {
				case 0:
					status = TaskOpinion.STATUS_ABANDON;
					break;
				case 1:
					status = TaskOpinion.STATUS_AGREE;
					break;
				case 2:
					status = TaskOpinion.STATUS_REFUSE;
					break;
				case 5:
					status = TaskOpinion.STATUS_PASSED;
					break;
				case 6:
					status = TaskOpinion.STATUS_NOT_PASSED;
					break;
				case 33:
					status = TaskOpinion.STATUS_SUBMIT;
					break;
				case 34:
					status = TaskOpinion.STATUS_RESUBMIT;
					break;
				case 40:
					status = TaskOpinion.STATUS_REPLACE_SUBMIT;
			}

			break;
		case 1:
			if (isRevover) {
				status = TaskOpinion.STATUS_RECOVER;
			} else {
				status = TaskOpinion.STATUS_REJECT;
			}
			break;
		case 2:
			if (isRevover) {
				status = TaskOpinion.STATUS_RECOVER_TOSTART;
			} else {
				status = TaskOpinion.STATUS_REJECT_TOSTART;
			}
			break;
		}
		return status;
	}
	
	/**
	 * 修改流程活动的执行人
	 * @param delegateTask
	 */
	private void setActHisAssignee(DelegateTask delegateTask) {
		DelegateExecution delegateExecution = delegateTask.getExecution();
		String parentId = delegateExecution.getParentId();

		String nodeId = delegateTask.getTaskDefinitionKey();

		List<HistoricActivityInstanceEntity> hisList = null;
		DelegateExecution execution = delegateExecution;
		while (execution != null) {
			hisList = this.historyActivityInstanceMapper.getByExecutionId(execution.getId(), nodeId);
			if (BeanUtils.isNotEmpty(hisList)) {
				break;
			}
			parentId = execution.getParentId();
			if (StringUtils.isEmpty(parentId))
				execution = null;
			else {
				execution = (DelegateExecution) bpmService.getExecution(parentId);
			}
		}

		if (BeanUtils.isEmpty(hisList)) {
			return;
		}

		User curUser = SecurityContext.getCurrentUser();
		if (curUser == null) {
			return;
		}
		String assignee = curUser.getZjid();
		for (HistoricActivityInstanceEntity hisActInst : hisList) {
			if (TaskOpinion.STATUS_COMMON_TRANSTO.toString().equals(delegateTask.getDescription())) {
				hisActInst.setAssignee(delegateTask.getAssignee());
			} else if ((StringUtils.isEmpty(hisActInst.getAssignee())) || (!hisActInst.getAssignee().equals(assignee))) {
				hisActInst.setAssignee(assignee);
			}
			this.historyActivityInstanceMapper.update(hisActInst);
		}
	}
	@Override
	protected String getScriptType() {
		return BpmConstants.END_SCRIPT;
	}

}
