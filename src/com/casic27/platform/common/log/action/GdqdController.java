/**
 * @(#)com.casic27.platform.common.log.action.GdqdController
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
import com.casic27.platform.common.log.entity.Gdqd;
import com.casic27.platform.common.log.service.GdqdService;

@Controller
@RequestMapping("gdqd")
public class GdqdController extends BaseMultiActionController{
	@Autowired
	private GdqdService gdqdService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("common/log/gdqdMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("gdqdList")
	public @ResponseBody GridData gdqdList(HttpServletRequest request){
	  	String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
	  	String gdhbmc = StringUtils.getNullBlankStr(request.getParameter("gdhbmc"));
		String gdmc = StringUtils.getNullBlankStr(request.getParameter("gdmc"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("gdmc", gdmc);
		queryMap.put("gdbmc", gdbmc);
	 	queryMap.put("gdhbmc", gdhbmc);
		List<Gdqd> result = gdqdService.findGdqdPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("gdqdTableList")
	public @ResponseBody GridData gdqdTableList(HttpServletRequest request){
	  	String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
	  	String gdhbmc = StringUtils.getNullBlankStr(request.getParameter("gdhbmc"));
		String gdmc = StringUtils.getNullBlankStr(request.getParameter("gdmc"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("gdmc", gdmc);
		queryMap.put("gdbmc", gdbmc);
	 	queryMap.put("gdhbmc", gdhbmc);
		List<Gdqd> result = gdqdService.findGdqdPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("getGdqdList")
	public @ResponseBody List<Gdqd> getGdqdList(HttpServletRequest request){
	  	String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
	  	String gdhbmc = StringUtils.getNullBlankStr(request.getParameter("gdhbmc"));
		String gdmc = StringUtils.getNullBlankStr(request.getParameter("gdmc"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("gdmc", gdmc);
		queryMap.put("gdbmc", gdbmc);
	 	queryMap.put("gdhbmc", gdhbmc);
		List<Gdqd> result = gdqdService.findGdqd(queryMap);
		return result;
	}
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("common/log/gdqdEdit")
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("addGdqd")
	public void addGdqd(HttpServletRequest request){
		Gdqd gdqd = assembleGdqd(request);
		gdqdService.addGdqd(gdqd);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		Gdqd gdqd = gdqdService.getGdqdById(id);
		if(gdqd==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/gdqdEdit")
					.addObject("gdqd",gdqd)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("updateGdqd")
	public void updateGdqd(HttpServletRequest request){
		Gdqd gdqd = assembleGdqd(request);
		gdqdService.updateGdqd(gdqd);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteGdqd")
	public void deleteGdqd(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				gdqdService.deleteGdqd(id);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		Gdqd gdqd = gdqdService.getGdqdById(id);
		if(gdqd==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/gdqdView")
					.addObject("gdqd",gdqd);
	}
	

	
	/**
	 * 组装页面传递过来的参数
	 */
	private Gdqd assembleGdqd(HttpServletRequest request){
		String zjid = StringUtils.getNullBlankStr(request.getParameter("zjid"));
		String gdpzZjid = StringUtils.getNullBlankStr(request.getParameter("gdpzZjid"));
		String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
		String gdhbmc = StringUtils.getNullBlankStr(request.getParameter("gdhbmc"));
		String gdsjKssjStr = StringUtils.getNullBlankStr(request.getParameter("gdsjKssj"));
		Date gdsjKssj = DateUtils.parseStrint2Date(gdsjKssjStr,"yyyy-MM-dd");
		String gdsjJssjStr = StringUtils.getNullBlankStr(request.getParameter("gdsjJssj"));
		Date gdsjJssj = DateUtils.parseStrint2Date(gdsjJssjStr,"yyyy-MM-dd");
		int gdjls = Integer.parseInt(request.getParameter("gdjls"));
		String jlcjsjStr = StringUtils.getNullBlankStr(request.getParameter("jlcjsj"));
		Date jlcjsj = DateUtils.parseStrint2Date(jlcjsjStr,"yyyy-MM-dd");
		Gdqd gdqd = new Gdqd();
		gdqd.setZjid(zjid);
		gdqd.setGdpzZjid(gdpzZjid);
		gdqd.setGdbmc(gdbmc);
		gdqd.setGdhbmc(gdhbmc);
		gdqd.setGdsjKssj(gdsjKssj);
		gdqd.setGdsjJssj(gdsjJssj);
		gdqd.setGdjls(gdjls);
		gdqd.setJlcjsj(jlcjsj);
		return gdqd;
	}
	
	
	/**
	 * 跳转至表页面
	 */
	@RequestMapping("toTableSelect")
	public ModelAndView toTableSelect(HttpServletRequest request){
		String cxmc = StringUtils.getNullBlankStr(request.getParameter("cxmc"));
		String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
		return new ModelAndView("common/log/tableSelect")
					.addObject("cxmc",cxmc)
					.addObject("gdbmc",gdbmc);
	}
}