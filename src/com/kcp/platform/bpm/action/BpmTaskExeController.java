package com.kcp.platform.bpm.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.bpm.entity.BpmRunLog;
import com.kcp.platform.bpm.entity.BpmTaskExe;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.service.BpmRunLogService;
import com.kcp.platform.bpm.service.BpmService;
import com.kcp.platform.bpm.service.BpmTaskExeService;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;

@Controller
@RequestMapping("workflow/bpm/taskExe")
public class BpmTaskExeController extends BaseMultiActionController{
	
	@Autowired
	private BpmTaskExeService bpmTaskExeService;
	
	@Autowired
	private BpmService bpmService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private BpmRunLogService bpmRunLogService;
	/**
	 * 跳转至我的代理事宜页面
	 */
	@RequestMapping("toAccordingMattersList")
	public ModelAndView toAccordingMattersList(HttpServletRequest request){
		return new ModelAndView("workflow/bpm/bpmTaskExeAccordingMattersList");
	}
	
	@ResponseBody
	@RequestMapping("accordingMattersList")
	public GridData accordingMattersList(HttpServletRequest request)throws Exception{
		String assigneeId = RequestUtil.getString(request, "assigneeId");
		String ownerId = SecurityContext.getCurrentUserId();
		String subject = RequestUtil.getString(request, "subject");
		String processName = RequestUtil.getString(request, "processName");
		Integer status = RequestUtil.getInt(request, "status", -1);
		Date beginCreateTime = RequestUtil.getDate(request, "beginCreateTime");
		Date endCreateTime = RequestUtil.getDate(request, "endCreateTime");
		endCreateTime = DateUtils.getDateEndTime(endCreateTime);
		Integer assignType = RequestUtil.getInt(request, "assignType", -1);
		String creatorId = RequestUtil.getString(request, "creatorId");
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("assigneeId", assigneeId);
		queryMap.put("ownerId", ownerId);
		queryMap.put("subject", subject);
		queryMap.put("processName", processName);
		queryMap.put("status", status);
		queryMap.put("beginCreateTime", beginCreateTime);
		queryMap.put("endCreateTime", endCreateTime);
		queryMap.put("assignType", assignType);
		queryMap.put("creatorId", creatorId);
		List<BpmTaskExe> result = bpmTaskExeService.accordingMattersList(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	@RequestMapping("toCancle")
	public ModelAndView toCancle(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("workflow/bpm/bpmTaskExeCancle");
	}
	/**
	 * 取消任务代理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("cancel")
	public @ResponseBody void cancel(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = RequestUtil.getString(request, "id");
		String opinion = RequestUtil.getString(request, "opinion");
	
		BpmTaskExe bpmTaskExe =  this.bpmTaskExeService.getBpmTaskExeById(id);
		String taskId = bpmTaskExe.getTaskId();
		TaskEntity taskEntity = this.bpmService.getTask(taskId.toString());
		if (taskEntity == null) {
			throw new BusinessException("任务已经结束！");
		}
		
		String ownerId = bpmTaskExe.getOwnerId();
		if (ownerId == null) {
			throw new BusinessException("找不到原来的任务执行人！");
		}
		User sysUser = this.userService.getUserById(ownerId);
		if (sysUser == null) {
			throw new BusinessException("找不到原来的任务执行人！");
		}
		this.bpmTaskExeService.cancel(bpmTaskExe, sysUser, opinion);
		ProcessRun processRun = this.processRunService.getProcessRunByActInstId(bpmTaskExe.getActInstId());
		String memo = "流程:" + processRun.getSubject() + ", 【" + sysUser.getYhmc() + "】将转办任务【"+ bpmTaskExe.getSubject() + "】 取消";
		this.bpmRunLogService.addBpmRunLog(processRun.getRunId(), BpmRunLog.OPERATOR_TYPE_BACK, memo);
  }
	
	/**
	 * 批量取消代理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("cancelBat")
	public @ResponseBody void cancelBat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User curUser = SecurityContext.getCurrentUser();
		String idStr = RequestUtil.getString(request, "ids");
		String opinion = RequestUtil.getString(request, "opinion");
		if (StringUtils.isEmpty(idStr)) {
			throw new BusinessException("请选择需要取消转办的项目!");
		}
		List<BpmTaskExe> list = this.bpmTaskExeService.cancelBat(idStr.split(","), opinion, curUser);
		addRunLong(list);
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	public @ResponseBody void del(HttpServletRequest request){
		String id = RequestUtil.getString(request, "id");
		bpmTaskExeService.delById(id);
	}
	
	/**
	 * 添加日志
	 * @param list
	 */
	private void addRunLong(List<BpmTaskExe> list) {
		User curUser = SecurityContext.getCurrentUser();
		for (BpmTaskExe bpmTaskExe : list) {
			String memo = "【" + curUser.getYhmc() + "】将转办任务【" + bpmTaskExe.getSubject() + "】 取消";
			this.bpmRunLogService.addBpmRunLog(bpmTaskExe.getRunId(), BpmRunLog.OPERATOR_TYPE_BACK, memo);
		}
	}
}
