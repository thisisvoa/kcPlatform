package com.kcp.platform.common.func.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.func.service.FuncService;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.menu.entity.SysMenu;
import com.kcp.platform.common.menu.service.MenuService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
@Controller
@RequestMapping("/func")
public class FuncController extends BaseMultiActionController{
	@Autowired
	private FuncService funcService;
	
	@Autowired
	private MenuService menuService;
	public FuncService getFuncService() {
		return funcService;
	}
	public void setFuncService(FuncService funcService) {
		this.funcService = funcService;
	}

	@RequestMapping("/func_main")
	public String funcMain(Model model, HttpServletRequest request) {
		return "common/func/funcMain";
	}
		
	@RequestMapping("/funcFrame")
	public String paraFrame(Model model, HttpServletRequest request) {
		model.addAttribute("id", request.getParameter("id"));
		return "common/func/funcFrame";
	}

	@RequestMapping("/funcList")
	public String funcList(Model model, HttpServletRequest request) {
		model.addAttribute("res", request.getParameter("res"));
		model.addAttribute("id", request.getParameter("id"));
		return "common/func/funcList";
	}
	
	@RequestMapping("/funcListData")
	public @ResponseBody GridData funcListData(HttpServletRequest request){
		Function funcMap = new Function();
		String belongMenu = com.kcp.platform.util.StringUtils.getNullBlankStr(getURLDecode(request.getParameter("belongMenu")));
		String funName = com.kcp.platform.util.StringUtils.getNullBlankStr(getURLDecode(request.getParameter("funName")));
		String useFlag = com.kcp.platform.util.StringUtils.getNullBlankStr(getURLDecode(request.getParameter("useFlag")));
		String id = com.kcp.platform.util.StringUtils.getNullBlankStr(getURLDecode(request.getParameter("id")));
		funcMap.setCdxxCdmc(com.kcp.platform.util.StringUtils.escapeSqlLike(belongMenu));
		funcMap.setGnmc(com.kcp.platform.util.StringUtils.escapeSqlLike(funName));
		if(useFlag != null && !"".equals(useFlag.trim())){
			funcMap.setSybz(useFlag);
		}
		funcMap.setJlyxzt("1");
		funcMap.setCdxxZjId(id);
		List<Function> resultList = funcService.queryFuncList(funcMap);
		Page page = PageContextHolder.getPage();
		GridData gridData = new GridData(resultList,page.getPage(), page.getTotalPages(), page.getTotalItems());
		return gridData;
	}
	@RequestMapping("/funcAdd")
	public String funcAdd(Model model, HttpServletRequest request) {
		Function funcMap = null;
		// 1)取得功能序号
		funcMap = new Function();
		funcMap.setCdxxZjId(request.getParameter("menuId"));
		List<Function> resultList = funcService.queryFuncList(funcMap);
		String gnxh = "" + (resultList.size() + 1);
		// 2)初始值
		funcMap.setGnxh(gnxh);
		funcMap.setSybz("1");// 使用标识：启用
		model.addAttribute("func", funcMap);
		model.addAttribute("type", "add");
		return "common/func/funcDetail";
	}
	
	@RequestMapping("/funcEdit")
	public String funcEdit(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		Function funcMap = new Function();
		if (StringUtils.isNotBlank(zjId)) {
			funcMap.setZjId(zjId);
			List<Function> resultList = funcService.queryFuncList(funcMap);
			model.addAttribute("func", resultList.get(0));
		}
		model.addAttribute("type", "edit");
		return "common/func/funcDetail";
	}
	
