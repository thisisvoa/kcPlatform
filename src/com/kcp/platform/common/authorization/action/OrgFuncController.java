package com.kcp.platform.common.authorization.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.common.authorization.service.OrgFuncService;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.func.service.FuncService;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.common.menu.service.MenuService;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：单位功能页面控制层
 * </pre>
 * <pre>
 * </pre>
 */
@Controller
@RequestMapping("/orgFunc")
public class OrgFuncController extends BaseMultiActionController {
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrgFuncService operationService;
	
	@Autowired
	private FuncService funcService;
	
	@Autowired
	private MenuService menuService;
	/**
	 * 跳转至主页面
	 * @return
	 */
	@RequestMapping("/toMain")
	public String orgOperation(){
		return "common/authorization/orgFuncMain";
	}
	/******************按单位分配功能页面**************************/
	/**
	 * 跳转至组织分配功能页面
	 * @return
	 */
	@RequestMapping("/assignFuncByOrg")
	public String assignFuncByOrgMain(){
		return "common/authorization/assignFuncByOrg";
	}
	
	/**
	 * 跳转至单位树页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orgTree")
	public ModelAndView orgTree(HttpServletRequest request, HttpServletResponse response){
		String operateType = request.getParameter("operateType");
		return new ModelAndView("common/authorization/orgTree").addObject("operateType", operateType);
	}
	
	/**
	 * 跳转至为单位分配功能页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignFuncToOrgPage")
	public ModelAndView assignFuncToOrgPage(HttpServletRequest request, HttpServletResponse response){
		String orgId = request.getParameter("orgId");
		List<Map> parentOrgList = orgService.queryParentOrgList(orgId);
		return new ModelAndView("common/authorization/assignFuncToOrg")
								.addObject("orgId", orgId)
								.addObject("parentOrgList", parentOrgList);
	}
	/**
	 * 带是否已经分配了组织的功能树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/funcTreeJson4Org")
	public @ResponseBody List<Map<String,Object>> funcTreeJson4Org(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String orgId = request.getParameter("orgId");
		List<Map> funcList = operationService.queryFuncTreeList(orgId);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Map map:funcList){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("name", map.get("LABLE"));
			String type = String.valueOf(map.get("TYPE"));
			String nodeId = String.valueOf(map.get("NODEID"));
			if("FUNC".equals(type)){
				node.put("id", "func_"+nodeId);
			}else{
				node.put("id", nodeId);
			}
			String parentNodeId = String.valueOf(map.get("PARENTNODEID"));
			node.put("pId", parentNodeId);
			node.put("type", type);
			node.put("nodeId", nodeId);
			node.put("checked", "1".equals(String.valueOf(map.get("ISCHECK"))));
			result.add(node);
		}
		return result;
	}
	/**
	 * 为组织分配功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignFuncToOrg")
	public void assignFuncToOrg(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String orgId = request.getParameter("orgId");
		String funcStr = request.getParameter("funcs");
		operationService.assignFuncToOrg(orgId, StringUtils.splitToList(funcStr, ","));
	}
	
	/******************按功能分配单位页面**************************/
	/**
	 * 跳转按功能分配单位页面
	 */
	@RequestMapping("/assignOrgByFunc")
	public String assignOrgByFuncMain(){
		return "common/authorization/assignOrgByFunc";
	}
	/**
	 * 跳转至为功能分配单位页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignOrgToFuncPage")
	public ModelAndView assignOrgToFuncPage(HttpServletRequest request, HttpServletResponse response){
		String funcId = request.getParameter("funcId");
		Function func = funcService.getFuncById(funcId);
		if(func==null){
			throw new BusinessException("选择的功能不存在!");
		}
		List<Menu> parentMenuList = menuService.queryParentMenuList(func.getCdxxZjId());
		return new ModelAndView("common/authorization/assignOrgToFunc")
						.addObject("func", func)
						.addObject("parentMenuList", parentMenuList);
	}
	
	@RequestMapping("getAssignedOrgList")
	public @ResponseBody GridData getAssignedOrgList(HttpServletRequest request){
		String funcId = request.getParameter("funcId");
		List<Map> assignedOrgList = operationService.queryAssignedOrgByFunc(funcId);
		return new GridData(assignedOrgList);
	}
	/**
	 * 为功能分配组织
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignOrgToFunc")
	public void assignOrgToFunc(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcId = request.getParameter("funcId");
		String orgs = request.getParameter("orgs");
		operationService.assignOrgToFunc(funcId, StringUtils.splitToList(orgs, ","));
	}
}
