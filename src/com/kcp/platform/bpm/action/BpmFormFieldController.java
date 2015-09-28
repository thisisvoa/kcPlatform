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
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmFormField;
import com.kcp.platform.bpm.service.BpmFormFieldService;

@Controller
@RequestMapping("workflow/formField")
public class BpmFormFieldController extends BaseMultiActionController{
	@Autowired
	private BpmFormFieldService bpmFormFieldService;

	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmFormFieldList(HttpServletRequest request){
	  	String tableId = StringUtils.getNullBlankStr(request.getParameter("tableId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("tableId", tableId);
		List<BpmFormField> result = bpmFormFieldService.findBpmFormField(queryMap);
		return new GridData(result);
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormFieldEdit")
					.addObject("type","add");
	}
	
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormFieldEdit")
					.addObject("type","update");
	}
	
	@RequestMapping("getFieldByTable")
	public @ResponseBody List<BpmFormField> getFieldByTable(HttpServletRequest request){
		String tableId = StringUtils.getNullBlankStr(request.getParameter("tableId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("tableId", tableId);
		List<BpmFormField> result = bpmFormFieldService.findBpmFormField(queryMap);
		return result;
	}
}