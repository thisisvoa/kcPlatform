package com.kcp.platform.bpm.listener;

import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.entity.TaskSignData;
import com.kcp.platform.bpm.service.BpmAgentSettingService;
import com.kcp.platform.bpm.service.BpmProStatusService;
import com.kcp.platform.bpm.service.TaskOpinionService;
import com.kcp.platform.bpm.service.TaskSignDataService;
import com.kcp.platform.bpm.service.thread.TaskThreadService;
import com.kcp.platform.bpm.service.thread.TaskUserAssignService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.util.BeanUtils;
import com.kcp.platform.util.CodeGenerator;

/**
 * 会签任务创建监听器
 * @author Administrator
 *
 */
public class TaskSignCreateListener extends BaseTaskListener {
	private static final long serialVersionUID = -632072725972940858L;

	private Logger logger = LoggerFactory.getLogger(TaskSignCreateListener.class);
	
	private TaskSignDataService taskSignDataService = SpringContextHolder.getBean(TaskSignDataService.class);
	
	private TaskOpinionService taskOpinionService = SpringContextHolder.getBean(TaskOpinionService.class);
	
	private TaskUserAssignService taskUserAssignService = SpringContextHolder.getBean(TaskUserAssignService.class);
	
	private BpmProStatusService bpmProStatusService = SpringContextHolder.getBean(BpmProStatusService.class);
	
	private UserService userService = SpringContextHolder.getBean(UserService.class);
	
	private BpmAgentSettingService bpmAgentSettingService = SpringContextHolder.getBean(BpmAgentSettingService.class);
	
	protected void execute(DelegateTask delegateTask, String actDefId, String nodeId) {
		//添加当前节点审批
		delegateTask.setDescription(TaskOpinion.STATUS_CHECKING.toString());
		TaskOpinion taskOpinion = new TaskOpinion(delegateTask);
		taskOpinion.setOpinionId(CodeGenerator.getUUID32());
		this.taskOpinionService.addTaskOpinion(taskOpinion);
		
		//设置节点执行人
		TaskExecutor taskExecutor = (TaskExecutor) delegateTask.getVariable("assignee");
		assignTaskExecutor(delegateTask, taskExecutor);
		TaskThreadService.addTask((TaskEntity) delegateTask);
		
		//添加会签数据
		String processInstanceId = delegateTask.getProcessInstanceId();
		//总结的会签实例数
		Integer instanceOfNumbers = (Integer) delegateTask.getVariable("nrOfInstances");
		//会签计数器
		Integer loopCounter = (Integer) delegateTask.getVariable("loopCounter");
		if (loopCounter == null){
			loopCounter = Integer.valueOf(0);
		}

		this.logger.debug("instance of numbers:" + instanceOfNumbers + " loopCounters:" + loopCounter);
		//创建第一个会签任务的时候把会签数据加入到BPM_TKSIGN_DATA中
		if (loopCounter.intValue() == 0) {
			addSignData(delegateTask, nodeId, processInstanceId, instanceOfNumbers);
		}
		//修改流程节点状态状态
		this.bpmProStatusService.addOrUpd(actDefId, processInstanceId, nodeId);
		//修改会签数据
		updTaskSignData(processInstanceId, nodeId, taskExecutor, delegateTask.getId());
	}
	
	/**
	 * 修改会签数据
	 * @param processInstanceId
	 * @param nodeId
	 * @param taskExecutor
	 * @param taskId
	 */
	private void updTaskSignData(String processInstanceId, String nodeId, TaskExecutor taskExecutor, String taskId) {
		String executorId = taskExecutor.getExecuteId();
		TaskSignData taskSignData = this.taskSignDataService.getUserTaskSign(processInstanceId, nodeId, executorId);
		if (taskSignData == null)
			return;
		taskSignData.setTaskId(taskId);
		this.taskSignDataService.updateTaskSignData(taskSignData);
	}
	
	/**
	 * 添加会签数据
	 * @param delegateTask
	 * @param nodeId
	 * @param processInstanceId
	 * @param instanceOfNumbers
	 */
	private void addSignData(DelegateTask delegateTask, String nodeId, String processInstanceId, Integer instanceOfNumbers) {
		List<TaskExecutor> signUserList = this.taskUserAssignService.getNodeUserMap().get(nodeId);
		if (signUserList == null) return;

		int batch = this.taskSignDataService.getMaxBatch(processInstanceId, nodeId) + 1;
		for (int i = 0; i < instanceOfNumbers.intValue(); i++) {
			int sn = i + 1;

			TaskSignData signData = new TaskSignData();
			signData.setActDefId(delegateTask.getProcessDefinitionId());
			signData.setActInstId(processInstanceId);
			signData.setNodeName(delegateTask.getName());
			signData.setNodeId(nodeId);
			signData.setSignNums(Integer.valueOf(sn));
			signData.setIsCompleted(TaskSignData.NOT_COMPLETED);
			TaskExecutor signUser = (TaskExecutor) signUserList.get(i);
			if (signUser != null) {
				signData.setVoteUserId(signUser.getExecuteId());
				signData.setVoteUserName(signUser.getExecutor());
			}
			signData.setDataId(CodeGenerator.getUUID32());
			signData.setBatch(batch);
			this.taskSignDataService.addSignData(signData);
		}
	}
	/**
	 * 设置用户
	 * @param delegateTask
	 * @param taskExecutor
	 * @param sysUserId
	 */
	private void setAssignUser(DelegateTask delegateTask, TaskExecutor taskExecutor, String sysUserId) {
		User user = null;
		if (isAllowAgent()) {
			user = this.bpmAgentSettingService.getAgent(delegateTask, sysUserId);
		}
		if (user != null) {
			delegateTask.setAssignee(user.getZjid());
			delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
			delegateTask.setOwner(sysUserId.toString());
		} else {
			delegateTask.setAssignee(sysUserId.toString());
		}
		TaskOpinion taskOpinion = this.taskOpinionService.getByTaskId(delegateTask.getId());
		User exeUser = this.userService.getUserById(sysUserId);
		taskOpinion.setExeUserId(exeUser.getZjid());
		taskOpinion.setExeUserName(exeUser.getYhmc());
		this.taskOpinionService.updateTaskOpinion(taskOpinion);
	}

	/**
	 * 设置会签节点执行人员
	 * @param delegateTask
	 * @param taskExecutor
	 */
	private void assignTaskExecutor(DelegateTask delegateTask, TaskExecutor taskExecutor) {
		if ("user".equals(taskExecutor.getType())) {
			delegateTask.setOwner(taskExecutor.getExecuteId());
			String sysUserId = taskExecutor.getExecuteId();
			setAssignUser(delegateTask, taskExecutor, sysUserId);
		} else {
			delegateTask.setAssignee("0");
			delegateTask.setOwner("0");
			if (1 == taskExecutor.getExactType()) {
				Set<User> userList = taskExecutor.getUser();
				if (BeanUtils.isEmpty(userList)) {
					String msg = "[" + taskExecutor.getExecutor() + "],没有设置人员,请先设置人员。";
					throw new BusinessException(msg);
				}
				for (User sysUser : userList) {
					delegateTask.addCandidateUser(sysUser.getZjid());
				}

			} else if (taskExecutor.getExactType() == 0) {
				delegateTask.addGroupIdentityLink(taskExecutor.getExecuteId(), taskExecutor.getType());
			}
		}
	}

	protected String getScriptType() {
		return BpmConstants.START_SCRIPT;
	}
}
