package com.kcp.platform.bpm.listener;

import org.activiti.engine.delegate.DelegateTask;

import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.service.TaskOpinionService;
import com.kcp.platform.sys.context.SpringContextHolder;

/**
 * activiti任务分配完用户后执行
 * @author Administrator
 *
 */
public class TaskAssignListener extends BaseTaskListener {
	private static final long serialVersionUID = -7087076131988033689L;
	
	private TaskOpinionService taskOpinionService = SpringContextHolder.getBean(TaskOpinionService.class);
	
	/**
	 * 更新任务审批用户
	 */
	@Override
	protected void execute(DelegateTask delegateTask, String actDefId,
			String nodeId) {
		String userId = delegateTask.getAssignee();
		this.logger.debug("任务ID:" + delegateTask.getId());
		TaskOpinion taskOpinion = this.taskOpinionService.getTaskOpinionByTaskId(delegateTask.getId());
		if (taskOpinion != null) {
			this.logger.debug("update taskopinion exe userId" + userId);
			taskOpinion.setExeUserId(userId);
			this.taskOpinionService.updateTaskOpinion(taskOpinion);
		}
		delegateTask.setOwner(userId);
	}

	@Override
	protected String getScriptType() {
		return BpmConstants.ASSIGN_SCRIPT;
	}

}
