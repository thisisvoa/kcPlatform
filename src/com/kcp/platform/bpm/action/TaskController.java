package com.kcp.platform.bpm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.bpm.entity.BpmFormDef;
import com.kcp.platform.bpm.entity.BpmNodeButton;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.entity.BpmNodeSign;
import com.kcp.platform.bpm.entity.BpmRunLog;
import com.kcp.platform.bpm.entity.FormModel;
import com.kcp.platform.bpm.entity.NodeCache;
import com.kcp.platform.bpm.entity.ProcessCmd;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.entity.ProcessTask;
import com.kcp.platform.bpm.entity.ProcessTaskHistory;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.entity.TaskSignData;
import com.kcp.platform.bpm.service.BpmDefinitionService;
import com.kcp.platform.bpm.service.BpmFormDefService;
import com.kcp.platform.bpm.service.BpmFormRunService;
import com.kcp.platform.bpm.service.BpmNodeButtonService;
import com.kcp.platform.bpm.service.BpmNodeConfigService;
import com.kcp.platform.bpm.service.BpmNodeSignService;
import com.kcp.platform.bpm.service.BpmRunLogService;
import com.kcp.platform.bpm.service.BpmService;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.bpm.service.TaskOpinionService;
import com.kcp.platform.bpm.service.TaskReadService;
import com.kcp.platform.bpm.service.TaskSignDataService;
import com.kcp.platform.bpm.util.BpmUtil;
import com.kcp.platform.bpm.util.ExceptionUtil;
import com.kcp.platform.bpm.util.WebUtil;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.BeanUtils;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;

@Controller
@RequestMapping(value = "/workflow/task")
public class TaskController extends BaseMultiActionController {
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	@Autowired
	private BpmFormRunService bpmFormRunService;
	
	@Autowired
	private BpmFormDefService bpmFormDefService;
	
//	@Autowired
//	private BpmFormTableService bpmFormTableService;
	
	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private TaskReadService taskReadService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private BpmNodeConfigService bpmNodeConfigService;
	
	@Autowired
	private BpmNodeButtonService bpmNodeButtonService;
	
	@Autowired
	private BpmNodeSignService bpmNodeSignService;
	
	@Autowired
	private TaskSignDataService taskSignDataService;
	
