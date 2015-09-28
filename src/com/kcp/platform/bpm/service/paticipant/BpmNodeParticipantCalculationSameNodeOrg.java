package com.kcp.platform.bpm.service.paticipant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcp.platform.bpm.entity.BpmNodeParticipant;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.service.TaskOpinionService;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.util.StringUtils;
@Component
public class BpmNodeParticipantCalculationSameNodeOrg implements IBpmNodeParticipantCalculation {
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String actInstId = vars.getActInstId();
			TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(actInstId, bpmNodeParticipant.getParticipan());
			if (taskOpinion != null) {
				User user = userService.getUserById(taskOpinion.getExeUserId());
				List<User> list = userService.getUserByOrgId(user.getSsdw_zjid());
				return list;
		    }
		}
		return new ArrayList<User>();
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String actInstId = paramCalcVars.getActInstId();
			TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(actInstId, bpmNodeParticipant.getParticipan());
			if (taskOpinion != null) {
				String userId = taskOpinion.getExeUserId();
				User user = userService.getUserById(userId);
				String orgId = user.getSsdw_zjid();
				short extractUser = bpmNodeParticipant.getExtractUser();
				switch(extractUser){
					case 0:
						Set<TaskExecutor> userIdSet = new LinkedHashSet<TaskExecutor>();
						Org org = orgService.getOrgById(orgId);
						userIdSet.add(TaskExecutor.getTaskOrg(org.getZjid(), org.getDwmc()));
						return userIdSet;
					case 1:
						List<User> list = userService.getUserByOrgId(orgId);
						return TaskExecutor.getExcutorsByUsers(list);
				}
			}
		}
		return new LinkedHashSet<TaskExecutor>();
	}

	@Override
	public String getTitle() {
		return "已执行节点相同执行人同部门";
	}

	@Override
	public String getKey() {
		return "6";
	}

}