	@RequestMapping("/funcView")
	public String funcView(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		Function funcMap = funcService.getFuncById(zjId);
		SysMenu menu = menuService.getMenuById(funcMap.getCdxxZjId());
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "功能信息管理", "[查看] 查看[功能名称："+funcMap.getGnmc()+"] [功能代码："+funcMap.getGndm()+"]的功能信息");
		model.addAttribute("func", funcMap);
		model.addAttribute("menu", menu);
		return "common/func/funcView";
	}
	
	@RequestMapping("/funcSave")
	public @ResponseBody int funcSave(Model model, HttpServletRequest request) {
		int succFlag = 1;
		String type = request.getParameter("type");
		model.addAttribute("type", type);
		try{
			Function funcMap = new Function();
			if ("add".equals(type)) {
				setMap(funcMap, request);
				funcMap.setJlxzsj(DateUtils.getCurrentDateTime14());
				funcMap.setJlxgsj(DateUtils.getCurrentDateTime14());
				funcMap.setJlyxzt("1");
				funcService.insert(funcMap);
			} else if ("edit".equals(type)) {
				funcMap = funcService.getFuncById(request.getParameter("zjId"));
				setMap(funcMap, request);
				funcMap.setJlxgsj(DateUtils.getCurrentDateTime14());
				funcService.update(funcMap);
			} else if ("sybz".equals(type)) {
				String zjIds = StringUtils.getNullBlankStr(getURLDecode(request.getParameter("zjIds")));
				String sybz = StringUtils.getNullBlankStr(getURLDecode(request.getParameter("sybz")));
				if(zjIds != null && !"".equals(zjIds.trim())){
					String[] zjidArr = zjIds.split(",");
					for(int i = 0, len = zjidArr.length; i < len; i++){
						funcMap = funcService.getFuncById(zjidArr[i]);
						funcMap.setSybz(sybz);
						funcMap.setJlxgsj(DateUtils.getCurrentDateTime14());
						funcService.updateSybz(funcMap);
					}
				}
			} else if ("del".equals(type)) {
				String zjIds = StringUtils.getNullBlankStr(getURLDecode(request.getParameter("zjIds")));
				if(zjIds != null && !"".equals(zjIds.trim())){
					String[] zjidArr = zjIds.split(",");
					for(int i = 0, len = zjidArr.length; i < len; i++){
						funcMap = funcService.getFuncById(zjidArr[i]);
						funcMap.setJlscsj(DateUtils.getCurrentDateTime14());
						funcMap.setJlyxzt("0");
						funcService.logicDelete(funcMap);
					}
				}
				
			} else {
				throw new BusinessException("未知操作类型!");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException("操作失败!");
		}
		return succFlag;
	}
	
	/**
	 * 设定 Map 内容
	 * 
	 * @param funcMap
	 * @param request
	 */
	private void setMap(Function funcMap, HttpServletRequest request) {
		funcMap.setGnmc(StringUtils.getNullBlankStr(request.getParameter("gnmc")));
		funcMap.setGndm(StringUtils.getNullBlankStr(request.getParameter("gndm")));
		funcMap.setCdxxZjId(StringUtils.getNullBlankStr(request.getParameter("cdxxZjId")));
		funcMap.setGnxh(StringUtils.getNullBlankStr(request.getParameter("gnxh").trim()));
		funcMap.setSybz(StringUtils.getNullBlankStr(request.getParameter("sybz")));
		funcMap.setBz(StringUtils.getNullBlankStr(request.getParameter("bz")));
	}
	
	/**
	 * 获取所有菜单 AJAX
	 * @param model
	 * @param request
	 */
	@RequestMapping("/funcSscd")
	public void funcSscd(Model model, HttpServletRequest request) {
		List<SysMenu> menuList = funcService.queryMenuList();
		model.addAttribute("sscdList", menuList);
	}
	
	/**
	 * 校验唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkGndm")
	public @ResponseBody Object[] checkGndm(HttpServletRequest request){
		String zjId = request.getParameter("zjId");
		String gndm = request.getParameter("fieldValue");
		String fieldId =  request.getParameter("fieldId");
		
		Function funcMap = new Function();
		funcMap.setGndm(gndm);
		funcMap.setJlyxzt("1");
		
		Object[] result = new Object[2];
		result[0] = fieldId;
		List<Function> resultList = funcService.queryFuncList(funcMap);
		if (resultList != null && resultList.size() > 0) {
			if (resultList.get(0).getZjId().equals(zjId)) {
				result[1] = true;
			} else {
				result[1] = false;
			}
		} else {
			result[1] = true;
		}
		return result;
	}
	/**
	 * 编码转换
	 * @param str
	 * @return
	 */
	private String getURLDecode(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		try {
			return java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