	@Autowired
	private BpmRunLogService bpmRunLogService;
	/**
	 * 跳至启动流程页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("startFlowForm")
	public ModelAndView startFlowForm(HttpServletRequest request, HttpServletResponse response){
		String defId = RequestUtil.getString(request, "defId");
		String businessKey = RequestUtil.getString(request, "businessKey");
		String runId = RequestUtil.getString(request, "runId");
		Map<String,Object> paraMap = RequestUtil.getParameterValueMap(request, false, false);
		paraMap.remove("businessKey");
	    paraMap.remove("defId");
	    
	    ModelAndView mv = new ModelAndView("workflow/bpm/taskStartFlowForm");
		
	    ProcessRun processRun = null;
		if(StringUtils.isNotEmpty(runId)){
			processRun = processRunService.getProcessRunById(runId);
			defId = processRun.getDefId();
		}
		
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		if(bpmDefinition==null){
			throw new BusinessException("流程定义不存在!");
		}else if(BpmDefinition.STATUS_UNDEPLOYED.toString().equals(bpmDefinition.getStatus()) || CommonConst.NO.equals(bpmDefinition.getUsable())){
			throw new BusinessException("该流程已经被禁用!");
		}
		
		Boolean isFormEmpty = Boolean.valueOf(false);
		Boolean isExtForm = Boolean.valueOf(false);
		String form = "";
		String ctxPath = request.getContextPath();
		if(processRun!=null && processRun.getStatus()==ProcessRun.STATUS_FORM){//流程已经保存为草稿的情况
			mv.addObject("isDraft", Boolean.valueOf(false));
			businessKey = processRun.getBusinessKey();
			String formDefId = processRun.getFormDefId();
		    if(StringUtils.isNotEmpty(formDefId)){
		    	//TODO 处理在线表单
		    }
		    if (StringUtils.isNotEmpty(processRun.getBusinessUrl())) {
		         isExtForm = true;
		         form = processRun.getBusinessUrl();
	        	 form = ctxPath + parseFormUrl(form, businessKey);; 
		    }else{
		    	//TODO 处理在线表单
		    }
		}else{//新启动的流程
			mv.addObject("isDraft", Boolean.valueOf(true));
			String actDefId = bpmDefinition.getActDefId();
			BpmNodeConfig nodeConfig = bpmFormRunService.getStartBpmNodeConfig(defId, actDefId);
			FormModel model = getForm(nodeConfig, businessKey, actDefId, ctxPath);
			isFormEmpty = model.isFormEmpty();
		    isExtForm = model.isExtForm();
		    if(isExtForm){
		    	form = model.getFormUrl();
		    }else{
		    	form = model.getFormHtml();
		    }
		    if(nodeConfig!=null){
		    	mv.addObject("formKey", nodeConfig.getFormKey());
		    }
		}
		List<BpmNodeButton> buttonList = this.bpmNodeButtonService.getStartNodeButton(bpmDefinition.getDefId());
	    mv.addObject("bpmDefinition", bpmDefinition)
	      .addObject("form", form)
	      .addObject("defId", defId)
	      .addObject("isExtForm", isExtForm)
	      .addObject("isFormEmpty", Boolean.valueOf(isFormEmpty))
	      .addObject("paraMap", paraMap)
	      .addObject("runId", runId)
	      .addObject("businessKey", businessKey)
	      .addObject("buttonList", buttonList);
	    return mv;
	}
	
	@RequestMapping("startFlow")
	public @ResponseBody void startFlow(HttpServletRequest request, HttpServletResponse response)throws Exception{
		try {
			String runId = RequestUtil.getString(request, "runId");
			ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
			String userId = SecurityContext.getCurrentUser().getZjid();
			processCmd.setCurrentUserId(userId);
			if (StringUtils.isNotEmpty(runId)) {
			     ProcessRun processRun = (ProcessRun)this.processRunService.getProcessRunById(runId);
			     if (processRun==null) {
			       throw new BusinessException("流程草稿不存在或已被清除!");
			     }
			     processCmd.setProcessRun(processRun);
			}
			this.processRunService.startProcess(processCmd);
		} catch (Exception e) {
			throw new BusinessException("启动流程失败:"+e.getMessage());
		}
	}
	
	/**
	 * 进入任务处理表单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "toStart" })
	public ModelAndView toStart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/workflow/bpm/taskToStart");
		return getToStartView(request, response, mv, 0);
	}
	
	/**
	 * 进入任务处理表单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "doNext" })
	public ModelAndView doNext(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/workflow/bpm/taskToStart");
		mv = getToStartView(request, response, mv, 1);
		return mv;
	}
	
	/**
	 * 流程任务处理页面
	 * @param request
	 * @param response
	 * @param mv
	 * @param isManage
	 * @return
	 * @throws Exception
	 */
	private ModelAndView getToStartView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, int isManage)
			throws Exception {
		String ctxPath = request.getContextPath();
		User sysUser = SecurityContext.getCurrentUser();
		String taskId = RequestUtil.getString(request, "taskId");
		String instanceId = RequestUtil.getString(request, "instanceId");
		if ((StringUtils.isEmpty(taskId)) && (StringUtils.isEmpty(instanceId))) {
			throw new BusinessException("没有输入任务或实例ID!");
		}

		if (StringUtils.isNotEmpty(instanceId)) {
			List<ProcessTask> list = this.bpmService.getTasks(instanceId);
			if (BeanUtils.isNotEmpty(list)) {
				taskId = ((ProcessTask) list.get(0)).getId();
			}
		}

		TaskEntity taskEntity = this.bpmService.getTask(taskId);

		if (taskEntity == null) {
			ProcessTaskHistory taskHistory = (ProcessTaskHistory) bpmService.getTaskHistoryById(taskId);
			if ((taskHistory == null) && (StringUtils.isEmpty(taskId))&& (StringUtils.isEmpty(instanceId))) {
				throw new BusinessException("任务ID错误!");
			}
			String actInstId = taskHistory.getProcessInstanceId();
			if ((StringUtils.isEmpty(actInstId)) && (taskHistory.getDescription().equals(TaskOpinion.STATUS_COMMUNICATION.toString()))) {
				throw new BusinessException("此任务为沟通任务,并且此任务已经处理!");
			}
			ProcessRun processRun = this.processRunService.getProcessRunByActInstId(actInstId);
			String url = request.getContextPath() + "/workflow/bpm/processRun/info.html?runId=" + processRun.getRunId();
			response.sendRedirect(url);
			return null;
		}

		if ((TaskOpinion.STATUS_TRANSTO_ING.toString().equals(taskEntity.getDescription())) && (taskEntity.getAssignee().equals(sysUser.getZjid()))) {
			throw new BusinessException("对不起,这个任务正在流转中,不能处理此任务!");
		}

		instanceId = taskEntity.getProcessInstanceId();

		/**if (isManage == 0) {
			boolean hasRights = this.processRunService.getHasRightsByTask(taskEntity.getId(), sysUser.getZjid());
			if (!hasRights) {
				throw new BusinessException("对不起,你不是这个任务的执行人,不能处理此任务!");
			}
		}*/

		this.taskReadService.saveReadRecord(instanceId,taskId);

		String nodeId = taskEntity.getTaskDefinitionKey();
		String actDefId = taskEntity.getProcessDefinitionId();
		String userId = SecurityContext.getCurrentUserId();

		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
		ProcessRun processRun = this.processRunService.getProcessRunByActInstId(instanceId);

		BpmNodeConfig bpmNodeConfig = this.bpmNodeConfigService.getByActDefIdNodeId(actDefId, nodeId);
		String toBackNodeId = NodeCache.getFirstNodeId(actDefId).getNodeId();
		
		/**********获取流程表单***********/
		String form = "";
		Map<String,Object> variables = this.taskService.getVariables(taskId);
		FormModel formModel = this.bpmFormRunService.getForm(processRun,nodeId, ctxPath, variables);
		
		if (!formModel.isValid()) {
			throw new BusinessException("流程定义的流程表单发生了更改,数据无法显示!");
		}

		String detailUrl = formModel.getDetailUrl();

		Boolean isExtForm = formModel.isExtForm();
		if (isExtForm){
			form = formModel.getFormUrl();			
		} else {
			form = formModel.getFormHtml();
		}
		Boolean isEmptyForm = Boolean.valueOf(formModel.isFormEmpty());

		boolean isSignTask = this.bpmService.isSignTask(taskEntity);
		if (isSignTask) {
			handleSignTask(mv, instanceId, nodeId, actDefId, userId);
		}

		boolean isCanBack = this.bpmService.getIsAllowBackByTask(taskEntity);//能否驳回
		boolean isFirstNode = isFirstNode(taskEntity);//是否是第一个节点
		List<BpmNodeButton> buttonList = this.bpmNodeButtonService.getNodeButtonByNodeId(bpmDefinition.getDefId(),nodeId);//按钮列表
		TaskOpinion taskOpinion = this.taskOpinionService.getOpinionByTaskId(taskId, userId);
		return mv.addObject("task", taskEntity)
				.addObject("bpmNodeConfig", bpmNodeConfig)
				.addObject("processRun", processRun)
				.addObject("bpmDefinition", bpmDefinition)
				.addObject("isSignTask", Boolean.valueOf(isSignTask))
				.addObject("isCanBack", Boolean.valueOf(isCanBack))
				.addObject("isFirstNode", Boolean.valueOf(isFirstNode))
				.addObject("toBackNodeId", toBackNodeId)
				.addObject("form", form)
				.addObject("isExtForm", isExtForm)
				.addObject("isEmptyForm", isEmptyForm)
				.addObject("buttonList", buttonList)
				.addObject("detailUrl", detailUrl)
				.addObject("isManage", Integer.valueOf(isManage))
				.addObject("taskOpinion", taskOpinion);
	}
	
	/**
	 * 完成任务
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("complete")
	public @ResponseBody void complete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("任务完成跳转....");
		User curUser = SecurityContext.getCurrentUser();
		String taskId = RequestUtil.getString(request, "taskId");
		TaskEntity task = this.bpmService.getTask(taskId);
		if (task == null) {
			throw new BusinessException("此任务已经执行了！");
		}
		String actDefId = task.getProcessDefinitionId();
		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
		if (BpmDefinition.STATUS_UNDEPLOYED.equals(bpmDefinition.getStatus()) || CommonConst.NO.equals(bpmDefinition.getUsable())){
			throw new BusinessException("该流程定义已经被禁用，该任务不能办理！");
		}
		String userId = curUser.getZjid();
		
		ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
		taskCmd.setCurrentUserId(userId.toString());
		String assignee = task.getAssignee();
		boolean isAdmin = taskCmd.getIsManage().shortValue() == 1;
		/**if (!isAdmin) {
			boolean rtn = this.processRunService.getHasRightsByTask(new Long(taskId), userId);
			if (!rtn) {
				throw new BusinessException("对不起,你不是这个任务的执行人,不能处理此任务!");
			}
		}*/
		this.logger.info(taskCmd.toString());
		if ((StringUtils.isNotEmpty(assignee) && !("0".equals(assignee)))
				&& (!task.getAssignee().equals(userId.toString()))
				&& (!isAdmin)) {
			throw new BusinessException("该任务已被其他人锁定！");
		} else {
			try {
				this.processRunService.nextProcess(taskCmd);
			} catch (Exception ex) {
				throw new BusinessException(ExceptionUtil.getExceptionMsg(ex),ex);
			}
		}
	}
	/**
	 * 终止流程
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("endProcess")
	public @ResponseBody void endProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String taskId = RequestUtil.getString(request, "taskId");
		String memo = RequestUtil.getString(request, "memo");
		TaskEntity taskEnt = this.bpmService.getTask(taskId);
		if (taskEnt == null) {
			throw new BusinessException("此任务已经完成!");
		}
		String instanceId = taskEnt.getProcessInstanceId();
		String nodeId = taskEnt.getTaskDefinitionKey();
		ProcessRun processRun = this.bpmService.endProcessByInstanceId(instanceId, nodeId, memo);
		memo = "结束流程:" + processRun.getSubject() + ",结束原因:" + memo;
		this.bpmRunLogService.addBpmRunLog(processRun, BpmRunLog.OPERATOR_TYPE_ENDTASK, memo);
	}
	
	@RequestMapping("toEnd")
	public ModelAndView toEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("workflow/bpm/taskToEnd");
	}
	/**
	 * 锁定任务(签收)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "claim" })
	public @ResponseBody void claim(HttpServletRequest request, HttpServletResponse response){
		String taskId = RequestUtil.getString(request, "taskId");
		String assignee = SecurityContext.getCurrentUserId();
		try {
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			ProcessRun processRun = this.processRunService.getProcessRunByActInstId(taskEntity.getProcessInstanceId());
			String runId = processRun.getRunId();
			this.taskService.claim(taskId, assignee);
			String memo = "流程:" + processRun.getSubject() + ",锁定任务，节点【" + taskEntity.getName() + "】";
			this.bpmRunLogService.addBpmRunLog(runId,BpmRunLog.OPERATOR_TYPE_LOCK, memo);
		} catch (Exception ex) {
			throw new BusinessException("任务已经完成或被其他用户锁定!");
		}
	}
	
	/**
	 * 解锁任务
	 * @param request
	 * @param response
	 */
	@RequestMapping({ "unlock" })
	public @ResponseBody void unlock(HttpServletRequest request, HttpServletResponse response){
		String taskId = request.getParameter("taskId");
		if (StringUtils.isNotEmpty(taskId)) {
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			ProcessRun processRun = this.processRunService.getProcessRunByActInstId(taskEntity.getProcessInstanceId());
			String runId = processRun.getRunId();
			this.bpmService.updateTaskAssigneeNull(taskId);
			String memo = "流程:" + processRun.getSubject() + ",解锁任务，节点【"+ taskEntity.getName() + "】";
			this.bpmRunLogService.addBpmRunLog(runId, BpmRunLog.OPERATOR_TYPE_UNLOCK, memo);
		}
	}
	/**
	 * 保存草稿
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "saveForm" })
	public void saveForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = SecurityContext.getCurrentUserId();
		ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
		processCmd.setCurrentUserId(userId);
		this.processRunService.saveForm(processCmd);
	}
	
	/**
	 * 保存表单数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveData")
	public void saveData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = SecurityContext.getCurrentUserId();
		ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
		processCmd.setCurrentUserId(userId);
		String runId = RequestUtil.getString(request, "runId");
		if (StringUtils.isNotEmpty(runId)) {
			ProcessRun processRun = (ProcessRun) this.processRunService.getProcessRunById(runId);
			processCmd.setProcessRun(processRun);
		}
		this.processRunService.saveData(processCmd);
	}
	
	/**
	 * 批量审批
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("batComplete")
	public @ResponseBody void batComplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String taskIds = RequestUtil.getString(request, "taskIds");
		String opinion = RequestUtil.getString(request, "opinion");
		this.processRunService.nextProcessBat(taskIds, opinion);
	}
	
	@RequestMapping("toBatComplete")
	public ModelAndView toBatComplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String taskIds = RequestUtil.getString(request, "taskIds");
		return new ModelAndView("/workflow/bpm/taskToBatComplete")
					.addObject("taskIds", taskIds);
	}
	/**
	 * 任务管理列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toList")
	public ModelAndView toList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/workflow/bpm/taskList");
	}
	
	/**
	 * 所有的任务列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public @ResponseBody GridData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String name = RequestUtil.getString(request, "name");
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("name", name);
		List<ProcessTask> taskList = bpmService.getAllTask(queryMap);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(taskList!=null){
			String ctx = request.getContextPath();
			for(ProcessTask task:taskList){
				Map<String,Object> t = BeanUtils.transBean2Map(task);
				String assigneeStr = WebUtil.getAssignee(ctx, task.getAssignee(), task.getId(), true);
				t.put("assigneeStr", assigneeStr);
				result.add(t);
			}
		}
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalItems(), page.getTotalPages());
	}
	
	/**
	 * 待办页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toPendingList")
	public ModelAndView toPendingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("workflow/bpm/taskPendingList");
	}
	
	/**
	 * 待办事项列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pendingList")
	public @ResponseBody GridData pendingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String creator = RequestUtil.getString(request, "creator");
		String hasRead = RequestUtil.getString(request, "hasRead");
		String description = RequestUtil.getString(request, "description");
		String taskStatus = RequestUtil.getString(request, "taskStatus");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String userId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creator", creator);
		queryMap.put("taskStatus", taskStatus);
		queryMap.put("description", description);
		queryMap.put("hasRead", hasRead);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		queryMap.put("userId", userId);
		List<ProcessTask> taskList = bpmService.getMyTask(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(taskList, page.getPage(), page.getTotalItems(), page.getTotalPages());
	}
	
	/**
	 * 补签页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAddSign")
	public ModelAndView toAddSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String taskId = RequestUtil.getString(request, "taskId");
		return new ModelAndView("workflow/bpm/taskToAddSign")
							.addObject("taskId", taskId);
	}
	
	/**
	 * 补签
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("addSign")
	public @ResponseBody void addSign(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String taskId = RequestUtil.getString(request, "taskId");
		String signUserIds = RequestUtil.getString(request, "signUserIds");
		if ((StringUtils.isNotEmpty(taskId)) && (StringUtils.isNotEmpty(signUserIds))) {
			this.taskSignDataService.addSign(signUserIds, taskId);
		}
	}
	
	/**
	 * 处理会签节点
	 * @param mv
	 * @param instanceId
	 * @param nodeId
	 * @param actDefId
	 * @param userId
	 */
	private void handleSignTask(ModelAndView mv, String instanceId, String nodeId, String actDefId, String userId) {
		List<TaskSignData> signDataList = this.taskSignDataService.getByNodeAndInstanceId(instanceId, nodeId);
		BpmNodeSign bpmNodeSign = this.bpmNodeSignService.getByDefIdAndNodeId(actDefId, nodeId);
		mv.addObject("signDataList", signDataList);
		mv.addObject("bpmNodeSign", bpmNodeSign);
		mv.addObject("curUser", SecurityContext.getCurrentUser());
		String orgId = SecurityContext.getCurrentOrgId();
		List<Role> roleList = SecurityContext.getCurrentRoleList();
		boolean isAllowDirectExecute = this.bpmNodeSignService.checkNodeSignPrivilege(actDefId, nodeId, BpmNodeSignService.BpmNodePrivilegeType.ALLOW_DIRECT, userId, orgId, roleList);
		boolean isAllowRetoactive = this.bpmNodeSignService.checkNodeSignPrivilege(actDefId,nodeId, BpmNodeSignService.BpmNodePrivilegeType.ALLOW_RETROACTIVE,userId, orgId, roleList);
		boolean isAllowOneVote = this.bpmNodeSignService.checkNodeSignPrivilege(actDefId, nodeId, BpmNodeSignService.BpmNodePrivilegeType.ALLOW_ONE_VOTE, userId, orgId, roleList);
		mv.addObject("isAllowDirectExecute",isAllowDirectExecute)
					.addObject("isAllowRetoactive", isAllowRetoactive)
					.addObject("isAllowOneVote", isAllowOneVote);
	}
	
	/**
	 * 获取表单
	 * @param nodeConfig
	 * @param businessKey
	 * @param actDefId
	 * @param path
	 * @return
	 */
	private FormModel getForm(BpmNodeConfig nodeConfig, String businessKey, String actDefId, String path){
		FormModel formModel = new FormModel();
		if (nodeConfig == null)
			return formModel;
		String form = "";
		if (BpmNodeConfig.NODE_FORM_TYPE_ONLINE.equals(nodeConfig.getFormType())) {
			formModel.setFormType(BpmNodeConfig.NODE_FORM_TYPE_ONLINE);
			String formDefId = nodeConfig.getFormDefId();
			if (StringUtils.isNotEmpty(formDefId)) {
				BpmFormDef bpmFormDef = (BpmFormDef) this.bpmFormDefService.getBpmFormDefById(formDefId);
				if (bpmFormDef != null) {
//					BpmFormTable bpmFormTable = (BpmFormTable) this.bpmFormTableService.getBpmFormTableById(bpmFormDef.getTableId());
					//TODO 在线表单的HTML解析
				}
			}
		} else {
			form = path + parseFormUrl(nodeConfig.getFormUrl(), businessKey);
			formModel.setFormType(BpmNodeConfig.NODE_FORM_TYPE_URL);
			formModel.setFormUrl(form);
		}
		return formModel;
	}
	
	private String parseFormUrl(String url, String businessKey){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pk", businessKey);
		url = BpmUtil.parseTextByRule(url, map);
		return url;
	}
	
	/**
	 * 审批中的任务是否第一个节点
	 * @param processRun
	 * @return
	 * @throws Exception
	 */
	private boolean isFirstNode(TaskEntity task){
		boolean isFirst = false;
		if (BeanUtils.isEmpty(task)) return isFirst;
		String actDefId = task.getProcessDefinitionId();
		String firstNodeId = NodeCache.getFirstNodeId(actDefId).getNodeId();
		return (task.getTaskDefinitionKey().equals(firstNodeId));
	}
	
}
