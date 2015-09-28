package com.kcp.platform.common.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.mybatis.annotation.Pageable;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Repository
public interface IUserMapper {
	/**
	 * 查询用户（含模糊查询）
	 * @param user
	 * @return
	 */
	List<User> queryUserList(User user);
	
	/**
	 * 根据单位Id查询该单位及所有子单位下的所有用户
	 * @param user
	 * @return
	 */
	@Pageable
	@OperateLog(isRecord = false)
	List<Map<String, Object>> queryCurAndChildOrgUserList(User user);
	
	/**
	 * 查找单位下的直属用户
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<Map<String,Object>> findUserByOrg(Map<String,Object> queryMap);
	
	/**
	 * 根据用户登录账号查询用户
	 * @param yhdlzh
	 * @return
	 */
	User queryUserByYhdlzh(@Param("yhdlzh")String yhdlzh);
	
	/**
	 * 修改用户信息(参数为com.kcp.platform.common.user.entity.User)
	 * @param user
	 * @return
	 */
	@OperateLog(isRecord = false)
	public void updateUser(User user);
	
	
	/**
	 * 更新使用标志
	 * @param user
	 */
	@OperateLog(isRecord = false)
	public void updateUserSybz(User user);
	
	/**
	 * 更新记录有效标志
	 * @param user
	 */
	@OperateLog(isRecord = false)
	public void updateUserJlyxzt(User user);
	
	/**
	 * 修改所属单位
	 * @param user
	 */
	@OperateLog(isRecord = false)
	public void updateUserSsdw(User user);
	
	/**
	 * 更新密码
	 * @param user
	 */
	@OperateLog(isRecord = false)
	public void resetPsw(User user);
	
	/**
	 * 根据ID查询用户信息
	 * @param userId
	 * @return
	 */
	public User getUserInfoById(@Param("userId")String userId);
	
	/**
	 * 根据ID查询用户信息
	 * @param orgId
	 * @return
	 */
	public Map<String, Object>  getUserInfoMapById(String orgId);
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	@OperateLog(isRecord = false)
	public int insertUser(User user);

	/**
	 * 统计用户
	 * @param user
	 * @return
	 */
	int staticsUser(User user);	
	
	List<Map<String, Object>> getUserPermission(@Param("userId")String userId);
	
	List<Map<String,Object>> getAllPermission();
	
	int getOrgUserCount(@Param("orgId")String orgId);
	
	public List<Map<String, Object>> getUserByIds(String[] ids);
	
	@Pageable
	public List<Map<String, Object>> findUserByRole(@Param("roleId")String roleId, @Param("rootOrgId")String rootOrgId);
	
	/**
	 * 批量查找用户
	 * @param ids
	 * @return
	 */
	List<User> getUserListByIds(String[] ids);
	/**
	 * 根据单位ID查找用户
	 * @param orgIds
	 * @return
	 */
	List<User> getUserByOrgIds(String[] orgIds);
	
	/**
	 * 根据单位ID查找用户
	 * @param orgId
	 * @return
	 */
	
	List<User> getUserByOrgId(@Param("orgId")String orgId);
	
	
	/**
	 * 根据单位ID，角色ID查找用户
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	List<User> getUserByOrgIdRoleId(@Param("orgId")String orgId, @Param("roleId")String roleId);
	
	/**
	 * 根据角色ID查找用户
	 * @param roleId
	 * @return
	 */
	List<User> getUserByRoleId(@Param("roleId")String roleId);
	
	/**
	 * 批量根据角色查询用户
	 * @param roleIds
	 * @return
	 */
	List<User> getUserByRoleIds(String[] roleIds);
	
	/**
	 * 根据用户身份证号查询用户
	 * @param sfzh
	 * @return
	 */
	User queryUserBySfzh(@Param("sfzh")String sfzh);
}
