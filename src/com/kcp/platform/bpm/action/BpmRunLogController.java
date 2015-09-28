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
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmRunLog;
import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.service.BpmRunLogService;
import com.kcp.platform.bpm.service.ProcessRunService;

@Controller
@RequestMapping("workflow/bpm/runLog")
public class BpmRunLogController extends BaseMultiActionController{
	@Autowired
	private BpmRunLogService bpmRunLogService;
	
	@Autowired
	private ProcessRunService processRunService;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toList")
	public ModelAndView toMain(HttpServletRequest request){
		String runId = RequestUtil.getString(request, "runId");
		ProcessRun processRun = processRunService.getProcessRunById(runId);
		return new ModelAndView("workflow/bpm/bpmRunLogList")
							.addObject("processRun", processRun);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmRunLogList(HttpServletRequest request){
	  	String runId = StringUtils.getNullBlankStr(request.getParameter("runId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("runId", runId);
		List<BpmRunLog> result = bpmRunLogService.findBpmRunLogPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	
	/**
	 * 删除操作
	 */
	@RequestMapping("del")
	public @ResponseBody void deleteBpmRunLog(HttpServletRequest request){
		String[] ids = RequestUtil.getStringAryByStr(request, "ids");
		bpmRunLogService.delByIds(ids);
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmRunLog bpmRunLog = bpmRunLogService.getBpmRunLogById(id);
		if(bpmRunLog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmRunLogView")
					.addObject("bpmRunLog",bpmRunLog);
	}
}