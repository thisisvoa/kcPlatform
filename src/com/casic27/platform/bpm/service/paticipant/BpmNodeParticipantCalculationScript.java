package com.casic27.platform.bpm.service.paticipant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.bpm.entity.BpmNodeParticipant;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.engine.GroovyScriptEngine;
import com.casic27.platform.util.BeanUtils;

/**
 * 流程人员分配类型为脚本
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationScript implements IBpmNodeParticipantCalculation {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroovyScriptEngine groovyScriptEngine;
	
	
	@Override
	public List<User> getExecutor(BpmNodeParticipant bpmNodeParticipant, CalcVars vars) {
		String prevUserId = vars.getPrevExecUserId();
		String startUserId = vars.getStartUserId();
		String script = bpmNodeParticipant.getParticipanDesc();
		List<User> users = new ArrayList<User>();
		Map<String,Object> grooVars = new HashMap<String,Object>();
		grooVars.put("prevUser", prevUserId);
		grooVars.put("startUser", startUserId.toString());
		if (vars.getVars().size() > 0) {
			grooVars.putAll(vars.getVars());
		}
		Object result = this.groovyScriptEngine.executeObject(script, grooVars);
		if (BeanUtils.isEmpty(result)){
			return users;
		}
	     Set<String> set = (Set<String>)result;
	     for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
	    	 String userId = (String)it.next();
	    	 User user = this.userService.getUserById(userId);
	    	 if(user!=null) users.add(user);
	     }
	     return users;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(
			BpmNodeParticipant bpmNodeParticipant, CalcVars paramCalcVars) {
		List<User> list = getExecutor(bpmNodeParticipant, paramCalcVars);
		return TaskExecutor.getExcutorsByUsers(list);
	}

	@Override
	public String getTitle() {
		return "脚本";
	}

	@Override
	public String getKey() {
		return "20";
	}

}
