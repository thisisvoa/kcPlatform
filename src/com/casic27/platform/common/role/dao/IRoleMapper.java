/**
 * @(#)com.casic27.platform.common.role.dao.IRoleMapper.java
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
package com.casic27.platform.common.role.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.mybatis.annotation.Pageable;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Repository
public interface IRoleMapper {
	/**
	 * 查找角色(不分页)
	 * @param role
	 * @return
	 */
	
	List<Role> findRole(Map<String,Object> queryMap);
	
	/**
	 * 查找角色(分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	@OperateLog(isRecord = false)
	List<Role> findRolePaging(Map<String,Object> queryMap);
	
	/**
	 * 根绝角色ID获取角色
	 * @return
	 */
	@OperateLog(isRecord = false)
	Role getRoleById(@Param("roleId")String roleId);
	
	/**
	 * 修改角色信息(参数为com.casic27.platform.common.role.entity.Role)
	 * @param role
	 * @return
	 */
	@OperateLog(isRecord = false)
	public void updateRole(Role role);
	
	/**
	 * 更新使用标志
	 * @param role
	 */
	@OperateLog(isRecord = false)
	public void updateRoleSybz(Role role);
	
	/**
	 * 修改应用使用标志
	 * @param role
	 * @return
	 */
	@OperateLog(isRecord = false)
	public void updateRoleYxzt(Role role);
	
	/**
	 * 新增角色信息
	 * @param role
	 * @return
	 */
	@OperateLog(isRecord = false)
	public int addRole(Role role);
	
	/**
	 * 统计角色个数
	 * @param role
	 * @return
	 */
	int staticsRole(Role role);
	
	public List<Role> getRoleByIds(String[] ids);
	
}
