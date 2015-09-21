package com.casic27.platform.bpm.sign;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.casic27.platform.bpm.constants.BpmConstants;
import com.casic27.platform.bpm.entity.BpmNodeSign;
import com.casic27.platform.bpm.entity.ProcessCmd;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.service.BpmNodeSignService;
import com.casic27.platform.bpm.service.BpmProStatusService;
import com.casic27.platform.bpm.service.BpmService;
import com.casic27.platform.bpm.service.TaskOpinionService;
import com.casic27.platform.bpm.service.TaskSignDataService;
import com.casic27.platform.bpm.service.thread.TaskThreadService;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.sys.security.context.SecurityContext;

/**
 * 
 * @author Administrator
 * 
 */
@Component("signComplete")
public class SignComplete implements ISignComplete {
	
	private Logger logger = LoggerFactory.getLogger(SignComplete.class);

	@Resource
	private BpmService bpmService;

	@Resource
	private TaskSignDataService taskSignDataService;

	@Resource
	private BpmNodeSignService bpmNodeSignService;

	@Resource
	private BpmProStatusService bpmProStatusService;

	@Resource
	private TaskOpinionService taskOpinionService;
	/**
	 * 判断会签节点是否可以结束
	 */
	public boolean isComplete(ActivityExecution execution) {
		this.logger.debug("entert the SignComplete isComplete method...");
		String nodeId = execution.getActivity().getId();
		String actInstId = execution.getProcessInstanceId();
		
		boolean isCompleted = false;
		String signResult = "";
		ProcessDefinition processDefinition = this.bpmService.getProcessDefinitionByProcessInanceId(actInstId);
		BpmNodeSign bpmNodeSign = this.bpmNodeSignService.getByDefIdAndNodeId(processDefinition.getId(), nodeId);//取得会签设置的规则
		Integer completeCounter = (Integer) execution.getVariable("nrOfCompletedInstances");//完成会签的次数
		Integer instanceOfNumbers = (Integer) execution.getVariable("nrOfInstances");//总循环次数
		Short approvalStatus = Short.valueOf(TaskThreadService.getProcessCmd().getVoteAgree().shortValue());//计算投票结果。
		
		String orgId = SecurityContext.getCurrentOrgId();
		ProcessCmd processCmd = TaskThreadService.getProcessCmd();
		if ((BpmConstants.TASK_BACK_TOSTART.equals(processCmd.isBack())) || (BpmConstants.TASK_BACK.equals(processCmd.isBack()))) {
			isCompleted = true;
		} else if ((approvalStatus.shortValue() == 5) || (approvalStatus.shortValue() == 6)) {
			isCompleted = true;
			if (approvalStatus.shortValue() == 5){
				signResult = "pass";
			} else {
				signResult = "refuse";
			}
		} else {
			List<Role> roleList = SecurityContext.getCurrentRoleList();
			boolean isOneVote = this.bpmNodeSignService.checkNodeSignPrivilege(processDefinition.getId(), nodeId,
														BpmNodeSignService.BpmNodePrivilegeType.ALLOW_ONE_VOTE,
														SecurityContext.getCurrentUserId(), orgId, roleList);
			if ((isOneVote) && (!execution.hasVariable("resultOfSign_" + nodeId))) {
				execution.setVariable("resultOfSign_" + nodeId, approvalStatus);
			}
			Short oneVoteResult = null;
			if (execution.hasVariable("resultOfSign_" + nodeId)) {
				oneVoteResult = (Short) execution.getVariable("resultOfSign_" + nodeId);
			}

			VoteResult voteResult = calcResult(bpmNodeSign, actInstId, nodeId, completeCounter, instanceOfNumbers, oneVoteResult);
			signResult = voteResult.getSignResult();
			isCompleted = voteResult.getIsComplete();
		}
		/**
		* 会签完成做的动作。
		* 1.删除会签的流程变量。
		* 2.将会签数据更新为完成。
		* 3.设置会签结果变量。
		* 4.更新会签节点结果。
		* 5.清除会签用户。
		*/
		if (isCompleted) {
			//将会签数据更新为完成。
			this.taskSignDataService.batchUpdateCompleted(actInstId, nodeId);
			Short status = null;
			if ((BpmConstants.TASK_BACK_TOSTART.equals(processCmd.isBack()))
					|| (BpmConstants.TASK_BACK.equals(processCmd.isBack()))) {
				status = processCmd.getVoteAgree();
				if ((TaskOpinion.STATUS_RECOVER_TOSTART.equals(status))
						|| (TaskOpinion.STATUS_RECOVER.equals(status))){
					signResult = "recover";
				}else if (TaskOpinion.STATUS_REJECT_TOSTART.equals(status)){
					signResult = "rejectToStart";
				}else if (TaskOpinion.STATUS_REJECT.equals(status))
					signResult = "reject";
				else{
					signResult = "UNKNOW_ACTION";
				}
			} else {
				status = TaskOpinion.STATUS_PASSED;
				if (signResult.equals("refuse")) {
					status = TaskOpinion.STATUS_NOT_PASSED;
				}
			}
			this.logger.debug("set the sign result + " + signResult);
			execution.setVariable("signResult_" + nodeId, signResult);//设置会签的结果
			String resultSign = "resultOfSign_" + nodeId;
			if (execution.hasVariable(resultSign)) {
				execution.removeVariable(resultSign);
			}
			this.bpmProStatusService.updStatus(actInstId, nodeId, status);//更新会签节点的状态。
			updOption(execution, status);//更新会签节点的状态。
			String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
			if ("sequential".equals(multiInstance)) {
				String varName = nodeId + "_" + "signUsers";
				execution.removeVariable(varName);//清除会签用户。
			}
		}
		return isCompleted;
	}
	/**
	 * 修改审批状态
	 * @param execution
	 * @param signStatus
	 */
	private void updOption(ActivityExecution execution, Short signStatus) {
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		String nodeId = execution.getCurrentActivityId();
		String actInstId = execution.getProcessInstanceId();
		if (!"parallel".equals(multiInstance))
			return;

		Short status = getStatus(signStatus);

		List<TaskOpinion> list = this.taskOpinionService.getByActInstIdTaskKeyStatus(actInstId, nodeId, TaskOpinion.STATUS_CHECKING);
		for (TaskOpinion taskOpinion : list) {
			taskOpinion.setCheckStatus(status);
			taskOpinion.setEndTime(new Date());
			Long duration = taskOpinion.getEndTime().getTime()-taskOpinion.getStartTime().getTime();
			taskOpinion.setDurTime(duration);
			this.taskOpinionService.updateTaskOpinion(taskOpinion);
		}
	}

