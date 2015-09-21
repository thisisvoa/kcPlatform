/**
 * @(#)com.casic27.platform.common.authorization.action.OrgRoleController.java
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

import com.casic27.platform.common.authorization.service.OrgRoleService;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.org.service.OrgService;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.role.service.RoleService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：单位角色页面控制层
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Controller
@RequestMapping("/orgRole")
public class OrgRoleController extends BaseMultiActionController {
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrgRoleService orgRoleService;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 跳转至单位角色关系管理页面
	 * @return
	 */
	@RequestMapping("/toMain")
	public String orgRole(){
		return "common/authorization/orgRoleMain";
	}
	
	/**
	 * 跳转至按单位分配角色页面
	 * @return
	 */
	@RequestMapping("/assignRoleByOrg")
	public String assignRoleByOrg(){
		return "common/authorization/assignRoleByOrg";
	}
	
	/**
	 * 跳转至按角色分配单位页面
	 * @return
	 */
	@RequestMapping("/assignOrgByRole")
	public String assignOrgByRole(){
		return "common/authorization/assignOrgByRole";
	}
	
	/**
	 * 跳转至为单位分配角色页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignRoleToOrgPage")
	public ModelAndView assignRoleToOrgPage(HttpServletRequest request, HttpServletResponse response){
		String orgId = request.getParameter("orgId");
		List<Map> parentOrgList = orgService.queryParentOrgList(orgId);
		return new ModelAndView("common/authorization/assignRoleToOrg")
											.addObject("orgId",orgId)
											.addObject("parentOrgList", parentOrgList);
	}
	
	@RequestMapping("getAssignedRoleList")
	public @ResponseBody GridData getAssignedRoleList(HttpServletRequest request){
		String orgId = request.getParameter("orgId");
		List<Map> roleList = orgRoleService.getAssignedRoleList(orgId);
		return new GridData(roleList);
	}
	/**
	 * 为单位分配角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/assignRoleToOrg")
	public @ResponseBody void assignRoleToOrg(HttpServletRequest request, HttpServletResponse response )throws Exception{
		String orgId = request.getParameter("orgId");
		String roles = request.getParameter("roles");
		Org org = orgService.getOrgById(orgId);
		orgRoleService.assignRoleToOrg(org, Arrays.asList(StringUtils.split(roles, ",")));
	}
	
	/**
	 * 跳转至为角色分配单位页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assignOrgToRolePage")
	public ModelAndView assignOrgToRolePage(HttpServletRequest request, HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		Role role = roleService.getRoleById(roleId);
		if(null==role){
			throw new BusinessException("所选择的角色不存在！");
		}
		return new ModelAndView("common/authorization/assignOrgToRole")
					.addObject("role", role);
	}
	
	@RequestMapping("getAssignedOrgList")
	public @ResponseBody GridData getAssignedOrgList(HttpServletRequest request){
		String roleId = request.getParameter("roleId");
		List<Map> assignedOrgList = orgRoleService.queryAssignedOrgByRole(roleId);
		return new GridData(assignedOrgList);
	}
	/**
	 * 为角色分配组织
	 * @param request
	 * @param response
	 */
	@RequestMapping("/assignOrgToRole")
	public @ResponseBody void assignOrgToRole(HttpServletRequest request, HttpServletResponse response){
		String roleId = request.getParameter("roleId");
		String orgs = request.getParameter("orgs");
		Role role = roleService.getRoleById(roleId);
		orgRoleService.assignOrgToRole(role, StringUtils.splitToList(orgs, ","));
	}
	
}
