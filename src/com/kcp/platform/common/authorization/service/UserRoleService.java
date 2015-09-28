package com.kcp.platform.common.authorization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.base.event.EventDispatcher;
import com.kcp.platform.common.authorization.dao.IUserRoleMapper;
import com.kcp.platform.common.authorization.event.AuthorizationEvent;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.role.service.RoleService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.security.context.SecurityContext;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Service
public class UserRoleService extends BaseService {
	@Autowired
	private IUserRoleMapper userRoleMapper;
	
	
	@Autowired
	private OrgService orgService;
	
	/**
	 * 根据用户查询角色树，若用户已有该角色，则返回的Map中ISCHECK值为1，否则为0
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> getAssignedRoleList(String userId){
		String roleLevel = SecurityContext.getRoleLevel();
		return userRoleMapper.getAssignedRoleList(userId, roleLevel);
	}
	
	/**
	 * 为用户分配功能
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param userId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignRoleToUser(User user, List<String> roleList){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：所选用户不属于您的所属单位或下属单位！");
		}
		String roleLevel = SecurityContext.getRoleLevel();
		userRoleMapper.deleteUserRoleByUserId(user.getZjid(), roleLevel);
		for(String roleId:roleList){
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", user.getZjid());
			map.put("roleId", roleId);
			userRoleMapper.insertUserRole(map);
		}
		String roleIdStr = StringUtils.join(roleList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为用户分配角色] 为用户[用户名称："+user.getYhmc()+"] [警员编号："+user.getJybh()+"] [身份证号："+user.getSfzh()+"]分配角色[角色ID："+roleIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	
	/**
	 * 为角色分配用户
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param roleId
	 * @param userList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignUserToRole(Role role,List<String> userList){
		String orgId = orgService.getPermissionRootOrgId();
		userRoleMapper.deleteUserRoleByRoleId(role.getZjid(), orgId);
		for(String userId:userList){
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", userId);
			map.put("roleId", role.getZjid());
			userRoleMapper.insertUserRole(map);
		}
		String userIdStr = StringUtils.join(userList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为角色分配用户] 为角色[角色名称："+role.getJsmc()+"] [角色代码："+role.getJsdm()+"]分配用户[用户ID："+userIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	
	/**
	 * 查询分配了某角色的用户列表,只获取登录用户所属单位下且用户级别小于当前登录用户的已分配用户
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param roleId
	 * @return
	 */
	public List<Map> queryAssignedUserByRole(String roleId){
		String orgId = orgService.getPermissionRootOrgId();
		return userRoleMapper.queryAssignedUserByRole(roleId, orgId);
	}
}
