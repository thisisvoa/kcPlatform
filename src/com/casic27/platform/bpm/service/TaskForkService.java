/**
 * @(#)com.casic27.platform.bpm.service.TaskForkService
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

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IProcessRunMapper;
import com.casic27.platform.bpm.dao.ITaskForkMapper;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.TaskFork;

@Service
public class TaskForkService {
	
	@Autowired
	private ITaskForkMapper taskForkMapper;
	
	@Autowired
	private IProcessRunMapper processRunMapper;

	public Integer getMaxSn(String actInstId) {
		return this.taskForkMapper.getMaxSn(actInstId);
	}

	public TaskFork newTaskFork(DelegateTask delegateTask, String joinTaskName, String joinTaskKey, Integer forkCount) {
		String token = (String) delegateTask.getVariableLocal(TaskFork.TAKEN_VAR_NAME);
		TaskFork taskFork = new TaskFork();
		taskFork.setTaskForkId(CodeGenerator.getUUID32());
		taskFork.setActInstId(delegateTask.getProcessInstanceId());
		taskFork.setForkTime(new Date());
		taskFork.setFininshCount(Integer.valueOf(0));
		taskFork.setForkCount(forkCount);
		taskFork.setForkTaskKey(delegateTask.getTaskDefinitionKey());
		taskFork.setForkTaskName(delegateTask.getName());
		taskFork.setJoinTaskKey(joinTaskKey);
		taskFork.setJoinTaskName(joinTaskName);
		Integer sn = Integer.valueOf(1);
		if (token == null) {
			taskFork.setForkTokenPre(TaskFork.TAKEN_PRE + "_");
		} else {
			String[] lines = token.split("[_]");
			if ((lines != null) && (lines.length > 1)) {
				sn = Integer.valueOf(lines.length - 1);
				String forkTokenPre = token.substring(0, token.lastIndexOf(lines[sn.intValue()]));
				taskFork.setForkTokenPre(forkTokenPre);
			}
		}

		String forkTokens = "";
		for (int i = 1; i <= forkCount.intValue(); i++) {
			forkTokens = forkTokens + taskFork.getForkTokenPre() + i + ",";
		}
		taskFork.setForkTokens(forkTokens);
		taskFork.setForkSn(sn);
		taskForkMapper.addTaskFork(taskFork);
		return taskFork;
	}

	public TaskFork getByInstIdJoinTaskKey(String actInstId, String joinTaskKey) {
		return this.taskForkMapper.getByInstIdJoinTaskKey(actInstId, joinTaskKey);
	}

	public TaskFork getByInstIdJoinTaskKeyForkToken(String actInstId, String joinTaskKey, String forkToken) {
		return this.taskForkMapper.getByInstIdJoinTaskKeyForkToken(actInstId, joinTaskKey, forkToken);
	}
	
	/**
	 * 修改
	 * @param taskFork
	 */
	public void updateTaskFork(TaskFork taskFork){
		taskForkMapper.updateTaskFork(taskFork);
	}
	
	/**
	 * 根据ID删除
	 * @param id
	 */
	public void deleteById(String id){
		taskForkMapper.deleteById(id);
	}
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public TaskFork getTaskForkById(String id){
		return taskForkMapper.getTaskForkById(id);
	}
	
	/**
	 * 根据流程定义进行删除
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId) {
		List<ProcessRun> prolist = this.processRunMapper.getProcessRunByActDefId(actDefId);
		if (prolist!=null){
			for (ProcessRun processRun : prolist){
				this.taskForkMapper.delByActInstId(processRun.getActInstId());
			}
		}
	}
}