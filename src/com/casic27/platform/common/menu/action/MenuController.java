/**
 * @(#)com.casic27.jcxxgl.action.cdxxglAction.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-19
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.menu.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.common.log.Logger;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.log.core.SqlContextHolder;
import com.casic27.platform.common.menu.entity.Menu;
import com.casic27.platform.common.menu.service.MenuService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.ExcelWriter;
import com.casic27.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 *@Author： 施阳钦(shiyangqin@casic27.com)
 *@Version：1.0
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseMultiActionController{
	@Autowired
	private MenuService menuService;
	
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@RequestMapping("/menu_main")
	public String menuMain(Model model, HttpServletRequest request) {
		return "common/menu/menuMain";
	}
		
	@RequestMapping("/menuAdd")
	public String menuAdd(Model model, HttpServletRequest request) {
		String parentId = request.getParameter("parentId");
		if(StringUtils.isEmpty(parentId)){
			parentId = "0";
		}
		Menu menu = new Menu();
		menu.setSjcd(parentId);
		menu.setCdxh(menuService.queryMaxCdxh(parentId));
		menu.setSfzhyicd("0");
		menu.setSybz(CommonConst.ENABLE_FLAG);
		model.addAttribute("menu", menu);
		model.addAttribute("type", "add");
		return "common/menu/menuDetail";
	}
	
	@RequestMapping("/menuEdit")
	public String menuEdit(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		Menu menuMap = new Menu();
		if (StringUtils.isNotBlank(zjId)) {
			menuMap.setZjId(zjId);
			List<Menu> resultList = menuService.queryMenuList(menuMap);
			model.addAttribute("menu", resultList.get(0));
		}
		model.addAttribute("type", "edit");
		return menuDetail(model, request, false);
	}
	
	/**
	 * 开启明细页面
	 * 
	 * @param model
	 * @param request
	 */
	private String menuDetail(Model model, HttpServletRequest request, Boolean show) {
		String zjId = request.getParameter("zjId");
		Menu menuMap = new Menu();
		if (StringUtils.isNotBlank(zjId)) {
			menuMap.setZjId(zjId);
			List<Menu> resultList = menuService.queryMenuList(menuMap);
			if (resultList != null && !resultList.isEmpty()) {
				model.addAttribute("menu", resultList.get(0));
			}
			if (show) {
				if (resultList != null && !resultList.isEmpty()) {
					model.addAttribute("msg", CommonConst.OPER_SUCCESS);
				} else {
					model.addAttribute("msg", CommonConst.OPER_FAIL);
				}
			}
		}
		return "common/menu/menuDetail";
	}
	
	@RequestMapping("/menuSave")
	public @ResponseBody void menuSave(Model model, HttpServletRequest request) {
		String type = request.getParameter("type");
		model.addAttribute("type", type);
		model.addAttribute("show", "Y");
		
		Menu menuMap;
		if ("add".equals(type)) {
			menuMap = new Menu();
			setMap(menuMap, request);
			menuMap.setSybz("1");
			menuMap.setJlxzsj(DateUtils.getCurrentDateTime14());
			menuMap.setJlxgsj(DateUtils.getCurrentDateTime14());
			menuMap.setJlyxzt("1");
			menuService.insert(menuMap);
		} else if ("edit".equals(type)) {
			String zjid = request.getParameter("zjId");
			menuMap = menuService.getMenuById(zjid);
			setMap(menuMap, request);
			menuMap.setJlxgsj(DateUtils.getCurrentDateTime14());
			menuService.update(menuMap);
			if(CommonConst.NO.equals(menuMap.getSybz())){
				menuService.updateChildrenSybz(menuMap);
			}else{
				menuService.updateParentSybz(menuMap);
			}
		}else{
			throw new BusinessException("未知操作类型!");
		}
	}

	/**
	 * 设定 Map 内容
	 * 
	 * @param funcMap
	 * @param request
	 */
	private void setMap(Menu menuMap, HttpServletRequest request) {
		menuMap.setCdmc(StringUtils.getNullBlankStr(request.getParameter("cdmc")));
		menuMap.setCddz(StringUtils.getNullBlankStr(request.getParameter("cddz")));
		menuMap.setCdjb(StringUtils.getNullBlankStr(request.getParameter("cdjb")));
		menuMap.setSjcd(StringUtils.getNullBlankStr(request.getParameter("sjcd")));
		menuMap.setSfzhyicd(StringUtils.getNullBlankStr(request.getParameter("sfzhyicd")));
		menuMap.setCdxh(StringUtils.getNullBlankStr(request.getParameter("cdxh")));
		menuMap.setSybz(StringUtils.getNullBlankStr(request.getParameter("sybz")));
		menuMap.setBz(StringUtils.getNullBlankStr(request.getParameter("bz")));
	}
	
	/**
	 * 异步加载单位树
	 * @param model
	 * @param request 根据单位ID(后期数据权限扩展--当前用户所属单位Id)
	 * @return
	 */
	@RequestMapping("loadAllMenu")
	public @ResponseBody List<Menu> loadAllMenu(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Menu menu = new Menu();
		menu.setJlyxzt("1");
		List<Menu> menuList = menuService.queryMenuList(menu);
		Menu m = new Menu();
		m.setZjId("0");
		m.setCdmc("菜单树");
		menuList.add(m);
		return menuList;
	}
	
	/**
	 * 获取上级菜单 AJAX
	 * @param model
	 * @param request
	 */
	@RequestMapping("/loadAllEnableMenu")
	public @ResponseBody List<Menu> loadAllEnableMenu(Model model, HttpServletRequest request) {
		Menu menu = new Menu();
		menu.setSybz(CommonConst.ENABLE_FLAG);
		menu.setJlyxzt(CommonConst.ENABLE_FLAG);
		List<Menu> menuList = menuService.queryMenuList(menu);
		Menu m = new Menu();
		m.setZjId("0");
		m.setCdmc("菜单树");
		menuList.add(m);
		return menuList;
	}
	
	@RequestMapping("/menuAuthExport")
	public ModelAndView menuAuthExport(HttpServletRequest request){
		return new ModelAndView("common/menu/MenuAuthExport");
	}
	
	@RequestMapping("/authExport")
	public @ResponseBody void authExport(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String selMenus = StringUtils.getNullBlankStr(request.getParameter("selMenus"));
		List<Map<String,Object>> dataList = menuService.getAuthInfo(selMenus.split(","));
		List<List<String>> excelData = new ArrayList<List<String>>();
		List<String> header = new ArrayList<String>();
		header.add("功能名称");
		header.add("人员ID");
		header.add("人员姓名");
		header.add("所在单位");
		header.add("警号");
		header.add("身份证号");
		excelData.add(header);
		if(dataList!=null){
			for(Map<String,Object> data:dataList){
				List<String> xlsData = new ArrayList<String>();
				xlsData.add((String)data.get("CDMC"));
				xlsData.add((String)data.get("YHID"));
				xlsData.add((String)data.get("YHMC"));
				xlsData.add((String)data.get("DWMC"));
				xlsData.add((String)data.get("JYBH"));
				xlsData.add((String)data.get("SFZH"));
				excelData.add(xlsData);
			}
		}
		ExcelWriter.exportExcelData("菜单权限信息("+DateUtils.getCurrentDate8()+").xls",null, excelData,response);
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.EXPORT.value(), sql, "菜单信息管理", "[导出] 导出ID为["+selMenus+"]的菜单的授权信息");
	}
	
	@RequestMapping("/queryMaxCdxh")
	public @ResponseBody String queryMaxCdxh(HttpServletRequest request){
		String parentId = request.getParameter("parentId");
		String cdxh = menuService.queryMaxCdxh(parentId);
		if(StringUtils.isEmpty(cdxh)){
			cdxh = "1";
		}
		return cdxh;
	}
	
	@RequestMapping("/delMenu")
	public @ResponseBody void delMenu(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		Menu menu = menuService.getMenuById(id);
		menuService.logicDelMenu(menu);
	}
}
