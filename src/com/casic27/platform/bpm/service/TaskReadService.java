/**
 * @(#)com.casic27.platform.bpm.service.TaskReadService
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.bpm.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.ITaskReadMapper;
import com.casic27.platform.bpm.entity.TaskRead;
import com.casic27.platform.common.user.entity.User;

@Service
public class TaskReadService {
	@Autowired
	private ITaskReadMapper taskReadMapper;
	
	/**
	 * 保存已读信息
	 * @param actInstId
	 * @param taskId
	 */
	public void saveReadRecord(String actInstId, String taskId) {
		User sysUser = SecurityContext.getCurrentUser();
		String userId = sysUser.getZjid();
		if (isTaskRead(taskId, userId))
			return;

		TaskRead taskRead = new TaskRead();
		taskRead.setId(CodeGenerator.getUUID32());
		taskRead.setActInstId(actInstId);
		taskRead.setTaskId(taskId);
		taskRead.setUserId(userId);
		taskRead.setUserName(sysUser.getYhmc());
		taskRead.setCreateTime(new Date());
		this.taskReadMapper.addTaskRead(taskRead);
	}

	public boolean isTaskRead(String taskId, String userId) {
		int count = this.taskReadMapper.isTaskRead(taskId, userId);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	public List<TaskRead> getTaskRead(String actInstId, String taskId) {
		return this.taskReadMapper.getTaskRead(actInstId, taskId);
	}

	public void delByActInstId(String actInstId) {
		this.taskReadMapper.delByActInstId(actInstId);
	}
}