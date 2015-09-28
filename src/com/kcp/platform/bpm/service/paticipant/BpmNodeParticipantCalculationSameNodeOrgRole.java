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
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.util.StringUtils;
@Component
public class BpmNodeParticipantCalculationSameNodeOrgRole implements IBpmNodeParticipantCalculation {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String[] arr = bpmNodeParticipant.getParticipan().split(",");
			String nodeId = arr[0]; 
			String roleId = arr[1];
			String actInstId = vars.getActInstId();
			TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(actInstId, nodeId);
			if (taskOpinion != null) {
				String userId = taskOpinion.getExeUserId();
				User user = userService.getUserById(userId);
				String orgId = user.getSsdw_zjid();
				List<User> list = userService.getUserByOrgIdRoleId(orgId, roleId);
				return list;
			}
		}
		return new ArrayList<User>();
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String[] arr = bpmNodeParticipant.getParticipan().split(",");
			String nodeId = arr[0]; 
			String roleId = arr[1];
			String actInstId = paramCalcVars.getActInstId();
			TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(actInstId, nodeId);
			if (taskOpinion != null) {
				String userId = taskOpinion.getExeUserId();
				User user = userService.getUserById(userId);
				String orgId = user.getSsdw_zjid();
				List<User> list = userService.getUserByOrgIdRoleId(orgId, roleId);
				return TaskExecutor.getExcutorsByUsers(list);
			}
		}
		return new LinkedHashSet<TaskExecutor>();
	}

	@Override
	public String getTitle() {
		return "已执行节点相同执行人同部门角色";
	}

	@Override
	public String getKey() {
		return "7";
	}
}
