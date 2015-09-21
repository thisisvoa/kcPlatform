/**
 * @(#)com.casic27.platform.common.authorization.dao.IAuthoriztationMapper.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-10
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.func.entity.Function;
import com.casic27.platform.common.menu.entity.Menu;
import com.casic27.platform.common.role.entity.Role;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Repository
public interface IAuthoriztationMapper {
	/**
	 * 获取用户拥有的角色列表
	 * @param userId
	 * @return
	 */
	List<Role> getAssignedRoleList(@Param("userId")String userId);
	
	/**
	 * 获取用户分配的功能列表
	 * @param userId
	 * @return
	 */
	List<Function> getAssignedFuncList(@Param("userId")String userId);
	
	/**
	 * 获取用户分配的菜单列表
	 * @param userId
	 * @return
	 */
	List<Menu> getAssignedMenuList(@Param("userId")String userId);
}
