/**
 * @(#)com.casic27.platform.common.authorization.action.UserFuncController.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-25
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.action;

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

import com.casic27.platform.common.authorization.service.UserFuncService;
import com.casic27.platform.common.func.entity.Function;
import com.casic27.platform.common.func.service.FuncService;
import com.casic27.platform.common.menu.entity.Menu;
import com.casic27.platform.common.menu.service.MenuService;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.org.service.OrgService;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Controller
@RequestMapping("/userFunc")
public class UserFuncController extends BaseMultiActionController {
	
	@Autowired
	private UserFuncService userFuncService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FuncService funcService;
	
	@Autowired
	private MenuService menuService;
	/**
	 * 跳转至用户功能关系管理主页面
	 * @return
	 */
	@RequestMapping("/toMain")
	public String toUserFunc(){
		return "common/authorization/userFuncMain";
	}
	/**
	 * 按功能分配用户页面
	 * @return
	 */
	@RequestMapping("/assignFuncByUser")
	public String assignFuncByUser(){
		return "common/authorization/assignFuncByUser";
	}
	/**
	 * 跳转至单位用户树页面
	 * @return
	 */
	@RequestMapping("/orgUserTree")
	public ModelAndView orgUserTree(HttpServletRequest request, HttpServletResponse response){
		String operateType = request.getParameter("operateType");
		return new ModelAndView("common/authorization/orgUserTree")
						.addObject("operateType", operateType);
	}
	
	@RequestMapping("/assignFuncToUserPage")
	public ModelAndView assignFuncToUserPage(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userId = request.getParameter("userId");
		User user = userService.getUserById(userId);
		if(user==null){
			throw new BusinessException("选择的用户不存在！");
		}
		List<Map> parentOrgList = orgService.queryParentOrgList(user.getSsdw_zjid());
		return new ModelAndView("common/authorization/assignFuncToUser")
					.addObject("user", user)
					.addObject("parentOrgList", parentOrgList);
	}
	
	/**
	 * 单位部门树Json
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("orgUserTreeJson")
	public @ResponseBody List<Map<String,Object>> orgUserTreeJson(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		User currentUser = SecurityContext.getCurrentUser();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(StringUtils.isEmpty(id)){
			if(!currentUser.isSuperAdmin()){
				String rootDwId = orgService.getPermissionRootOrgId();
				if(StringUtils.isEmpty(rootDwId)){
					return null;
				}
				List<Org> orgList = orgService.getParentOrgList(rootDwId);
				for(Org org:orgList){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", org.getZjid());
					node.put("name", org.getDwmc());
					node.put("pId", org.getSjdw_zjid());
					node.put("type", "org");
					if(org.getZjid().equals(rootDwId)){
						node.put("isParent", true);
					}else{
						node.put("enable", false);
					}
					result.add(node);
				}
			}else{
				List<Org> orgList = orgService.queryChildOrgListByPOrgId(CommonConst.TREE_ROOT_ID);
				for(Org org: orgList){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", org.getZjid());
					node.put("name", org.getDwmc());
					node.put("pId", org.getSjdw_zjid());
					node.put("isParent", true);
					node.put("type", "org");
					result.add(node);
				}
			}
		}else{
			List<Org> orgList = orgService.queryChildOrgListByPOrgId(id);
			List<User> userList = userService.queryUserByOrgId(id);
			for(Org org: orgList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", org.getZjid());
				node.put("name", org.getDwmc());
				node.put("pId", org.getSjdw_zjid());
				node.put("isParent", true);
				node.put("type", "org");
				result.add(node);
			}
			
			for(User user: userList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", "user_"+user.getZjid());
				node.put("name", user.getYhmc());
				node.put("pId", user.getSsdw_zjid());
				node.put("type", "user");
				node.put("userId", user.getZjid());
				result.add(node);
			}
		}
		return result;
	}
	
	@RequestMapping("/funcTreeJson4User")
	public @ResponseBody List<Map<String,Object>> funcTreeJson4User(HttpServletRequest request){
		String userId = request.getParameter("userId");
		List<Map> funcList = userFuncService.queryUserFucList(userId);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Map map:funcList){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("name", map.get("LABEL"));
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
	 * 为用户分配功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignFuncToUser")
	public void  assignFuncToUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userId = request.getParameter("userId");
		String funcs = request.getParameter("funcs");
		userFuncService.assignFuncToUser(userId, Arrays.asList(StringUtils.split(funcs, ",")));
	}
	
	/**********************************按用户分配功能页面******************************/
	/**
	 * 按功能分配用户页面
	 * @return
	 */
	@RequestMapping("/assignUserByFunc")
	public String assignUserByFunc(){
		return "common/authorization/assignUserByFunc";
	}
	
	/**
	 * 为功能分配用户页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/assignUserToFuncPage")
	public ModelAndView assignUserToFuncPage(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcId = request.getParameter("funcId");
		Function func = funcService.getFuncById(funcId);
		if(func==null){
			throw new BusinessException("选择的功能不存在!");
		}
		List<Menu> parentMenuList = menuService.queryParentMenuList(func.getCdxxZjId());
		return new ModelAndView("common/authorization/assignUserToFunc")
					.addObject("func", func)
					.addObject("parentMenuList", parentMenuList);
	}
	/**
	 * 获取已分配用户列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getAssignedUserList")
	public @ResponseBody GridData getAssignedUserList(HttpServletRequest request){
		String funcId = request.getParameter("funcId");
		List<Map> assignedUserList = userFuncService.queryAssignedUserByFunc(funcId);
		return new GridData(assignedUserList);
	}
	/**
	 * 为功能分配用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignUserToFunc")
	public void  assignUserToFunc(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcId = request.getParameter("funcId");
		String users = request.getParameter("users");
		userFuncService.assignUserToFunc(funcId, Arrays.asList(StringUtils.split(users, ",")));
	}
}
