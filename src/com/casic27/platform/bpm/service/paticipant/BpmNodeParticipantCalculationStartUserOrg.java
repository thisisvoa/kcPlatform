package com.casic27.platform.bpm.service.paticipant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.bpm.entity.BpmNodeParticipant;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.org.service.OrgService;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;

/**
 * 分配类型为发起人
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationStartUserOrg implements
		IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		String startUserId = vars.getStartUserId();
		List<User> list = new ArrayList<User>();
		User user = userService.getUserById(startUserId);
		list = userService.getUserByOrgId(user.getSsdw_zjid());
		return list;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		String startUserId = vars.getStartUserId();
		User user = userService.getUserById(startUserId);
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
		return new LinkedHashSet<TaskExecutor>();
	}

	@Override
	public String getTitle() {
		return "发起人同部门";
	}

	@Override
	public String getKey() {
		return "8";
	}

}
