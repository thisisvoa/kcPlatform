package com.casic27.platform.bpm.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.bpm.dao.ITaskUserMapper;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.entity.TaskUser;
import com.casic27.platform.common.org.dao.IOrgMapper;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.role.dao.IRoleMapper;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.user.dao.IUserMapper;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.util.BeanUtils;

@Service
public class TaskUserService extends BaseService{
	@Autowired
	private ITaskUserMapper taskUserMapper;

	@Autowired
	private IUserMapper userMapper;

	@Autowired
	private IOrgMapper orgMapper;

	@Autowired
	private IRoleMapper roleMapper;


	public List<TaskUser> getByTaskId(String taskId) {
		return this.taskUserMapper.getByTaskId(taskId);
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Set<TaskExecutor> getCandidateExecutors(String taskId) {
		Set<TaskExecutor> taskUserSet = new HashSet<TaskExecutor>();
		List<TaskUser> taskUsers = getByTaskId(taskId);
		for (TaskUser taskUser : taskUsers) {
			if (taskUser.getUserId() != null) {
				User sysUser = this.userMapper.getUserInfoById(taskUser.getUserId());
				if (BeanUtils.isNotEmpty(sysUser))
					taskUserSet.add(TaskExecutor.getTaskUser(taskUser.getUserId(), sysUser.getYhmc()));
			} else if (taskUser.getGroupId() != null) {
				String tmpId = taskUser.getGroupId();
				if ("org".equals(taskUser.getType())) {
					Org org = this.orgMapper.getOrgById(tmpId);
					taskUserSet.add(TaskExecutor.getTaskOrg(tmpId, org.getDwmc()));
				} else if ("role".equals(taskUser.getType())) {
					Role role = roleMapper.getRoleById(tmpId);
					taskUserSet.add(TaskExecutor.getTaskRole(tmpId, role.getJsmc()));
				}
			}
		}
		return taskUserSet;
	}

	public Set<User> getCandidateUsers(String taskId) {
		Set<User> taskUserSet = new HashSet<User>();
		List<TaskUser> taskUsers = getByTaskId(taskId);
		for (TaskUser taskUser : taskUsers) {
			if (taskUser.getUserId() != null) {
				User sysUser = this.userMapper.getUserInfoById(taskUser.getUserId());
				taskUserSet.add(sysUser);
			} else if (taskUser.getGroupId() != null) {
				String tmpId = taskUser.getGroupId();
				if ("org".equals(taskUser.getType())) {
					List<User> userList = this.userMapper.getUserByOrgId(tmpId);
					taskUserSet.addAll(userList);
				} else if ("role".equals(taskUser.getType())) {
					List<User> userList = this.userMapper.getUserByRoleId(tmpId);
					taskUserSet.addAll(userList);
				}
			}
		}
		return taskUserSet;
	}
}
