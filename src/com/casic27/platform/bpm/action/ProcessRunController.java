/**
 * @(#)com.casic27.platform.bpm.action.ProcessRunController
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
package com.casic27.platform.bpm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.RequestUtil;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmNodeConfig;
import com.casic27.platform.bpm.entity.BpmRunLog;
import com.casic27.platform.bpm.entity.FlowNode;
import com.casic27.platform.bpm.entity.NodeCache;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.ProcessTask;
import com.casic27.platform.bpm.entity.TaskExeStatus;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.entity.TaskNodeStatus;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.graph.ShapeMeta;
import com.casic27.platform.bpm.service.BpmDefinitionService;
import com.casic27.platform.bpm.service.BpmNodeConfigService;
import com.casic27.platform.bpm.service.BpmRunLogService;
import com.casic27.platform.bpm.service.BpmService;
import com.casic27.platform.bpm.service.ProcessRunService;
import com.casic27.platform.bpm.service.TaskOpinionService;
import com.casic27.platform.bpm.service.TaskReadService;
import com.casic27.platform.bpm.service.TaskUserService;
import com.casic27.platform.bpm.util.BpmUtil;

@Controller
@RequestMapping("workflow/bpm/processRun")
public class ProcessRunController extends BaseMultiActionController{
	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private BpmNodeConfigService bpmNodeConfigService;
	
	@Autowired
	private TaskOpinionService taskOpinionService;
	
	@Autowired
	private BpmRunLogService bpmRunLogService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskReadService taskReadService;
	
	@Autowired
	private TaskUserService taskUserService;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("workflow/bpm/processRunList");
	}
	
	@RequestMapping("/toListDefId")
	public ModelAndView toListDefId(HttpServletRequest request){
		String defId = RequestUtil.getString(request, "defId");
		return new ModelAndView("workflow/bpm/processRunListDefId")
						.addObject("defId", defId);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData processRunList(HttpServletRequest request)throws Exception{
		String defId = RequestUtil.getString(request, "defId");
	  	String processName = RequestUtil.getString(request, "processName");
	  	String subject = RequestUtil.getString(request, "subject");
	  	String status = RequestUtil.getString(request, "status");
	  	Date startTime = RequestUtil.getTimeStamp(request, "startTime");
	  	Date endTime = RequestUtil.getTimeStamp(request, "endTime");
	  	if(endTime!=null){
	  		endTime = new Date(endTime.getTime()+999);
	  	}
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("defId", defId);
	 	queryMap.put("processName", processName);
	 	queryMap.put("subject", subject);
	 	queryMap.put("status", status);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
		List<ProcessRun> result = processRunService.findProcessRunPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 流程实例明细
	 * @param request
	 * @return
	 */
	@RequestMapping("detail")
	public ModelAndView detail(HttpServletRequest request){
		String runId = StringUtils.getNullBlankStr(request.getParameter("runId"));
		ProcessRun processRun = processRunService.getProcessRunById(runId);
		return new ModelAndView("workflow/bpm/processRunDetail")
					.addObject("processRun", processRun);
	}
	
	
	/**
	 * 删除操作
	 */
	@RequestMapping("del")
	public @ResponseBody void del(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String[] ids = RequestUtil.getStringAryByStr(request, "ids");
		this.processRunService.delByIds(ids);
	}
	
	/**
	 * 流程监控图
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("graph")
	public ModelAndView graph(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView("workflow/bpm/processRunGraph");
		String action = request.getParameter("action");
		String runId = request.getParameter("runId");
		String actInstanceId = null;
		ProcessRun processRun = null;
		if (StringUtils.isNotEmpty(runId)) {
			processRun = (ProcessRun) this.processRunService.getProcessRunById(runId);
			actInstanceId = processRun.getActInstId();
		} else {
			actInstanceId = request.getParameter("actInstId");
			processRun = this.processRunService.getProcessRunByActInstId(actInstanceId);
		}
		
		String defXml = this.bpmService.getDefXmlByProcessDefinitionId(processRun.getActDefId());
		ExecutionEntity executionEntity = this.bpmService.getExecution(actInstanceId);
		if ((executionEntity != null) && (executionEntity.getSuperExecutionId() != null)) {
			ExecutionEntity superExecutionEntity = this.bpmService.getExecution(executionEntity.getSuperExecutionId());
			modelAndView.addObject("superInstanceId",superExecutionEntity.getProcessInstanceId());
		}

		ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
		modelAndView.addObject("notShowTopBar", request.getParameter("notShowTopBar"))
				.addObject("defXml", defXml)
				.addObject("processInstanceId", actInstanceId)
				.addObject("shapeMeta", shapeMeta)
				.addObject("processRun", processRun)
				.addObject("action", action);
		return modelAndView;
	}
	/**
	 * 流程业务表单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getForm")
	public ModelAndView getForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String runId = RequestUtil.getString(request, "runId");
		String ctxPath = request.getContextPath();
		ProcessRun processRun = this.processRunService.getProcessRunById(runId);
		Map<String, Object> formMap = getFormByProcessRun(processRun, ctxPath);
		ModelAndView view = new ModelAndView("workflow/bpm/processRunForm")
							.addObject("processRun", processRun)
							.addObject("isFormEmpty", formMap.get("isFormEmpty"))
							.addObject("isExtForm", formMap.get("isExtForm"))
							.addObject("form", formMap.get("form"));
		return view;
	}
	
	/**
	 * 已办列表页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAlreadyMatterList")
	public ModelAndView toAlreadyMatterList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("workflow/bpm/processRunAlreadyMatterList");
	}
	
	/**
	 * 已办列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("alreadyMatterList")
	public @ResponseBody GridData alreadyMatterList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String creator = RequestUtil.getString(request, "creator");
		String status = RequestUtil.getString(request, "status");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String curUserId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creator", creator);
		queryMap.put("status", status);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		queryMap.put("assignee", curUserId);
		List<ProcessRun> list = this.processRunService.getAlreadyMattersList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(list, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 已结列表页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toCompleteMatterList")
	public ModelAndView toCompleteMatterList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("workflow/bpm/processRunCompleteMatterList");
	}
	
	/**
	 * 已结列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("completeMatterList")
	public @ResponseBody GridData completeMatterList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String creator = RequestUtil.getString(request, "creator");
		String status = RequestUtil.getString(request, "status");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String curUserId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creator", creator);
		queryMap.put("status", status);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		queryMap.put("assignee", curUserId);
		List<ProcessRun> list = this.processRunService.getCompleteMattersList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(list, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 查看流程详细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("info")
	public ModelAndView info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String curUserId = SecurityContext.getCurrentUserId();
		String runId = RequestUtil.getString(request, "runId");
		String ctxPath = request.getContextPath();
		ProcessRun processRun = this.processRunService.getProcessRunById(runId);
		Map<String, Object> formMap = getFormByProcessRun(processRun, ctxPath);
		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionById(processRun.getDefId());
		boolean isFirst = isFirst(processRun);
		boolean isCanRecover = isCanRecover(processRun, isFirst, curUserId);
		boolean isCanRedo = isCanRedo(processRun, isFirst, curUserId);

		return new ModelAndView("workflow/bpm/processRunInfo")
				.addObject("bpmDefinition", bpmDefinition)
				.addObject("processRun", processRun)
				.addObject("form", formMap.get("form"))
				.addObject("isExtForm", formMap.get("isExtForm"))
				.addObject("isFirst", Boolean.valueOf(isFirst))
				.addObject("isCanRedo", Boolean.valueOf(isCanRedo))
				.addObject("isCanRecover", Boolean.valueOf(isCanRecover));
	}
	
	/**
	 * 撤销确认窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toRecover")
	public ModelAndView toRecover(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String runId = RequestUtil.getString(request, "runId");
		this.processRunService.validCanRecover(runId);//校验是否可以撤销
		ModelAndView mv = new ModelAndView("workflow/bpm/processRunRecover");
		mv.addObject("runId", runId);
		return mv;
	}
	/**
	 * 召回确认窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toRedo")
	public ModelAndView toRedo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String runId = RequestUtil.getString(request, "runId");
		ModelAndView mv = new ModelAndView("workflow/bpm/processRunRedo");
		mv.addObject("runId", runId);
		return mv;
	}
	
	/**
	 * 撤销操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("recover")
	public @ResponseBody void recover(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String runId = RequestUtil.getString(request, "runId");
		String memo = RequestUtil.getString(request, "opinion");
		this.processRunService.recover(runId, memo);
	}
	
	/**
	 * 召回操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("redo")
	public @ResponseBody void redo(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String runId = RequestUtil.getString(request, "runId");
		String memo = RequestUtil.getString(request, "opinion");
		this.processRunService.redo(runId, memo);
	}
	
	/**
	 * 删除确认窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toLogicDel")
	public ModelAndView toLogicDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = RequestUtil.getString(request, "instanceId");
		ModelAndView mv = new ModelAndView("workflow/bpm/processRunLogicDel");
		mv.addObject("instanceId", instanceId);
		return mv;
	}
	
	/**
	 * 逻辑删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("logicDelete")
	public @ResponseBody void logicDelete(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String instanceId = RequestUtil.getString(request, "instanceId");
		String memo = RequestUtil.getString(request, "opinion");
		ProcessRun processRun = this.bpmService.delProcessByInstanceId(instanceId, memo);
		String runId = processRun.getRunId();
        String tmp = "逻辑删除了:" + processRun.getSubject() + ",删除原因!";
        this.bpmRunLogService.addBpmRunLog(runId, BpmRunLog.OPERATOR_TYPE_DELETEINSTANCE, tmp);
	}
	
	/**
	 * 我的请求
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toMyRequestList")
	public ModelAndView toMyRequestList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		return new ModelAndView("workflow/bpm/processRunMyRequestList");
	}
	
	/**
	 * 我的请求列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myRequestList")
	public @ResponseBody GridData myRequestList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String status = RequestUtil.getString(request, "status");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String creatorId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creatorId", creatorId);
		queryMap.put("status", status);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		List<ProcessRun> list = this.processRunService.getMyRequestList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(list, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 我的办结
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toMyCompleteList")
	public ModelAndView toMyCompleteList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		return new ModelAndView("workflow/bpm/processRunMyCompleteList");
	}
	
	/**
	 * 我的办结列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myCompleteList")
	public @ResponseBody GridData myCompleteList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		String status = RequestUtil.getString(request, "status");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String creatorId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creatorId", creatorId);
		queryMap.put("status", status);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		List<ProcessRun> list = this.processRunService.getMyCompleteList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(list, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 我的办结
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toMyDraftList")
	public ModelAndView toMyDraftList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		return new ModelAndView("workflow/bpm/processRunMyDraftList");
	}
	
	/**
	 * 我的办结列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myDraftList")
	public @ResponseBody GridData myDraftList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String processName = RequestUtil.getString(request, "processName");
		String subject = RequestUtil.getString(request, "subject");
		Date startTime =  RequestUtil.getDate(request, "startTime");
		Date endTime =  RequestUtil.getDate(request, "endTime");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+24*1000*60*60);
		}
		String creatorId = SecurityContext.getCurrentUserId();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("processName", processName);
		queryMap.put("subject", subject);
		queryMap.put("creatorId", creatorId);
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		List<ProcessRun> list = this.processRunService.getMyDraftList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(list, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 获取流程节点状态
	 * @param request
	 * @return
	 */
	@RequestMapping("getFlowStatus")
	public @ResponseBody TaskNodeStatus getFlowStatus(HttpServletRequest request) {
		String instanceId = RequestUtil.getString(request, "instanceId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		TaskNodeStatus taskNodeStatus = this.bpmService.getNodeCheckStatusInfo(instanceId, nodeId);
		for (TaskOpinion taskOpinion : taskNodeStatus.getTaskOpinionList())
			if (TaskOpinion.STATUS_CHECKING.equals(taskOpinion.getCheckStatus())) {
				String taskId = taskOpinion.getTaskId();
				ProcessTask processTask = this.processRunService.getTaskByTaskId(taskId);
				String assignee = processTask.getAssignee();
				if (StringUtils.isNotEmpty(assignee) && !"0".equals(assignee)) {
					TaskExeStatus taskExeStatus = new TaskExeStatus();
					String fullname = this.userService.getUserById(processTask.getAssignee()).getYhmc();
					taskExeStatus.setExecutor(fullname);
					taskExeStatus.setExecutorId(assignee);
					boolean isRead = this.taskReadService.isTaskRead(processTask.getId(), assignee);
					taskExeStatus.setRead(isRead);
					taskOpinion.setTaskExeStatus(taskExeStatus);
				} else {
					Set<TaskExecutor> set = this.taskUserService.getCandidateExecutors(processTask.getId());
					List<TaskExeStatus> candidateUserStatusList = new ArrayList<TaskExeStatus>();
					for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();) {
						TaskExecutor taskExe = it.next();
						TaskExeStatus taskExeStatus = new TaskExeStatus();
						taskExeStatus.setExecutorId(taskExe.getExecuteId().toString());
						taskExeStatus.setType(taskExe.getType());
						taskExeStatus.setCandidateUser(taskExe.getExecutor());
						String executorId = taskExe.getExecuteId();
						if ("user".equals(taskExe.getType())) {
							boolean isRead = this.taskReadService.isTaskRead(processTask.getId(),executorId);
							taskExeStatus.setRead(isRead);
						}
						candidateUserStatusList.add(taskExeStatus);
					}
					taskOpinion.setCandidateUserStatusList(candidateUserStatusList);
				}
			}
		return taskNodeStatus;
	}
	/**
	 * 获取流程实例表单
	 * @param processRun
	 * @param ctxPath
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getFormByProcessRun(ProcessRun processRun, String ctxPath) throws Exception {
		Map<String, Object> formMap = new HashMap<String, Object>();
		String businessKey = processRun.getBusinessKey();
		String defId = processRun.getDefId();
		String formUrl = "";
		String formDefId = null;
		boolean isExtForm = false;
		boolean isFormEmpty = false;
		String form = "";
		if (processRun.getStatus().shortValue() != ProcessRun.STATUS_RUNNING) {
			if((StringUtils.isEmpty(processRun.getFormDefId())) && (StringUtils.isNotEmpty(processRun.getBusinessUrl()))) {
				isExtForm = true;
				formUrl = processRun.getBusinessUrl();
			} else {
				formDefId = processRun.getFormDefId();
			}
		} else {
			String nodeId = "";
			List<ProcessTask> taskList = this.processRunService.getTasksByRunId(processRun.getRunId());
			for (ProcessTask task : taskList) {
				if (StringUtils.isNotEmpty(task.getTaskDefinitionKey())) {
					nodeId = task.getTaskDefinitionKey();
					break;
				}
			}
			BpmNodeConfig bpmNodeConfig = this.bpmNodeConfigService.getNodeConfig(defId, nodeId);
			if ((BeanUtils.isEmpty(bpmNodeConfig)) || (StringUtils.isEmpty(bpmNodeConfig.getFormType()))) {
				bpmNodeConfig = this.bpmNodeConfigService.getBySetType(defId, BpmNodeConfig.TYPE_GLOBEL);
			}
			if (BeanUtils.isNotEmpty(bpmNodeConfig)) {
				if (BpmNodeConfig.NODE_FORM_TYPE_URL.equals(bpmNodeConfig.getFormType())) {
					isExtForm = true;
					formUrl = bpmNodeConfig.getFormUrl();
				} else {
					formDefId = bpmNodeConfig.getFormKey();
				}
			}
		}
		if (isExtForm) {
			if (StringUtils.isNotEmpty(businessKey)) {
				form = formUrl.replaceFirst("\\{pk\\}", businessKey);
				if (!formUrl.startsWith("http")) {
					form = ctxPath + form;
				}
			}

		} else if ((processRun.getStatus().shortValue() != 2) && (processRun.getStatus().shortValue() != 3)) {
			if (StringUtils.isNotEmpty(formDefId)) {
				//TODO 获取在线表单
			}
		} else{
			//TODO 获取在线表单
		}

		if (StringUtils.isEmpty(form)) {
			isFormEmpty = true;
		}
		formMap.put("form", form);
		formMap.put("isFormEmpty", Boolean.valueOf(isFormEmpty));
		formMap.put("isExtForm", Boolean.valueOf(isExtForm));
		return formMap;
	}
	
	/**
	 * 审批中的任务是否第一个节点
	 * @param processRun
	 * @return
	 * @throws Exception
	 */
	private boolean isFirst(ProcessRun processRun) throws Exception {
		boolean isFirst = false;
		if (BeanUtils.isEmpty(processRun)) return isFirst;
		String instId = processRun.getActInstId();
		String actDefId = processRun.getActDefId();
		List<TaskOpinion> taskOpinionList = this.taskOpinionService.getCheckOpinionByInstId(instId);
		FlowNode flowNode = NodeCache.getFirstNodeId(actDefId);
		if (flowNode == null)
			return isFirst;
		String nodeId = flowNode.getNodeId();
		for (TaskOpinion taskOpinion : taskOpinionList) {
			isFirst = nodeId.equals(taskOpinion.getTaskKey());
			if (isFirst)
				break;
		}
		return isFirst;
	}
	
	/**
	 * 是否可以撤销,只有满足一下条件的流程实例可以撤销
	 * 1.流程定义未禁用
	 * 2.当前审批节点不是第一个节点
	 * 3.当前用户是流程实例创建人
	 * 4.流程实例处于运行状态
	 * @param processRun
	 * @param isFirst
	 * @param curUserId
	 * @return
	 */
	private boolean isCanRecover(ProcessRun processRun, boolean isFirst, String curUserId) {
		String actDefId = processRun.getActDefId();
		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
		if (BpmDefinition.STATUS_UNDEPLOYED.toString().equals(bpmDefinition.getStatus()) || CommonConst.NO.equals(bpmDefinition.getUsable())) return false;
		return (!isFirst) && (curUserId.equals(processRun.getCreatorId()))
							&& (processRun.getStatus().shortValue() == ProcessRun.STATUS_RUNNING.shortValue());
	}
	
	/**
	 * 是否可以召回,只有满足以下条件的流程实例可以召回：
	 * 1.流程定义未禁用
	 * 2.流程实例处于运行状态
	 * 3.流程实例当前节点不是第一个节点
	 * 4.节点执行完后、下一节点执行前，可以收回进行修改然后再提交。
	 * @param processRun
	 * @param isFirst
	 * @param curUserId
	 * @return
	 */
	private boolean isCanRedo(ProcessRun processRun, boolean isFirst, String curUserId) {
		if ((!processRun.getStatus().equals(ProcessRun.STATUS_RUNNING)) || (isFirst))
			return false;
		String actDefId = processRun.getActDefId();
		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);
		if (BpmDefinition.STATUS_UNDEPLOYED.equals(bpmDefinition.getStatus()) || CommonConst.NO.equals(bpmDefinition.getUsable())) return false;
		String instanceId = processRun.getActInstId();
		TaskOpinion taskOpinion = this.taskOpinionService.getLatestUserOpinion( instanceId, curUserId);
		if (taskOpinion != null) {
			Short checkStatus = taskOpinion.getCheckStatus();
			if (TaskOpinion.STATUS_AGREE.equals(checkStatus)) {
				String taskKey = taskOpinion.getTaskKey();
				FlowNode flowNode = NodeCache.getNodeByActNodeId(processRun.getActDefId(), taskKey);
				if (flowNode != null) {
					List<FlowNode> nextNodes = flowNode.getNextFlowNodes();
					List<String> nodeKeys = new ArrayList<String>();
					for (FlowNode node : nextNodes) {
						nodeKeys.add(node.getNodeId());
					}
					List<ProcessTask> tasks = this.bpmService.getTasks(instanceId);
					if (tasks.size() != 1)
						return false;
					if (nodeKeys.contains(((ProcessTask) tasks.get(0)).getTaskDefinitionKey())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}