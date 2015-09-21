/**
 * @(#)com.casic27.platform.bpm.action.TaskOpinionController
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.RequestUtil;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.bpm.entity.TaskOpinion;
import com.casic27.platform.bpm.service.ProcessRunService;
import com.casic27.platform.bpm.service.TaskOpinionService;

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