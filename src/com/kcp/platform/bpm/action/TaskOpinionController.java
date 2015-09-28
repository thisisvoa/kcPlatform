package com.kcp.platform.bpm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.entity.TaskOpinion;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.bpm.service.TaskOpinionService;

@Controller
@RequestMapping("workflow/bpm/taskOpinion")
public class TaskOpinionController extends BaseMultiActionController{
	@Autowired
	private TaskOpinionService bpmTaskOpinionService;
	
	@Autowired
	private ProcessRunService processRunService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toList")
	public ModelAndView toMain(HttpServletRequest request){
		String runId = RequestUtil.getString(request, "runId");
		ProcessRun processRun = processRunService.getProcessRunById(runId);
		if(processRun==null){
			throw new BusinessException("流程实例不存在！");
		}
		return new ModelAndView("workflow/bpm/taskOpinionList")
					.addObject("processRun", processRun);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmTaskOpinionList(HttpServletRequest request){
		String actInstId = RequestUtil.getString(request, "actInstId");
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("actInstId", actInstId);
		List<TaskOpinion> result = bpmTaskOpinionService.getByActInstId(actInstId);
		return new GridData(result);
	}
}