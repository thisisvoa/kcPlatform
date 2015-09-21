package com.casic27.platform.bpm.service.paticipant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.bpm.entity.BpmNodeParticipant;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.util.StringUtils;

@Component
public class BpmNodeParticipantCalculationRole implements IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars) {
		List<User> list = new ArrayList<User>();
		if(StringUtils.isEmpty(bpmNodeParticipant.getParticipan())){
			return list;
		}
		return userService.getUserByRoleIds(bpmNodeParticipant.getParticipan().split(","));
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		short extractUser = bpmNodeParticipant.getExtractUser();
		switch(extractUser){
			case 0:
				if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
					Set<TaskExecutor> userIdSet = new LinkedHashSet<TaskExecutor>();
					String[] roleIds = bpmNodeParticipant.getParticipan().split(",");
				    String[] roleNames = bpmNodeParticipant.getParticipanDesc().split(",");
					for (int i = 0; i < roleIds.length; i++) {
						TaskExecutor taskExecutor = TaskExecutor.getTaskRole(roleIds[i].toString(), roleNames[i]);
						userIdSet.add(taskExecutor);
					}
					return userIdSet;
				}
				break;
			case 1:
				List<User> list = getExecutor(bpmNodeParticipant, vars);
				return TaskExecutor.getExcutorsByUsers(list);
		}
		return new LinkedHashSet<TaskExecutor>();
	}

	@Override
	public String getTitle() {
		return "角色";
	}

	@Override
	public String getKey() {
		return "3";
	}

}
