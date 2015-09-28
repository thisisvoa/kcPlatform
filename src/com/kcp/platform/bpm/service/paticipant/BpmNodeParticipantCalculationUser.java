package com.kcp.platform.bpm.service.paticipant;

import java.util.ArrayList;
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
 * 分配类型为用户
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationUser implements IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars) {
		List<User> list = new ArrayList<User>();
		String uids = bpmNodeParticipant.getParticipan();
	    if (StringUtils.isNotEmpty(uids)) {
	    	String[] uIds = bpmNodeParticipant.getParticipan().split(",");
	    	List<User> userList = userService.getUserListByIds(uIds);
	    	list = (userList==null)?list:userList;
	    	
	    }
		return list;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		List<User> userList = getExecutor(bpmNodeParticipant, vars);
		return TaskExecutor.getExcutorsByUsers(userList);
	}

	@Override
	public String getTitle() {
		return "用户";
	}

	@Override
	public String getKey() {
		return "2";
	}

}
