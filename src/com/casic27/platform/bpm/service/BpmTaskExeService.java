/**
 * @(#)com.casic27.platform.bpm.service.BpmTaskExeService
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmTaskExeMapper;
import com.casic27.platform.bpm.dao.IProcessRunMapper;
import com.casic27.platform.bpm.dao.IProcessTaskMapper;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmTaskExe;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.common.user.entity.User;

/**
 * 流程任务代理
 * @author Administrator
 *
 */
@Service
public class BpmTaskExeService {
	@Autowired
	private IBpmTaskExeMapper bpmTaskExeMapper;
	
	@Autowired
	private IProcessTaskMapper processTaskMapper;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private IProcessRunMapper processRunMapper;
	
	@Autowired
	private BpmService bpmService;
	
	/**
	 * 新增代理任务
	 * @param bpmTaskExe
	 * @throws Exception
	 */
	public void assignSave(BpmTaskExe bpmTaskExe) throws Exception {
		this.bpmTaskExeMapper.addBpmTaskExe(bpmTaskExe);

		Short opinionStatus = TaskOpinion.STATUS_DELEGATE;
		if (BpmTaskExe.TYPE_ASSIGNEE.equals(bpmTaskExe.getAssignType())) {//代理
			opinionStatus = TaskOpinion.STATUS_AGENT;
		} else if (BpmTaskExe.TYPE_TRANSMIT.equals(bpmTaskExe.getAssignType())) {//流转
			opinionStatus = TaskOpinion.STATUS_DELEGATE;
			this.processTaskMapper.updateTask(bpmTaskExe.getTaskId(), bpmTaskExe.getAssigneeId(), opinionStatus.toString());
		}
		this.processTaskMapper.delCommuTaskByInstNodeUser(bpmTaskExe.getActInstId(), bpmTaskExe.getTaskDefKey(), bpmTaskExe.getAssigneeId());

		TaskOpinion taskOpinion = this.taskOpinionService.getByTaskId(bpmTaskExe.getTaskId());
		taskOpinion.setCheckStatus(opinionStatus);

		if (BpmTaskExe.TYPE_TRANSMIT.equals(bpmTaskExe.getAssignType())) {
			User sysUser = SecurityContext.getCurrentUser();
			taskOpinion.setExeUserId(sysUser.getZjid());
			taskOpinion.setExeUserName(sysUser.getYhmc());
		}
		taskOpinion.setOpinion(bpmTaskExe.getMemo());
		taskOpinion.setEndTime(new Date());
		Long duration = taskOpinion.getEndTime().getTime()-taskOpinion.getStartTime().getTime();
		taskOpinion.setDurTime(duration);
		this.taskOpinionService.updateTaskOpinion(taskOpinion);

		ProcessRun processRun = this.processRunMapper.getProcessRunById(bpmTaskExe.getRunId());
		TaskOpinion newOpinion = new TaskOpinion();
		newOpinion.setOpinionId(CodeGenerator.getUUID32());
		newOpinion.setActInstId(processRun.getActInstId());
		newOpinion.setActDefId(processRun.getActDefId());
		newOpinion.setCheckStatus(TaskOpinion.STATUS_CHECKING);
		newOpinion.setStartTime(new Date());
		newOpinion.setTaskKey(bpmTaskExe.getTaskDefKey());
		newOpinion.setTaskName(bpmTaskExe.getTaskName());
		newOpinion.setTaskId(bpmTaskExe.getTaskId());
		newOpinion.setExeUserId(bpmTaskExe.getAssigneeId());
		newOpinion.setExeUserName(bpmTaskExe.getAssigneeName());

		this.taskOpinionService.addTaskOpinion(newOpinion);
	}
	
