package com.kcp.platform.common.authorization.action;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.kcp.platform.common.authorization.service.RoleFuncService;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.func.service.FuncService;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.common.menu.service.MenuService;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.role.service.RoleService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：角色功能页面控制层
 * </pre>
 * <pre>
 * </pre>
 */

@Controller
@RequestMapping("/roleFunc")
public class RoleFuncController extends BaseMultiActionController {
	
	@Autowired
	private RoleService roleService;
	
	
	@Autowired
	private RoleFuncService roleFuncService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private FuncService funcService;
	/**
	 * 主页面
	 * @return
	 */
	@RequestMapping("/toMain")
	public String toRoleOperation(){
		return "common/authorization/roleFuncMain";
	}
	
	/**
	 * 跳转至按角色分配功能页面
	 * @return
	 */
	@RequestMapping("/assignFuncByRole")
	public String toAssignByRoleMain(){
		return "common/authorization/assignFuncByRole";
	}
	
	/**
	 * 跳转至角色树页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/roleTree")
	public ModelAndView roleTree(HttpServletRequest request, HttpServletResponse response){
		String operateType = request.getParameter("operateType");
		return new ModelAndView("common/authorization/roleTree")
					.addObject("operateType", operateType);
	}
	
	
	/**
	 * 跳转至为角色分配用户页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/assignFuncToRolePage")
	public ModelAndView assignFuncToRolePage(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String roleId = request.getParameter("roleId");
		Role role =  roleService.getRoleById(roleId);
		if(role==null){
			throw new BusinessException("所选角色不存在");
		}
		return new ModelAndView("common/authorization/assignFuncToRole")
						.addObject("role",role);
	}
	
	/**
	 * 构造功能树XML
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/funcTreeJson4Role")
	public @ResponseBody List<Map<String,Object>> funcTreeJson4Role(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String roleId = request.getParameter("roleId");
		
		List<Map> fucTreeList = roleFuncService.queryFuncTreeList(roleId);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Map map:fucTreeList){
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("name", String.valueOf(map.get("LABEL")));
			String type = String.valueOf(map.get("TYPE"));
			if("MENU".equals(type)){
				node.put("id","menu_"+String.valueOf(map.get("NODEID")));
			}else{
				node.put("id",String.valueOf(map.get("NODEID")));
			}
			String parentNodeId = String.valueOf(map.get("PARENTNODEID"));
			if("0".equals(parentNodeId)){
				node.put("pId",parentNodeId);
			}else{
				node.put("pId", "menu_"+parentNodeId);
			}
			node.put("type", type);
			node.put("checked", "1".equals(String.valueOf(map.get("ISCHECK"))));
			result.add(node);
		}
		return  result;
	}
	
	/**
	 * 为角色分配功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignFuncToRole")
	public @ResponseBody void  assignFuncToRole(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String roleId = request.getParameter("roleId");
		String funcStr = request.getParameter("funcs");
		Role role = roleService.getRoleById(roleId);
		roleFuncService.assignFuncToRole(role, Arrays.asList(StringUtils.split(funcStr, ",")));
	}
	
	/************************按功能分配角色页面******************************/
	
	/**
	 * 跳转至按功能分配角色页面
	 * @return
	 */
	@RequestMapping("/assignRoleByFunc")
	public String assignRoleByFunc(){
		return "common/authorization/assignRoleByFunc";
	}
	
	/**
	 * 跳转至功能树页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funcTree")
	public ModelAndView funcTree(HttpServletRequest request, HttpServletResponse response){
		String operateType = request.getParameter("operateType");
		return new ModelAndView("common/authorization/funcTree")
					.addObject("operateType", operateType);
	}
	
	/**
	 * 跳转至为功能分配角色页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignRoleToFuncPage")
	public ModelAndView assignRoleToFuncPage(HttpServletRequest request, HttpServletResponse response){
		String funcId = request.getParameter("funcId");
		Function func = funcService.getFuncById(funcId);
		if(func == null){
			throw new BusinessException("所选择的功能不存在！");
		}
		List<Menu> parentMenuList = menuService.queryParentMenuList(func.getCdxxZjId());
		
		return new ModelAndView("common/authorization/assignRoleToFunc")
						.addObject("parentMenuList", parentMenuList)
						.addObject("func", func);
	}
	
	@RequestMapping("getAssignedRoleList")
	public @ResponseBody GridData getAssignedRoleList(HttpServletRequest request){
		String funcId = request.getParameter("funcId");
		List<Map> roleList = roleFuncService.getAssignedRoleList(funcId);
		return new GridData(roleList);
	}
	/**功能树
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("funcTreeJson")
	public @ResponseBody List<Map<String,Object>> funcTreeJson(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(StringUtils.isEmpty(id)){
			List<Menu> menuList = menuService.queryMenusByParentMenuId(CommonConst.TREE_ROOT_ID);
			for(Menu menu:menuList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", menu.getZjId());
				node.put("name", menu.getCdmc());
				node.put("pId", menu.getSjcd());
				node.put("isParent", true);
				node.put("type", "menu");
				result.add(node);
			}
		}else{
			List<Menu> menuList = menuService.queryMenusByParentMenuId(id);
			List<Function> funcList = funcService.queryFuncByParentMenuId(id);
			for(Menu menu:menuList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", menu.getZjId());
				node.put("name", menu.getCdmc());
				node.put("pId", menu.getSjcd());
				node.put("isParent", true);
				node.put("type", "menu");
				result.add(node);
			}
			for(Function func:funcList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", "FUNC_"+func.getZjId());
				node.put("name", func.getGnmc());
				node.put("pId", func.getCdxxZjId());
				node.put("funcId", func.getZjId());
				node.put("type", "func");
				result.add(node);
			}
		}
		return result;
	}
	
	/**
	 * 为功能分配角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignRoleToFunc")
	public @ResponseBody void assignRoleToFunc(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcId = request.getParameter("funcId");
		String roles = request.getParameter("roles");
		Function func = funcService.getFuncById(funcId);
		roleFuncService.assignRoleToFunc(func, Arrays.asList(StringUtils.split(roles, ",")));
		
	}
}
