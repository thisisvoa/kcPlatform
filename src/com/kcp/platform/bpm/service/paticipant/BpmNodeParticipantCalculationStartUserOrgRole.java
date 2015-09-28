package com.kcp.platform.bpm.service.paticipant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcp.platform.bpm.entity.BpmNodeParticipant;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.util.StringUtils;

/**
 * 分配类型为发起人
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationStartUserOrgRole implements
		IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String startUserId = vars.getStartUserId();
			User user = userService.getUserById(startUserId);
			String roleId = bpmNodeParticipant.getParticipan();
			String orgId = user.getSsdw_zjid();
			List<User> list = userService.getUserByOrgIdRoleId(orgId, roleId);
			return list;
		}
		return new ArrayList<User>();
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
			String startUserId = vars.getStartUserId();
			User user = userService.getUserById(startUserId);
			String roleId = bpmNodeParticipant.getParticipan();
			String orgId = user.getSsdw_zjid();
			List<User> list = userService.getUserByOrgIdRoleId(orgId, roleId);
			return TaskExecutor.getExcutorsByUsers(list);
		}
		return new LinkedHashSet<TaskExecutor>();
	}

	@Override
	public String getTitle() {
		return "发起人同部门角色";
	}

	@Override
	public String getKey() {
		return "9";
	}

}