	/**
	 * 取消代理任务
	 * @param bpmTaskExe
	 * @param sysUser
	 * @param opinion
	 * @param informType
	 * @return
	 * @throws Exception
	 */
	public ProcessRun cancel(BpmTaskExe bpmTaskExe, User sysUser, String opinion) throws Exception {
		Short opininStatus = TaskOpinion.STATUS_CHECKING;
		
		User curUser = SecurityContext.getCurrentUser();
		String memo = bpmTaskExe.getMemo() + ",<br/>取消了该任务原因:" + opinion;
		bpmTaskExe.setMemo(memo);
		bpmTaskExe.setExeUserId(bpmTaskExe.getOwnerId());
		bpmTaskExe.setExeUserName(bpmTaskExe.getOwnerName());
		bpmTaskExe.setStatus(BpmTaskExe.STATUS_CANCEL);
		bpmTaskExe.setExeTime(new Date());
		this.bpmTaskExeMapper.updateBpmTaskExe(bpmTaskExe);
		this.processTaskMapper.updateTask(bpmTaskExe.getTaskId(), sysUser.getZjid(), opininStatus.toString());
		
		//设置审批意见为取消转办
		TaskOpinion taskOpinion = this.taskOpinionService.getByTaskId(bpmTaskExe.getTaskId());
		taskOpinion.setCheckStatus(TaskOpinion.STATUS_DELEGATE_CANCEL);
		taskOpinion.setExeUserId(curUser.getZjid());
		taskOpinion.setExeUserName(curUser.getYhmc());
		taskOpinion.setOpinion(opinion);
		taskOpinion.setEndTime(BeanUtils.isEmpty(taskOpinion.getEndTime()) ? new Date() : taskOpinion.getEndTime());
		Long duration = taskOpinion.getEndTime().getTime()-taskOpinion.getStartTime().getTime();
		taskOpinion.setDurTime(duration);
		taskOpinion.setEndTime(new Date());
		this.taskOpinionService.updateTaskOpinion(taskOpinion);
		
		//新增原本任务拥有者审批意见记录
		ProcessRun processRun = this.processRunMapper.getProcessRunById(bpmTaskExe.getRunId());
		TaskOpinion newOpinion = new TaskOpinion();
		newOpinion.setOpinionId(CodeGenerator.getUUID32());
		newOpinion.setActInstId(processRun.getActInstId());
		newOpinion.setActDefId(processRun.getActDefId());
		newOpinion.setCheckStatus(TaskOpinion.STATUS_CHECKING);
		newOpinion.setStartTime(new Date());
		newOpinion.setTaskKey(bpmTaskExe.getTaskDefKey());
		newOpinion.setTaskName(bpmTaskExe.getTaskName());
		newOpinion.setTaskId(bpmTaskExe.getTaskId());
		newOpinion.setExeUserId(bpmTaskExe.getOwnerId());
		newOpinion.setExeUserName(bpmTaskExe.getOwnerName());
		this.taskOpinionService.addTaskOpinion(newOpinion);

		return processRun;
	}
	
