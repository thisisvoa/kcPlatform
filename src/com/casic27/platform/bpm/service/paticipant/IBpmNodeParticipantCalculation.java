package com.casic27.platform.bpm.service.paticipant;

import java.util.List;
import java.util.Set;

import com.casic27.platform.bpm.entity.BpmNodeParticipant;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.common.user.entity.User;


public interface IBpmNodeParticipantCalculation {
	
	List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars);
	 
	Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars);
	 
	String getTitle();
	
	String getKey();
}
