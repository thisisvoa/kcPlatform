package com.kcp.platform.bpm.service;

import org.springframework.stereotype.Component;

import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.engine.IScript;
import com.kcp.platform.sys.security.context.SecurityContext;

@Component("bpmScript")
public class BpmScript implements IScript {
	public User getCurrentUser(){
		return SecurityContext.getCurrentUser();
	}
	
	public String getCurrentUserId(){
		return getCurrentUser().getZjid();
	}
	public String getCurrentName(){
		 return getCurrentUser().getYhmc();
	}
	 
	public Org getCurrentUserOrg(){
		return SecurityContext.getCurrentUserOrg();
	}
	 
	public String getCurrentUserOrgName(){
		return SecurityContext.getCurrentUserOrg().getDwmc();
	}
	 
	public String getCurrentUserOrgNo(){
		return SecurityContext.getCurrentUserOrg().getDwbh();
	}
}
