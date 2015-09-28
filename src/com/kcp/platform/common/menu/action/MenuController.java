package com.kcp.platform.common.menu.action;

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

import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.menu.entity.SysMenu;
import com.kcp.platform.common.menu.service.MenuService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.ExcelWriter;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
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
		SysMenu menu = new SysMenu();
		menu.setParentMenuId(parentId);
		menu.setMenuIndex(menuService.queryMaxCdxh(parentId));
		menu.setIsLast("0");
		menu.setIsUsed(CommonConst.ENABLE_FLAG);
		model.addAttribute("menu", menu);
		model.addAttribute("type", "add");
		return "common/menu/menuDetail";
	}
	
	@RequestMapping("/menuEdit")
	public String menuEdit(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		SysMenu menuMap = new SysMenu();
		if (StringUtils.isNotBlank(zjId)) {
			menuMap.setId(zjId);
			List<SysMenu> resultList = menuService.queryMenuList(menuMap);
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
		SysMenu menuMap = new SysMenu();
		if (StringUtils.isNotBlank(zjId)) {
			menuMap.setId(zjId);
			List<SysMenu> resultList = menuService.queryMenuList(menuMap);
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
		
		SysMenu menuMap;
		if ("add".equals(type)) {
			menuMap = new SysMenu();
			setMap(menuMap, request);
			menuMap.setIsUsed("1");
			menuMap.setCreateTime(DateUtils.getCurrentDateTime());
			menuMap.setUpdateTime(DateUtils.getCurrentDateTime());
			menuMap.setStatus("1");
			menuService.insert(menuMap);
		} else if ("edit".equals(type)) {
			String zjid = request.getParameter("zjId");
			menuMap = menuService.getMenuById(zjid);
			setMap(menuMap, request);
			menuMap.setUpdateTime(DateUtils.getCurrentDateTime());
			menuService.update(menuMap);
			if(CommonConst.NO.equals(menuMap.getIsUsed())){
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
	private void setMap(SysMenu menuMap, HttpServletRequest request) {
		menuMap.setMenuName(StringUtils.getNullBlankStr(request.getParameter("menu_name")));
		menuMap.setMenuAddr(StringUtils.getNullBlankStr(request.getParameter("menu_addr")));
		menuMap.setMenuLevel(StringUtils.getNullBlankStr(request.getParameter("menu_level")));
		menuMap.setParentMenuId(StringUtils.getNullBlankStr(request.getParameter("parent_menu_id")));
		menuMap.setIsLast(StringUtils.getNullBlankStr(request.getParameter("is_last")));
		menuMap.setMenuIndex(StringUtils.getNullBlankStr(request.getParameter("menu_index")));
		menuMap.setIsUsed(StringUtils.getNullBlankStr(request.getParameter("is_used")));
		menuMap.setMemo(StringUtils.getNullBlankStr(request.getParameter("memo")));
	}
	
	/**
	 * 异步加载单位树
	 * @param model
	 * @param request 根据单位ID(后期数据权限扩展--当前用户所属单位Id)
	 * @return
	 */
	@RequestMapping("loadAllMenu")
	public @ResponseBody List<SysMenu> loadAllMenu(HttpServletRequest request, HttpServletResponse response) throws Exception{
		SysMenu menu = new SysMenu();
		menu.setStatus("1");
		List<SysMenu> menuList = menuService.queryMenuList(menu);
		SysMenu m = new SysMenu();
		m.setId("0");
		m.setMenuName("菜单树");
		menuList.add(m);
		return menuList;
	}
	
	/**
	 * 获取上级菜单 AJAX
	 * @param model
	 * @param request
	 */
	@RequestMapping("/loadAllEnableMenu")
	public @ResponseBody List<SysMenu> loadAllEnableMenu(Model model, HttpServletRequest request) {
		SysMenu menu = new SysMenu();
		menu.setIsUsed(CommonConst.ENABLE_FLAG);
		menu.setStatus(CommonConst.ENABLE_FLAG);
		List<SysMenu> menuList = menuService.queryMenuList(menu);
		SysMenu m = new SysMenu();
		m.setId("0");
		m.setMenuName("菜单树");
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
		SysMenu menu = menuService.getMenuById(id);
		menuService.logicDelMenu(menu);
	}
}
