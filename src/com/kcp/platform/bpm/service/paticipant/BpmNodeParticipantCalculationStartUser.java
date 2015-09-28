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

/**
 * 分配类型为发起人
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationStartUser implements
		IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		String startUserId = vars.getStartUserId();
		List<User> list = new ArrayList<User>();
		User user = userService.getUserById(startUserId);
		list.add(user);
		return list;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		String startUserId = vars.getStartUserId();
		Set<TaskExecutor> list = new LinkedHashSet<TaskExecutor>();
		User user = userService.getUserById(startUserId);
		list.add(TaskExecutor.getTaskUser(user.getZjid(),user.getYhmc()));
		return list;
	}

	@Override
	public String getTitle() {
		return "发起人";
	}

	@Override
	public String getKey() {
		return "1";
	}

}
