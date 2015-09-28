package com.kcp.platform.common.log.action;

import java.util.Date;
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
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.common.log.entity.InterfaceLog;
import com.kcp.platform.common.log.service.InterfaceLogService;

@Controller
@RequestMapping("interfaceLog")
public class InterfaceLogController extends BaseMultiActionController{
	@Autowired
	private InterfaceLogService interfaceLogService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("common/log/interfaceLogMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("interfaceLogList")
	public @ResponseBody GridData interfaceLogList(HttpServletRequest request){
	  	String callerName = StringUtils.getNullBlankStr(request.getParameter("callerName"));
	  	String terminalId = StringUtils.getNullBlankStr(request.getParameter("terminalId"));
	  	String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+999);
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("callerName", callerName);
	 	queryMap.put("terminalId", terminalId);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
		List<InterfaceLog> result = interfaceLogService.findInterfaceLogPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("interfaceLogListByTable")
	public @ResponseBody GridData interfaceLogListByTable(HttpServletRequest request){
	  	String callerName = StringUtils.getNullBlankStr(request.getParameter("callerName"));
	  	String terminalId = StringUtils.getNullBlankStr(request.getParameter("terminalId"));
	  	String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+999);
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("callerName", callerName);
	 	queryMap.put("terminalId", terminalId);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
	 	queryMap.put("tableName", tableName);
		List<InterfaceLog> result = interfaceLogService.findInterfaceLogByTablePaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		InterfaceLog interfaceLog = interfaceLogService.getInterfaceLogById(id);
		if(interfaceLog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/interfaceLogView")
					.addObject("interfaceLog",interfaceLog);
	}
	
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toViewInterfaceLog")
	public ModelAndView toViewInterfaceLog(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("id", id);
		queryMap.put("tableName", tableName);
		InterfaceLog interfaceLog = interfaceLogService.getInterfaceLogByTable(queryMap);
		if(interfaceLog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/interfaceLogView")
					.addObject("interfaceLog",interfaceLog);
	}
}