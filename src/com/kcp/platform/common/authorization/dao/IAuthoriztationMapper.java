package com.kcp.platform.common.authorization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.common.role.entity.Role;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@kcp.com)
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
