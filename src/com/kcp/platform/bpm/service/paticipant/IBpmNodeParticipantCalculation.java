package com.kcp.platform.bpm.service.paticipant;

import java.util.List;
import java.util.Set;

import com.kcp.platform.bpm.entity.BpmNodeParticipant;
import com.kcp.platform.bpm.entity.TaskExecutor;
import com.kcp.platform.common.user.entity.User;


public interface IBpmNodeParticipantCalculation {
	
	List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars);
	 
	Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars);
	 
	String getTitle();
	
	String getKey();
}
