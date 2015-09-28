package com.kcp.platform.common.authorization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.base.event.EventDispatcher;
import com.kcp.platform.common.authorization.dao.IOrgRoleMapper;
import com.kcp.platform.common.authorization.event.AuthorizationEvent;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.role.entity.Role;
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
public class OrgRoleService extends BaseService {
	@Autowired
	private IOrgRoleMapper orgRoleMapper;
	
	@Autowired
	private OrgService orgService;
	
	/**
	 * 查询单位下的角色树
	 * @param orgId
	 * @return
	 */
	public List<Map> getAssignedRoleList(String orgId){
		String roleLevel = SecurityContext.getRoleLevel();
		return orgRoleMapper.getAssignedRoleList(orgId, roleLevel);
	}
	
	/**
	 * 为单位分配角色
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param orgId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignRoleToOrg(Org org, List<String> roleList){
		if(!orgService.hasPermission(org.getZjid())){
			throw new BusinessException("权限不足：所选单位不是您的所属单位或下属单位！");
		}
		String roleLevel = SecurityContext.getRoleLevel();
		orgRoleMapper.deleteOrgRoleByOrgId(org.getZjid(), roleLevel);
		for(String roleId:roleList){
			Map map = new HashMap();
			map.put("orgId", org.getZjid());
			map.put("roleId", roleId);
			orgRoleMapper.insertOrgFunc(map);
		}
		
		String roleIdStr = StringUtils.join(roleList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为单位分配角色] 为单位[单位名称"+org.getDwmc()+"] [单位编号："+org.getDwbh()+"]分配角色[角色ID:"+roleIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	/**
	 * 查询某角色已分配的单位列表
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param roleId
	 * @return
	 */
	public List<Map> queryAssignedOrgByRole(String roleId){
		return orgRoleMapper.queryAssignedOrgByRole(roleId);
	}
	
	/**
	 * 为角色分配组织
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param roleId
	 * @param orgList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignOrgToRole(Role role, List<String> orgList){
		orgRoleMapper.deleteOrgRoleByRoleId(role.getZjid());
		for(String orgId:orgList){
			Map map = new HashMap();
			map.put("orgId", orgId);
			map.put("roleId", role.getZjid());
			orgRoleMapper.insertOrgFunc(map);
		}
		String orgIdStr = StringUtils.join(orgList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为角色分配单位] 为角色[角色名称："+role.getJsmc()+"] [角色代码："+role.getJsdm()+"]分配单位[单位ID："+orgIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
}
