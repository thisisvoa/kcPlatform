package com.kcp.platform.common.user.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.user.dao.IUserMapper;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.CipherUtil;
import com.kcp.platform.util.DateUtils;

/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
@Service("userService")
public class UserService extends BaseService {
	@Autowired
	private IUserMapper iUserMapper;
	
	@Autowired
	private OrgService orgService;
	/**
	 * 根据单位Id查询该单位下的用户
	 * @param orgId
	 * @return
	 */
	public List<User> queryUserByOrgId(String orgId){
		User user = new User();
		user.setSsdw_zjid(orgId);
		user.setSybz(CommonConst.ENABLE_FLAG);
		user.setJlyxzt(CommonConst.ENABLE_FLAG);//默认查询有效
		return iUserMapper.queryUserList(user);
	}
	
	/**
	 * 根据单位Id查询该单位和它所有子单位下的用户
	 * @param orgId
	 * @return
	 */
	@Log(type=OperateLogType.QUERY, moduleName="用户信息管理", operateDesc="[查询] 查询用户信息", useSpel=false)
	public List<Map<String, Object>> queryCurAndChildOrgUserList(User user){
		return iUserMapper.queryCurAndChildOrgUserList(user);
	}
	
	/**
	 * 分页查找用户
	 * @return
	 */
	public List<Map<String,Object>> findUserByOrg(Map<String,Object> queryMap){
		return iUserMapper.findUserByOrg(queryMap);
	}
	/**
	 * 根据ID查询用户信息
	 * @param orgId
	 * @return
	 */
	public User getUserById(String userId){
		return iUserMapper.getUserInfoById(userId);
	}
	
	/**
	 * 根据ID查询用户信息
	 * @param orgId
	 * @return
	 */
	public Map<String, Object>  getUserInfoMapById(String orgId){
		Map<String, Object>  userInfo= iUserMapper.getUserInfoMapById(orgId);
		return userInfo;
	}
	
	/**
	 * 根据用户登录账号查询用户信息
	 * @param yhdlzh
	 * @return
	 */
	public User queryUserByYhdlzh(String yhdlzh){
		return iUserMapper.queryUserByYhdlzh(yhdlzh);
	}
	