	private Short getStatus(Short signResult) {
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Short status = TaskOpinion.STATUS_PASS_CANCEL;

		int isBack = cmd.isBack().intValue();
		boolean isRevover = cmd.isRecover();
		switch (isBack) {
		case 0:
			if (TaskOpinion.STATUS_PASSED.equals(signResult)) {
				status = TaskOpinion.STATUS_PASS_CANCEL;
			} else {
				status = TaskOpinion.STATUS_REFUSE_CANCEL;
			}
			break;
		case 1:
		case 2:
			if (isRevover) {
				status = TaskOpinion.STATUS_REVOKED_CANCEL;
			} else {
				status = TaskOpinion.STATUS_BACK_CANCEL;
			}
			break;
		}
		return status;
	}
	
	/**
	* 根据会签规则计算投票结果。
	* <pre>
	* 1.如果会签规则为空，那么需要所有的人同意通过会签，否则不通过。
	* 2.否则按照规则计算投票结果。
	* </pre>
	* @param bpmNodeSign 会签规则
	* @param actInstId 流程实例ID
	* @param nodeId 节点id名称
	* @param completeCounter 循环次数
	* @param instanceOfNumbers 总的会签次数。
	* @return
	*/
	private VoteResult calcResult(BpmNodeSign bpmNodeSign, String actInstId, String nodeId,
					Integer completeCounter, Integer instanceOfNumbers, Short oneVoteResult) {
		VoteResult voteResult = new VoteResult();
		Integer agreeAmount = this.taskSignDataService.getAgreeVoteCount(actInstId, nodeId);
		Integer refuseAmount = this.taskSignDataService.getRefuseVoteCount(actInstId, nodeId);

		if (bpmNodeSign == null) {
			voteResult = getResultNoRule(oneVoteResult, refuseAmount,agreeAmount, instanceOfNumbers);
			return voteResult;
		}

		voteResult = getResultByRule(bpmNodeSign, oneVoteResult, agreeAmount, refuseAmount, completeCounter, instanceOfNumbers);
		return voteResult;
	}

