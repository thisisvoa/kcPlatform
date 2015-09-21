/**
 * @(#)com.casic27.platform.common.role.service.RoleService.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-21
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.role.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.base.cache.annotation.CacheEvict;
import com.casic27.platform.common.log.annotation.Log;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.role.dao.IRoleMapper;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.DateUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Service("roleService")
public class RoleService extends BaseService {
	
	@Autowired
	private IRoleMapper roleMapper;
	
	/**
	 * 根据角色Id查找角色
	 * @param roleId
	 * @return
	 */
	public Role getRoleById(String roleId){
		return roleMapper.getRoleById(roleId);
	}
	
	/**
	 * 逻辑删除角色
	 * @param orgId
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.DELETE, moduleName="角色信息管理", operateDesc="'[删除] 删除[角色名称：'+#role.jsmc+'] [角色代码：'+#role.jsdm+']的角色信息'")
	public void logicDelRole(Role role){
		role.setJlyxzt(CommonConst.DISABLE_FLAG);
		role.setJlscsj(DateUtils.getCurrentDateTime14());
		role.setGxyh(SecurityContext.getCurrentUser().getZjid());
		roleMapper.updateRoleYxzt(role);
	}
	
	
	/**
	 * 启用角色
	 * @param id
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.UPDATE, moduleName="角色信息管理", operateDesc="'[启用] 启用[角色名称：'+#role.jsmc+'] [角色代码：'+#role.jsdm+']的角色信息'")
	public void enabledRole(Role role){		
		role.setSybz(CommonConst.ENABLE_FLAG);
		role.setJlxgsj(DateUtils.getCurrentDateTime14());
		role.setGxyh(SecurityContext.getCurrentUser().getZjid());
		roleMapper.updateRoleSybz(role);
	}	
	/**
	 * 禁用角色
	 * @param id
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.UPDATE, moduleName="角色信息管理", operateDesc="'[禁用] 禁用[角色名称：'+#role.jsmc+'] [角色代码：'+#role.jsdm+']的角色信息'")
	public void forbidRole(Role role){
		role.setSybz(CommonConst.DISABLE_FLAG);
		role.setGxyh(SecurityContext.getCurrentUser().getZjid());
		role.setJlxgsj(DateUtils.getCurrentDateTime14());
		roleMapper.updateRoleSybz(role);
		
	}
	/**
	 * 新增角色
	 * @param paramMap
	 */
	@Log(type=OperateLogType.INSERT, moduleName="角色信息管理", operateDesc="'[新增] 新增[角色名称：'+#role.jsmc+'] [角色代码：'+#role.jsdm+']的角色信息'")
	public void addRole(Role role){
		if(!validateUniqueRole(role)){
			throw new BusinessException("角色代码已存在!");
		}
		String date = DateUtils.getCurrentDateTime14();
		role.setJlxzsj(date);
		role.setJlxgsj(date);
		role.setJlyxzt(CommonConst.YES);
		role.setSybz(CommonConst.YES);
		role.setCjyh(SecurityContext.getCurrentUser().getZjid());
		role.setGxyh(SecurityContext.getCurrentUser().getZjid());
		roleMapper.addRole(role);
	}
	
	@Log(type=OperateLogType.UPDATE, moduleName="角色信息管理", operateDesc="'[修改] 修改[角色名称：'+#role.jsmc+'] [角色代码：'+#role.jsdm+']的角色信息'")
	public void updateRole(Role role){
		role.setGxyh(SecurityContext.getCurrentUser().getZjid());
		role.setJlxgsj(DateUtils.getCurrentDateTime14());
		roleMapper.updateRole(role);
	}
	
	/**
	 * 查询角色
	 * @param queryMap
	 * @return
	 */
	@Log(type=OperateLogType.QUERY, moduleName="角色信息管理", operateDesc="[查询] 查询角色信息", useSpel=false)
	public List<Role> findRolePaging(Map<String, Object> queryMap){
		return roleMapper.findRolePaging(queryMap);
	}
	
	
	public boolean validateUniqueRole(Role role){
		int count = roleMapper.staticsRole(role);
		if(count > 0){
			return false;
		}else{
			return true;
		}
	}
	
	
	public List<Role> getRoleByIds(String[] ids){
		return roleMapper.getRoleByIds(ids);
	}
}
