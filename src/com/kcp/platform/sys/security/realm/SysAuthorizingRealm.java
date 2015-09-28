package com.kcp.platform.sys.security.realm;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.AllPermission;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.kcp.platform.base.event.BaseEvent;
import com.kcp.platform.base.event.BaseEventListener;
import com.kcp.platform.common.authorization.event.AuthorizationEvent;
import com.kcp.platform.common.authorization.service.AuthoriztationService;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.entity.LogonLog;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;

/**
 * <pre>
 * 类描述：获取权限数据,实现BaseEventListener接口，用于在监听到权限分配事件时候刷新缓存
 * </pre>
 * <pre>
 * </pre>
 */
public class SysAuthorizingRealm extends AuthorizingRealm implements BaseEventListener{
	@Autowired
	private AuthoriztationService authoriztationService;
	
	/**
	 * 认证，登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = authoriztationService.getUserByUserName(token.getUsername());
		if (user != null) {
			//获取用户所属部门
    		Org org = authoriztationService.getOrgByUser(user);
    		LogonLog logonLog = genBasicLogonLog(user, org);
        	if (new String(token.getPassword()).equals(user.getYhdlmm())) {
        		//用户被禁用
        		if(user.getSybz().equals(CommonConst.DISABLE_FLAG)
        				||user.getJlyxzt().equals(CommonConst.DISABLE_FLAG)){
        			logonLog.setLogonResult(CommonConst.NO);
        			throw new LockedAccountException();
        		}
        		
        		List<Role> roleList = authoriztationService.getAssignedRoleList(user);
        		//将用户及部门放入Session中
        		SecurityContext.setCurrentUser(user);
        		SecurityContext.setCurrentUserOrg(org);
        		SecurityContext.setCurrentRoleList(roleList);
        		logonLog.setLogonResult(CommonConst.YES);
        		Logger.getInstance().addLogonLog(logonLog);
        		return new SimpleAuthenticationInfo(user.getYhdlzh(), user.getYhdlmm(), getName());
        	}else{
        		logonLog.setLogonResult(CommonConst.NO);
        		Logger.getInstance().addLogonLog(logonLog);
        		throw new IncorrectCredentialsException();
        	}
        } else {
            throw new UnknownAccountException();
        }
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pricPrincipalCollection) {
		User user = SecurityContext.getCurrentUser();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(user.isSuperAdmin()){
			Permission permission = new AllPermission();
			info.addObjectPermission(permission);
		}else{
			//菜单权限
			List<Menu> menuList = authoriztationService.getAssignedMenuList(user);
			for(Menu menu:menuList){
				if("1".equals(menu.getSfzhyicd())){
					info.addStringPermission("MENU_"+menu.getZjId());
				}
			}
			
			//按钮权限
			List<Function> funcList = authoriztationService.getAssignedFuncList(user);
			for (Function func : funcList) {
	            info.addStringPermission(func.getGndm());
	        }
		}
		
		return info;
	}
	
	private LogonLog genBasicLogonLog(User user, Org org){
		LogonLog logonLog = new LogonLog();
		logonLog.setIdCard(user.getSfzh());
		logonLog.setPoliceId(user.getJybh());
		logonLog.setUserId(user.getZjid());
		logonLog.setUserName(user.getYhmc());
		logonLog.setLoginId(user.getYhdlzh());
		logonLog.setOrgId(org.getZjid());
		logonLog.setOrgName(org.getDwmc());
		logonLog.setOrgNo(org.getDwbh());
		logonLog.setTerminalId(SecurityContext.getCurrentRemoteIp());
		logonLog.setSessionId(SecurityContext.getSessionId());
		logonLog.setLogonTime(new Date());
		return logonLog;
	}
	
	/**
	 * 清除所有权限缓存
	 */
	protected void flushAllCache(){
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
	}
	/**
	 * 事件处理的方法
	 * @param event
	 */
	public void onEvent(BaseEvent event){
		flushAllCache();
	}
	
	/**
	 * 所要处理的事件类型
	 * @return
	 */
	public List<Class<? extends BaseEvent>> getEventClasses(){
		List<Class<? extends BaseEvent>> events = new ArrayList<Class<? extends BaseEvent>>();
		events.add(AuthorizationEvent.class);
		return events;
	}
	
}
