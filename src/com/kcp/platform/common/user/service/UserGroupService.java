package com.kcp.platform.common.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.user.dao.IUserGroupMapper;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.entity.UserGroup;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.DateUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Service
public class UserGroupService extends BaseService {
	@Autowired
	private IUserGroupMapper userGroupMapper;
	
	public List<UserGroup> getAllEnableUserGroup(){
		UserGroup userGroup = new UserGroup();
		userGroup.setJlyxzt(CommonConst.ENABLE_FLAG);
		userGroup.setSybz(CommonConst.ENABLE_FLAG);
		return userGroupMapper.queryUserGroupList(userGroup);
	}
	public List<UserGroup> queryUserGroupList(UserGroup userGroup){
		return userGroupMapper.queryUserGroupList(userGroup);		
	}
	
	public List<User> queryUserByUserGroup(String groupId){
		return userGroupMapper.queryUserByUserGroup(groupId);
	}
	
	public List<UserGroup> queryUserGroup(Map<String, Object> paramMap){
		return userGroupMapper.queryUserGroup(paramMap);
	}
	/**
	 * 查询用户组（含统计当前用户组）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryUserGroupByMap(Map<String, Object> paramMap){
		return userGroupMapper.queryUserGroupByMap( paramMap);
	}
	
	/**
	 * 查询用户组（含统计当前用户组）
	 * @param UserGroup
	 * @return
	 */
	public List<Map<String, Object>> queryUserGroupByEntity(UserGroup userGroup){
		return userGroupMapper.queryUserGroupByEntity(userGroup);
	}
	
	public UserGroup addUserGroup(UserGroup userGroup){
		userGroupMapper.insertUserGroup(userGroup);
		return userGroup;
	}
	
	public Map<String, Object> addUserGroupMap(Map<String, Object> paramMap){
		userGroupMapper.insertUserGroupMap(paramMap);
		return paramMap;
	}
	
	public UserGroup updateUserGroup(UserGroup userGroup){
		userGroupMapper.updateUserGroup(userGroup);
		return userGroup;
	}
	
	public Map<String, Object> updateUserGroupMap(Map<String, Object> paramMap){
		userGroupMapper.updateUserGroupMap(paramMap);
		return paramMap;
	}
	
	public List<Map<String, Object>> queryAlloteUserByUserGroupMap(Map<String, Object> paramMap){
		return userGroupMapper.queryAlloteUserByUserGroupMap(paramMap);		
	}
	
	public void deleteUserGroup_R_User(Map<String, Object> paramMap){
		 userGroupMapper.deleteUserGroup_R_User(paramMap);
	}
	
	public int AddUserGroup_R_User(Map<String, Object> paramMap){
		return userGroupMapper.insertUserGroup_R_User(paramMap);
	}
}
