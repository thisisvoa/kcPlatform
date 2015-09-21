/**
 * @(#)com.casic27.platform.bpm.service.TaskOpinionService
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.IProcessExecutionMapper;
import com.casic27.platform.bpm.dao.ITaskOpinionMapper;
import com.casic27.platform.bpm.entity.ProcessExecution;
import com.casic27.platform.bpm.entity.TaskOpinion;

@Service
public class TaskOpinionService {
	@Autowired
	private ITaskOpinionMapper taskOpinionMapper;
	
	@Autowired
	private IProcessExecutionMapper processExecutionMapper;
	
	/**
	 * 根据ID查询
	 */
	public TaskOpinion getTaskOpinionById(String id){
		return taskOpinionMapper.getTaskOpinionById(id);
	}
	
	/**
	 * 根据任务ID查询
	 * @param taskId
	 * @return
	 */
	public TaskOpinion getTaskOpinionByTaskId(String taskId){
	     return taskOpinionMapper.getTaskOpinionByTaskId(taskId);
	}
	/**
	 * 新增
	 */
	public void addTaskOpinion(TaskOpinion bpmTaskOpinion){
		bpmTaskOpinion.setOpinionId(CodeGenerator.getUUID32());
		taskOpinionMapper.addTaskOpinion(bpmTaskOpinion);
	}
	
	/**
	 * 修改
	 */
	public void updateTaskOpinion(TaskOpinion bpmTaskOpinion){
		taskOpinionMapper.updateTaskOpinion(bpmTaskOpinion);
	}
	
	/**
	 *删除
	 */
	public void deleteTaskOpinion(String id){
		taskOpinionMapper.deleteTaskOpinion(id);
	}
	
	public List<TaskOpinion> getByActInstIdTaskKey(String actInstId, String taskKey){
		return taskOpinionMapper.getByActInstIdTaskKey(actInstId, taskKey);
	}
	
	/**
	 * 获取最新的任务审批意见 
	 * @param actInstId
	 * @param taskKey
	 * @return
	 */
	public TaskOpinion getLatestTaskOpinion(String actInstId, String taskKey){
		List<TaskOpinion> list = getByActInstIdTaskKey(actInstId, taskKey);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据流程实例ID获取流程审批意见
	 * @param actInstId
	 * @param isAsc
	 * @return
	 */
	public List<TaskOpinion> getByActInstId(String actInstId, boolean isAsc) {
		List<String> actInstIds = new ArrayList<String>();
		putRelateActInstIdIntoList(actInstIds, actInstId);
		return this.taskOpinionMapper.getByActInstId(actInstIds, isAsc);
	}
	
	private void putRelateActInstIdIntoList(List<String> actInstIds, String actInstId) {
		actInstIds.add(actInstId);
		ProcessExecution execution = processExecutionMapper.getExecutionById(actInstId);
		if ((execution != null) && (StringUtils.isNotEmpty(execution.getSuperExecutionId()))) {
			ProcessExecution superExecution = (ProcessExecution) this.processExecutionMapper.getExecutionById(execution.getSuperExecutionId());
			if (superExecution != null){
				putRelateActInstIdIntoList(actInstIds, superExecution.getProcessInstanceId());
			}
		}
	}
	
	/**
	 * 根据流程实例ID查询任务审批意见
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinion> getByActInstId(String actInstId) {
		return getByActInstId(actInstId, false);
	}
	/**
	 * 根据任务ID和用户Id获取审批意见
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public TaskOpinion getOpinionByTaskId(String taskId, String userId) {
	     return this.taskOpinionMapper.getOpinionByTaskId(taskId, userId);
	}
	
	/**
	 * 根据流程实例ID删除
	 * @param actInstId
	 */
	public void delByActInstId(String actInstId){
		this.taskOpinionMapper.delByActInstId(actInstId);
	}
	
	/**
	 * 获取流程实例审批中的任务
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinion> getCheckOpinionByInstId(String actInstId){
		return this.taskOpinionMapper.getCheckOpinionByInstId(actInstId);
	}
	
	/**
	 * 获取用户审批意见
	 * @param actInstId
	 * @param exeUserId
	 * @return
	 */
	public TaskOpinion getLatestUserOpinion(String actInstId, String exeUserId) {
		List<TaskOpinion> taskOpinions = this.taskOpinionMapper.getByActInstIdExeUserId(actInstId, exeUserId);
		if (taskOpinions.size() == 0) return null;
		return (TaskOpinion) taskOpinions.get(0);
	}
	
	/**
	 * 根据流程实例ID 获取节点的某个审批状态列表
	 * @param actInstId
	 * @param taskKey
	 * @param checkStatus
	 * @return
	 */
	public List<TaskOpinion> getByActInstIdTaskKeyStatus(String actInstId, String taskKey, Short checkStatus) {
		return this.taskOpinionMapper.getByActInstIdTaskKeyStatus(actInstId, taskKey, checkStatus);
	}
	
	/**
	 * 获取节点处理审批中的审批意见
	 * @param taskId
	 * @return
	 */
	public TaskOpinion getByTaskId(String taskId){
		return this.taskOpinionMapper.getByTaskId(taskId);
	}
}