	@Log(type=OperateLogType.INSERT, moduleName="用户信息管理", operateDesc="'[新增] 新增[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号：'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户信息'")
	public User addUser(User user){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setCjyh(SecurityContext.getCurrentUser().getZjid());
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		String date = DateUtils.getCurrentDateTime14();
		user.setJlxzsj(date);
		user.setJlxgsj(date);
		user.setJlscsj("");
		iUserMapper.insertUser(user);
		return user;
	}
	
	
	/**
	 * 修改用户使用标识
	 * @param id
	 */
	public void updateUserTarg(String id){
		User old = getUserById(id);
		if(!orgService.hasPermission(old.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		User  user = new User();
		user.setZjid(id);
		user.setSybz(CommonConst.DISABLE_FLAG);
		user.setJlscsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUser(user);	
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	@Log(type=OperateLogType.UPDATE, moduleName="用户信息管理", operateDesc="'[修改] 修改[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号：'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户信息'")
	public User updateUser(User user){
		User old = getUserById(user.getZjid());
		if(!orgService.hasPermission(old.getSsdw_zjid()) || !orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		user.setJlxgsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUser(user);
		return user;
	}
	
	/**
	 * 逻辑删除用户
	 * @param orgId
	 */
	@Log(type=OperateLogType.DELETE, moduleName="用户信息管理", operateDesc="'[删除] 删除[用户名称：'+#user.yhmc+'] [警员编号:'+#user.jybh+'] [登录账号：'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户信息'")
	public void logicDelUser(User user){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setJlyxzt(CommonConst.DISABLE_FLAG);
		user.setJlscsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUserJlyxzt(user);
	}
	
	
	/**
	 * 启用用户
	 * @param id
	 */
	@Log(type=OperateLogType.UPDATE, moduleName="用户信息管理", operateDesc="'[启用] 启用[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号:'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户信息'")
	public void enabledUser(User user){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setSybz(CommonConst.ENABLE_FLAG);
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		user.setJlxgsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUserSybz(user);
	}	
	/**
	 * 禁用用户
	 * @param id
	 */
	@Log(type=OperateLogType.UPDATE, moduleName="用户信息管理", operateDesc="'[禁用] 禁用[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号：'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户信息'")
	public void forbidUser(User user){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setSybz(CommonConst.DISABLE_FLAG);
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		user.setJlxgsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUserSybz(user);
	}
	/**
	 * 重置密码
	 * @param id
	 */
	@Log(type=OperateLogType.UPDATE, moduleName="用户信息管理", operateDesc="'[密码重置] 重置[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号：'+#user.yhdlzh+'] [身份证号：'+#user.sfzh+']的用户密码'")
	public void resetPsw(User user){
		if(!orgService.hasPermission(user.getSsdw_zjid())){
			throw new BusinessException("权限不足：用户所属单位不是您的所属单位或下属单位！");
		}
		user.setYhdlmm(CipherUtil.generatePassword(CommonConst.INIT_PASSWORD));
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		user.setJlxgsj(DateUtils.getCurrentDateTime14());
		iUserMapper.resetPsw(user);
	}
	
	/**
	 * 调动用户部门:修改用户部门信息之前先删除用户与角色关联信息、用户与功能关联信息、
	 * 		用户与应用系统关联信息、用户与用户组关联信息
	 * @param id
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.UPDATE, moduleName="用户信息管理", operateDesc="'[用户调动] 调动[用户名称：'+#user.yhmc+'] [警员编号：'+#user.jybh+'] [登录账号:'+#user.yhdlzh+'] 身份证号['+#user.sfzh+']的用户到新单位[单位ID：'+#orgId+']'")
	public void transferUserOrg(User user, String orgId){
		if(!orgService.hasPermission(user.getSsdw_zjid()) || !orgService.hasPermission(orgId)){
			throw new BusinessException("权限不足：用户所属单位或调动后的单位不是您的所属单位或下属单位！");
		}
		user.setSsdw_zjid(orgId);
		user.setGxyh(SecurityContext.getCurrentUser().getZjid());
		user.setJlxgsj(DateUtils.getCurrentDateTime14());
		iUserMapper.updateUserSsdw(user);
	}
	
	/**
	 * 验证用户属性的唯一性
	 * @param user
	 * @return
	 */
	public boolean validateUniqueUser(User user){		
		int count = iUserMapper.staticsUser(user);
		if(count > 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取用户权限信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserPermission(String userId){
		return iUserMapper.getUserPermission(userId);
	}
	
	public List<Map<String, Object>> getAllPermission(){
		return iUserMapper.getAllPermission();
	}
	
	public int getOrgUserCount(String orgId){
		return iUserMapper.getOrgUserCount(orgId);
	}
	
	public List<Map<String, Object>> getUserByIds(String[] ids){
		return iUserMapper.getUserByIds(ids);
	}
	
	public List<Map<String,Object>> findUserByRole(String roleId){
		User currentUser = SecurityContext.getCurrentUser();
		String rootOrgId = null;
		if(!currentUser.isSuperAdmin()){
			rootOrgId = orgService.getPermissionRootOrgId();
		}
		return iUserMapper.findUserByRole(roleId, rootOrgId);
	}
	
	/**
	 * 批量查找用户
	 * @param ids
	 * @return
	 */
	public List<User> getUserListByIds(String[] ids){
		return iUserMapper.getUserListByIds(ids);
	}
	/**
	 * 获取单位下的用户ID
	 * @param orgIds
	 * @return
	 */
	public List<User> getUserByOrgIds(String[] orgIds){
		return iUserMapper.getUserByOrgIds(orgIds);
	}
	
	/**
	 * 根据单位ID查询用户
	 * @param orgId
	 * @return
	 */
	public List<User> getUserByOrgId(String orgId){
		return iUserMapper.getUserByOrgId(orgId);
	}
	
	/**
	 * 根据单位ID角色ID查询用户
	 * @param orgId
	 * @return
	 */
	public List<User> getUserByOrgIdRoleId(String orgId, String roleId){
		return iUserMapper.getUserByOrgIdRoleId(orgId, roleId);
	}
	
	/**
	 * 根据角色ID查询用户
	 * @param roleId
	 * @return
	 */
	public List<User> getUserByRoleId(String roleId){
		return iUserMapper.getUserByRoleId(roleId);
	}
	
	/**
	 * 批量角色查询用户
	 * @param roleIds
	 * @return
	 */
	public List<User> getUserByRoleIds(String[] roleIds){
		return iUserMapper.getUserByRoleIds(roleIds);
	}
	
	/**
	 * 根据用户身份证号查询用户
	 * @param sfzh
	 * @return
	 */
	public User queryUserBySfzh(@Param("sfzh")String sfzh)
	{
		return iUserMapper.queryUserBySfzh(sfzh);
	}
 }
