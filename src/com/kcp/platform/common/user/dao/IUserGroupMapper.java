package com.kcp.platform.common.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.entity.UserGroup;
import com.kcp.platform.core.mybatis.annotation.Pageable;

/**
 * <pre>
 * 类描述：用户组DAO映射类
 * </pre>
 * <pre>
 * </pre>
 */
@Repository
public interface IUserGroupMapper {
	/**
	 * 查询用户组列表
	 * @param userGroup
	 * @return
	 */
	@Pageable
	List<UserGroup> queryUserGroupList(UserGroup userGroup);
	
	/**
	 * 查询某个组下的所有用户
	 * @param groupId
	 * @return
	 */
	@Pageable
	List<User> queryUserByUserGroup(@Param("groupId")String groupId);
	
	/**
	 * 查询某个组下的分配用户（包含全部、已分配、未分配）
	 * @param groupId
	 * @return
	 */
	@Pageable
	List<Map<String, Object>> queryAlloteUserByUserGroupMap(Map<String, Object> paramMap);
	
	/**
	 * 编辑用户组
	 * @param userGroup
	 * @return
	 */
	int updateUserGroup(UserGroup userGroup);
		
	/**
	 * 编辑用户组
	 * @param userGroup
	 * @return
	 */
	int updateUserGroupMap(Map<String, Object> paramMap);
	
	
	/**
	 * 新增用户组 
	 * @param userGroup
	 * @return
	 */
	int insertUserGroup(UserGroup userGroup);
	
	/**
	 * 新增用户组 
	 * @param userGroup
	 * @return
	 */
	int insertUserGroupMap(Map<String, Object> paramMap);
	
	/**
	 * 查询用户组（含统计当前用户组）
	 * @param paramMap
	 * @return
	 */
	@Pageable
	List<Map<String, Object>> queryUserGroupByMap(Map<String, Object> paramMap);
	
	/**
	 * 查询用户组（含统计当前用户组）
	 * @param UserGroup
	 * @return
	 */
	List<Map<String, Object>> queryUserGroupByEntity(UserGroup userGroup);
	/**
	 * 查询用户组
	 * @param UserGroup
	 * @return
	 */
	@Pageable
	List<UserGroup> queryUserGroup(Map<String, Object> paramMap);
	
	int deleteUserGroup_R_User(Map<String, Object> paramMap);
	
	int insertUserGroup_R_User(Map<String, Object> paramMap);
}