	private VoteResult getResultByRule(BpmNodeSign bpmNodeSign, Short oneVoteResult, Integer agreeAmount, 
					Integer refuseAmount, Integer completeCounter, Integer instanceOfNumbers) {
		VoteResult voteResult = new VoteResult();
		boolean isDirect = BpmNodeSign.FLOW_MODE_DIRECT.equals(bpmNodeSign.getFlowMode());
		//没有设置会签规则
		//（那么得全部会签通过才通过，否则不通过)
		if (oneVoteResult != null) {
			String result = oneVoteResult.shortValue() == 1 ? "pass" : "refuse";
			if (isDirect) {
				voteResult = new VoteResult(result, true);
			} else if (completeCounter.equals(instanceOfNumbers)) {
				voteResult = new VoteResult(result, true);
			}
			return voteResult;
		}

		if (BpmNodeSign.VOTE_TYPE_PERCENT.equals(bpmNodeSign.getVoteType())) {
			voteResult = getResultByPercent(bpmNodeSign, agreeAmount, refuseAmount, instanceOfNumbers, completeCounter);//百分比类型
		} else {
			voteResult = getResultByVotes(bpmNodeSign, agreeAmount, refuseAmount, instanceOfNumbers, completeCounter);//绝对票数
		}

		return voteResult;
	}

	private VoteResult getResultNoRule(Short oneVoteResult, Integer refuseAmount, Integer agreeAmount, Integer instanceOfNumbers) {
		VoteResult voteResult = new VoteResult();
		if (oneVoteResult != null) {
			if (oneVoteResult.shortValue() == 1) {
				voteResult.setSignResult("pass");
			} else {
				voteResult.setSignResult("refuse");
			}
			voteResult.setIsComplete(true);
		} else if (refuseAmount.intValue() > 0) {
			voteResult.setSignResult("refuse");
			voteResult.setIsComplete(true);
		} else if (agreeAmount.equals(instanceOfNumbers)) {
			voteResult.setSignResult("pass");
			voteResult.setIsComplete(true);
		}

		return voteResult;
	}

	private VoteResult getResultByVotes(BpmNodeSign bpmNodeSign, Integer agree, Integer refuse, Integer instanceOfNumbers, Integer completeCounter) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isDirect = bpmNodeSign.getFlowMode().shortValue() == 1;
		boolean isPass = false;

		if (BpmNodeSign.DECIDE_TYPE_PASS.equals(bpmNodeSign.getDecideType())) {
			if (agree.intValue() >= bpmNodeSign.getVoteAmount().longValue()) {
				result = "pass";
				isPass = true;
			} else {
				result = "refuse";
			}

		} else if (refuse.intValue() >= bpmNodeSign.getVoteAmount().longValue()) {
			result = "refuse";
			isPass = true;
		} else {
			result = "pass";
		}

		if ((isDirect) && (isPass)) {
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

	private VoteResult getResultByPercent(BpmNodeSign bpmNodeSign, Integer agree, Integer refuse, Integer instanceOfNumbers, Integer completeCounter) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isPass = false;
		boolean isDirect = bpmNodeSign.getFlowMode().shortValue() == 1;
		float percents = 0.0F;

		if (BpmNodeSign.DECIDE_TYPE_PASS.equals(bpmNodeSign.getDecideType())) {
			percents = agree.intValue() / instanceOfNumbers.intValue();

			if (percents * 100.0F >= (float) bpmNodeSign.getVoteAmount()
					.longValue()) {
				result = "pass";
				isPass = true;
			} else {
				result = "refuse";
			}
		} else {
			percents = refuse.intValue() / instanceOfNumbers.intValue();

			if (percents * 100.0F >= (float) bpmNodeSign.getVoteAmount()
					.longValue()) {
				result = "refuse";
				isPass = true;
			} else {
				result = "pass";
			}
		}

		if ((isDirect) && (isPass)) {
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

	class VoteResult {
		private String signResult = "";

		private boolean isComplete = false;

		public VoteResult() {
		}

		public VoteResult(String signResult, boolean isComplate) {
			this.signResult = signResult;
			this.isComplete = isComplate;
		}

		public String getSignResult() {
			return this.signResult;
		}

		public void setSignResult(String signResult) {
			this.signResult = signResult;
		}

		public boolean getIsComplete() {
			return this.isComplete;
		}

		public void setIsComplete(boolean isComplete) {
			this.isComplete = isComplete;
		}
	}
}
