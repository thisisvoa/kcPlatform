/**
 * @(#)com.casic27.platform.common.log.action.LogonLogController
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
package com.casic27.platform.common.log.action;

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

import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.common.log.entity.LogonLog;
import com.casic27.platform.common.log.service.LogonLogService;

@Controller
@RequestMapping("logonLog")
public class LogonLogController extends BaseMultiActionController{
	@Autowired
	private LogonLogService logonLogService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("common/log/logonLogMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("logonLogList")
	public @ResponseBody GridData logonLogList(HttpServletRequest request){
	  	String terminalId = StringUtils.getNullBlankStr(request.getParameter("terminalId"));
	  	String orgNo = StringUtils.getNullBlankStr(request.getParameter("orgNo"));
	  	String orgName = StringUtils.getNullBlankStr(request.getParameter("orgName"));
	  	String idCard = StringUtils.getNullBlankStr(request.getParameter("idCard"));
	  	String policeId = StringUtils.getNullBlankStr(request.getParameter("policeId"));
	  	String userName = StringUtils.getNullBlankStr(request.getParameter("userName"));
	  	String loginId = StringUtils.getNullBlankStr(request.getParameter("loginId"));
		String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+999);
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("terminalId", terminalId);
	 	queryMap.put("orgNo", orgNo);
	 	queryMap.put("orgName", orgName);
	 	queryMap.put("idCard", idCard);
	 	queryMap.put("policeId", policeId);
	 	queryMap.put("userName", userName);
	 	queryMap.put("loginId", loginId);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
		List<LogonLog> result = logonLogService.findLogonLogPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("logonLogListByTable")
	public @ResponseBody GridData logonLogListByTable(HttpServletRequest request){
	  	String terminalId = StringUtils.getNullBlankStr(request.getParameter("terminalId"));
	  	String orgNo = StringUtils.getNullBlankStr(request.getParameter("orgNo"));
	  	String orgName = StringUtils.getNullBlankStr(request.getParameter("orgName"));
	  	String idCard = StringUtils.getNullBlankStr(request.getParameter("idCard"));
	  	String policeId = StringUtils.getNullBlankStr(request.getParameter("policeId"));
	  	String userName = StringUtils.getNullBlankStr(request.getParameter("userName"));
	  	String loginId = StringUtils.getNullBlankStr(request.getParameter("loginId"));
		String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
		if(endTime!=null){
			endTime = new Date(endTime.getTime()+999);
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("terminalId", terminalId);
	 	queryMap.put("orgNo", orgNo);
	 	queryMap.put("orgName", orgName);
	 	queryMap.put("idCard", idCard);
	 	queryMap.put("policeId", policeId);
	 	queryMap.put("userName", userName);
	 	queryMap.put("loginId", loginId);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
	 	queryMap.put("tableName", tableName);
		List<LogonLog> result = logonLogService.findLogonLogByTablePaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		LogonLog logonLog = logonLogService.getLogonLogById(id);
		if(logonLog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/logonLogView")
					.addObject("logonLog",logonLog);
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toViewLogonLog")
	public ModelAndView toViewLogonLog(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("id", id);
		queryMap.put("tableName", tableName);
		LogonLog logonLog = logonLogService.getLogonLogByTable(queryMap);
		if(logonLog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/logonLogView")
					.addObject("logonLog",logonLog);
	}
}