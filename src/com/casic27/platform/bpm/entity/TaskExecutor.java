package com.casic27.platform.bpm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.UnsatisfiedDependencyException;

import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.util.BeanUtils;

public class TaskExecutor implements Serializable {
	public static final int EXACT_NOEXACT = 0;
	public static final int EXACT_EXACT_USER = 1;
	public static final int EXACT_EXACT_SECOND = 2;
	public static final int EXACT_USER_GROUP = 3;
	private static final long serialVersionUID = 10001L;
	public static final String USER_TYPE_USER = "user";
	public static final String USER_TYPE_ORG = "org";
	public static final String USER_TYPE_ROLE = "role";
	/**
	 * 类型
	 */
	private	String type = "user";
	/**
	 * 
	 */
	public String mainOrgName;
	
	/**
	 * 主键ID
	 */
	private String executeId = "";

	/**
	 * 
	 */
	private String executor = "";

	/**
	 * 
	 */
	private int exactType = 0;

	public TaskExecutor() {
	}

	public TaskExecutor(String executeId) {
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = userService.getUserById(executeId);
		this.executeId = executeId;
		this.executor = user.getZjid();
	}

	public TaskExecutor(String type, String executeId, String name) {
		this.type = type;
		this.executeId = executeId;
		this.executor = name;
	}

	public static TaskExecutor getTaskUser(String executeId, String name) {
		return new TaskExecutor("user", executeId, name);
	}
	
	public static Set<TaskExecutor> getExcutorsByUsers(List<User> userList){
		Set<TaskExecutor> set = new LinkedHashSet<TaskExecutor>();
		if(BeanUtils.isEmpty(userList)){
			return set;
		}
		for (User user : userList) {
			set.add(TaskExecutor.getTaskUser(user.getZjid(), user.getYhmc()));
		}
		return set;
	}
	
	public static TaskExecutor getTaskOrg(String executeId, String name) {
		return new TaskExecutor("org", executeId, name);
	}

	public static TaskExecutor getTaskRole(String executeId, String name) {
		return new TaskExecutor("role", executeId, name);
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecuteId() {
		return this.executeId;
	}

	public void setExecuteId(String executeId) {
		this.executeId = executeId;
	}

	public String getExecutor() {
		return this.executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public int getExactType() {
		return this.exactType;
	}

	public void setExactType(int exactType) {
		this.exactType = exactType;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TaskExecutor)) {
			return false;
		}
		TaskExecutor tmp = (TaskExecutor) obj;
		if ((this.type.equals(tmp.getType()))
				&& (this.executeId.equals(tmp.getExecuteId()))) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		String tmp = this.type + this.executeId;
		return tmp.hashCode();
	}

	public Set<User> getUser() throws UnsatisfiedDependencyException {
		Set<User> sysUsers = new HashSet<User>();
		UserService userService = (UserService)SpringContextHolder.getBean(UserService.class);
		if ("user".equals(this.type)) {
			User user = (User) userService.getUserById(executeId);
			sysUsers.add(user);
		} else if ("org".equals(this.type)) {
			List<User> users = userService.getUserByOrgId(executeId);
			sysUsers.addAll(users);
		} else if ("role".equals(this.type)) {
			List<User> users = userService.getUserByRoleId(executeId);
			sysUsers.addAll(users);
		}
		return sysUsers;
	}

	public String getMainOrgName() {
		return this.mainOrgName;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}
}