	/**
	 * 根据ID获取代理任务
	 * @param id
	 * @return
	 */
	public BpmTaskExe getBpmTaskExeById(String id){
		return this.bpmTaskExeMapper.getBpmTaskExeById(id);	
	}
	/**
	 * 批量取消代理
	 * @param ids
	 * @param opinion
	 * @param sysUser
	 * @return
	 * @throws Exception
	 */
	public List<BpmTaskExe> cancelBat(String[] ids, String opinion, User sysUser) throws Exception {
		List<BpmTaskExe> list = new ArrayList<BpmTaskExe>();
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			BpmTaskExe bpmTaskExe = this.bpmTaskExeMapper.getBpmTaskExeById(id);
			String taskId = bpmTaskExe.getTaskId();
			TaskEntity taskEntity = this.bpmService.getTask(taskId.toString());
			if (taskEntity != null) {
				cancel(bpmTaskExe, sysUser, opinion);
				list.add(bpmTaskExe);
			}
		}
		return list;
	}
	
	/**
	 * 取消任务的代理
	 * @param taskId
	 */
	public void cancel(String taskId) {
		BpmTaskExe bpmTaskExe = getByTaskIdStatusInit(taskId);
		if (BeanUtils.isNotEmpty(bpmTaskExe)) {
			bpmTaskExe.setStatus(BpmTaskExe.STATUS_CANCEL);
			this.bpmTaskExeMapper.updateBpmTaskExe(bpmTaskExe);
		}
	}
	
	/**
	 * 完成某个代理任务
	 * @param taskId
	 */
	public void complete(String taskId) {
		List<BpmTaskExe> list = getByTaskId(taskId);
		User sysUser = SecurityContext.getCurrentUser();
		for (BpmTaskExe bpmTaskExe : list) {
			if (bpmTaskExe.getStatus() == BpmTaskExe.STATUS_INIT) {
				bpmTaskExe.setExeTime(new Date());
				bpmTaskExe.setExeUserId(sysUser.getZjid());
				bpmTaskExe.setExeUserName(sysUser.getYhmc());
				if (bpmTaskExe.getAssigneeId().equals(sysUser.getZjid()))
					bpmTaskExe.setStatus(BpmTaskExe.STATUS_COMPLETE);
				else {
					bpmTaskExe.setStatus(BpmTaskExe.STATUS_OTHER_COMPLETE);
				}
			}
			this.bpmTaskExeMapper.updateBpmTaskExe(bpmTaskExe);
		}
	}
	
	/**
	 * 获取初始化的任务代理
	 * @param taskId
	 * @return
	 */
	public BpmTaskExe getByTaskIdStatusInit(String taskId) {
		BpmTaskExe bpmTaskExe = this.bpmTaskExeMapper.getByTaskIdStatus(taskId, BpmTaskExe.STATUS_INIT);
		return bpmTaskExe;
	}
	
	/**
	 * 查询任务的代理列表
	 * @param taskId
	 * @return
	 */
	public List<BpmTaskExe> getByTaskId(String taskId) {
		return this.bpmTaskExeMapper.getByTaskId(taskId);
	}

	public boolean isAssigneeTask(String taskId) {
		return BeanUtils.isNotEmpty(getByTaskIdStatusInit(taskId));
	}
	
	/**
	 * 获取任务代理状态
	 * @param list
	 * @return
	 */
	public Map<String, Integer> getTaskMap(List<?> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (BeanUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				ProcessTask task = (ProcessTask) list.get(i);
				BpmTaskExe bpmTaskExe = getByTaskIdStatusInit(task.getId());
				if (BeanUtils.isEmpty(bpmTaskExe))
					map.put(task.getId(), 0);
				else {
					map.put(task.getId(), bpmTaskExe.getAssignType());
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取我的代理任务
	 * @param queryMap
	 * @return
	 */
	public List<BpmTaskExe> accordingMattersList(Map<String,Object> queryMap) {
		return this.bpmTaskExeMapper.accordingMattersList(queryMap);
	}
	
	/**
	 * 判断任务是否能代理
	 * @param taskEnt
	 * @param bpmDefinition
	 * @return
	 */
	public boolean isAssigneeTask(TaskEntity taskEnt, BpmDefinition bpmDefinition) {
		boolean isFirstNode = NodeCache.isFirstNode(taskEnt.getProcessDefinitionId(), taskEnt.getTaskDefinitionKey());
		if (isFirstNode) {
			return false;
		}
		boolean isCanAssignee = !isAssigneeTask(taskEnt.getId());
		if ((TaskOpinion.STATUS_RECOVER_TOSTART.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinion.STATUS_REJECT_TOSTART.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinion.STATUS_REJECT.toString().equals(taskEnt.getDescription()))
				|| (TaskOpinion.STATUS_DELEGATE.toString().equals(taskEnt.getDescription()))) {
			isCanAssignee = false;
		}
		return isCanAssignee;
	}

	public boolean getByIsAssign(String taskId) {
		int rtn = this.bpmTaskExeMapper.getByIsAssign(taskId).intValue();
		return rtn > 0;
	}
	
	public void delById(String id){
		this.bpmTaskExeMapper.delById(id);
	}
	
	public void delByRunId(String runId) {
		this.bpmTaskExeMapper.delByRunId(runId);
	}

	public List<BpmTaskExe> getByRunId(String runId) {
		return this.bpmTaskExeMapper.getByRunId(runId);
	}
}