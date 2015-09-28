package com.kcp.platform.common.authorization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Repository
public interface IUserFuncMapper {
	
	/**
	 * 根据单位查询角色树，若单位已有该角色，则返回的Map中ISCHECK值为1，否则为0
	 * @param orgId
	 * @return
	 */
	List<Map> queryUserFucList(@Param("userId")String userId);
	
	/**
	 * 删除某组织下的所有角色
	 * @param orgId
	 */
	void deleteUserFuncByUserId(@Param("userId")String userId);
	
	/**
	 * 为组织分配角色
	 * @param orgRole
	 */
	void insertUserFunc(Map userFunc);
	
	/**
	 * 查询某用户
	 * @param funcId
	 * @return
	 */
	List<Map> queryAssignedUserByFunc(@Param("funcId")String funcId);
	
	/**
	 * 删除某功能下的所有用户功能关系
	 * @param orgId
	 */
	void deleteUserFuncByFuncId(@Param("funcId")String funcId);
	
}
