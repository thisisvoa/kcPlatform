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

@Component
public class BpmNodeParticipantCalculationOrg implements IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		List<User> list = new ArrayList<User>();
		if(StringUtils.isEmpty(bpmNodeParticipant.getParticipan())){
			return list;
		}
		return userService.getUserByOrgIds(bpmNodeParticipant.getParticipan().split(","));
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		short extractUser = bpmNodeParticipant.getExtractUser();
		switch(extractUser){
			case 0:
				if(StringUtils.isNotEmpty(bpmNodeParticipant.getParticipan())){
					Set<TaskExecutor> userIdSet = new LinkedHashSet<TaskExecutor>();
					String[] orgIds = bpmNodeParticipant.getParticipan().split(",");
				    String[] orgNames = bpmNodeParticipant.getParticipanDesc().split(",");
					for (int i = 0; i < orgIds.length; i++) {
						TaskExecutor taskExecutor = TaskExecutor.getTaskOrg(orgIds[i].toString(), orgNames[i]);
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
		return "单位";
	}

	@Override
	public String getKey() {
		return "4";
	}

}
