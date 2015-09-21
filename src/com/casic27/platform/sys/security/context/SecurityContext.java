/**
 * @(#)com.casic27.platform.sys.security.context.SecurityContext.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-9
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.sys.security.context;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class SecurityContext extends SecurityUtils {
	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static User getCurrentUser(){
		try {
			Session session = getSubject().getSession();
			return (User)session.getAttribute(CommonConst.SESSION_USER_KEY);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取当前登录用户ID
	 * @return
	 */
	public static String getCurrentUserId(){
		User user = getCurrentUser();
		if(user!=null){
			return user.getZjid();
		}
		return "";
	}
	
	/**
	 * 设置当前登录用户
	 * @param user
	 */
	public static void setCurrentUser(User user){
		Session session = getSubject().getSession();
		session.setAttribute(CommonConst.SESSION_USER_KEY, user);
	}
	
	/**
	 * 获取当前登录用户所属单位
	 * @return
	 */
	public static Org getCurrentUserOrg(){
		try {
			Session session = getSubject().getSession();
			return (Org)session.getAttribute(CommonConst.SESSION_ORG_KEY);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取当前登录用户ID
	 * @return
	 */
	public static String getCurrentOrgId(){
		Org org = getCurrentUserOrg();
		if(org!=null){
			return org.getZjid();
		}
		return "";
	}
	
	/**
	 * 设置当前登录部门信息
	 * @param user
	 */
	public static void setCurrentUserOrg(Org org){
		Session session = getSubject().getSession();
		session.setAttribute(CommonConst.SESSION_ORG_KEY, org);
	}
	
	/**
	 * 设置当前客户端IP
	 * @param ip
	 */
	public static void setCurrentRemoteIp(String ip){
		Session session = getSubject().getSession();
		session.setAttribute(CommonConst.SESSION_REMOTE_IP_KEY, ip);
	}
	
	/**
	 * 获取当前登录IP
	 * @return
	 */
	public static String getCurrentRemoteIp(){
		try {
			Session session = getSubject().getSession();
			return (String)session.getAttribute(CommonConst.SESSION_REMOTE_IP_KEY);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 设置当前用户角色
	 * @param roleList
	 */
	public static void setCurrentRoleList(List<Role> roleList){
		Session session = getSubject().getSession();
		session.setAttribute(CommonConst.SESSION_ROLE_LIST_KEY, roleList);
	}
	
	/**
	 * 获取当前用户角色
	 * @return
	 */
	public static List<Role> getCurrentRoleList(){
		try {
			Session session = getSubject().getSession();
			return (List<Role>)session.getAttribute(CommonConst.SESSION_ROLE_LIST_KEY);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 设置当前SessionId
	 * @param ip
	 */
	public static void setSessionId(String sessionId){
		Session session = getSubject().getSession();
		session.setAttribute(CommonConst.SESSION_ID, sessionId);
	}
	
	/**
	 * 获取当前SessionId
	 * @return
	 */
	public static String getSessionId(){
		try {
			Session session = getSubject().getSession();
			return (String)session.getAttribute(CommonConst.SESSION_ID);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getRoleLevel(){
		User user = SecurityContext.getCurrentUser();
		String roleLevel = "";
		if(user.isSuperAdmin()){
			roleLevel = "1";
		}else{
			List<Role> roleList = SecurityContext.getCurrentRoleList();
			if(roleList!=null){
				for(Role role:roleList){
					if(StringUtils.isEmpty(roleLevel)){
						roleLevel = role.getJsjb();
					}else{
						if(role.getJsjb().compareTo(roleLevel)<0){
							roleLevel = role.getJsjb();
						}
					}
					
				}
			}
		}
		return roleLevel;
	}
}
