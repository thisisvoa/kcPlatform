package com.kcp.platform.common.authorization.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.common.authorization.service.UserRoleService;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.role.service.RoleService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@kcp.com)
 */
@Controller
@RequestMapping("/userRole")
public class UserRoleController extends BaseMultiActionController {
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 跳转至用户角色关系管理主页面
	 * @return
	 */
	@RequestMapping("/toMain")
	public String toUserRole(){
		return "common/authorization/userRoleMain";
	}
	/**
	 * 跳转至按角色分配用户页面
	 * @return
	 */
	@RequestMapping("/assignRoleByUser")
	public String assignRoleByUser(){
		return "common/authorization/assignRoleByUser";
	}
	
	/**
	 * 跳转至角色分配用户操作页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignRoleToUserPage")
	public ModelAndView assignRoleToUserPage(HttpServletRequest request, HttpServletResponse response){
		String userId = request.getParameter("userId");
		User user = userService.getUserById(userId);
		if(user==null){
			throw new BusinessException("选择的用户不存在！");
		}
		List<Map> parentOrgList = orgService.queryParentOrgList(user.getSsdw_zjid());
		return new ModelAndView("common/authorization/assignRoleToUser")
					.addObject("user", user)
					.addObject("parentOrgList", parentOrgList);
	}
	
	@RequestMapping("getAssignedRoleList")
	public @ResponseBody GridData getAssignedRoleList(HttpServletRequest request){
		String userId = request.getParameter("userId");
		List<Map<String,Object>> roleList = userRoleService.getAssignedRoleList(userId);
		
		return new GridData(roleList);
	}
	/**
	 * 为角色分配用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignRoleToUser")
	public @ResponseBody void assignRoleToUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userId = request.getParameter("userId");
		String roles = request.getParameter("roles");
		User user = userService.getUserById(userId);
		userRoleService.assignRoleToUser(user, Arrays.asList(StringUtils.split(roles, ",")));
	}
	
	/**
	 * 跳转至按角色分配用户页面
	 * @return
	 */
	@RequestMapping("/assignUserByRole")
	public String assignUserByRole(){
		return "common/authorization/assignUserByRole";
	}
	
	/**
	 * 跳转至角色分配用户操作页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignUserToRolePage")
	public ModelAndView assignUserToRolePage(HttpServletRequest request, HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		Role role = roleService.getRoleById(roleId);
		if(role==null){
			throw new BusinessException("找不到选中的角色信息！");
		}
		return new ModelAndView("common/authorization/assignUserToRole")
					.addObject("role", role);
	}
	
	@RequestMapping("getAssignedUserList")
	public @ResponseBody GridData getAssignedUserList(HttpServletRequest request, HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		List<Map> assignedUserList = null;
		if(StringUtils.isNotEmpty(roleId)){
			assignedUserList = userRoleService.queryAssignedUserByRole(roleId);
		}
		return new GridData(assignedUserList);
	}
	/**
	 * 为角色分配用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignUserToRole")
	public @ResponseBody void assignUserToRole(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String roleId = request.getParameter("roleId");
		String users = request.getParameter("users");
		Role role = roleService.getRoleById(roleId);
		userRoleService.assignUserToRole(role, StringUtils.splitToList(users, ","));
	}
	
}
