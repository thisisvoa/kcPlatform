package com.kcp.platform.common.role.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.common.role.service.RoleService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 */
@Controller
@RequestMapping("roleCtl")
public class RoleController extends BaseMultiActionController {
	
	@Autowired
	private RoleService roleService;
	/**
	 * 获取角色管理界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toRoleMainList")
	public String toRoleMainList(Model model, HttpServletRequest request){
		return "common/role/roleMain";
	}
	
	@RequestMapping("/roleList")
	public @ResponseBody GridData roleList(HttpServletRequest request){
		String  roleName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("roleName")));
		String  roleCode = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("roleCode")));
		String  useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String jsjb = SecurityContext.getRoleLevel();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jsmc",roleName);
		paramMap.put("jsdm", roleCode);
		paramMap.put("sybz", useTarg);
		paramMap.put("jsjb", jsjb);
		paramMap.put("jlyxzt", "1");
		List<Role> roleList = roleService.findRolePaging(paramMap);
		Page page = PageContextHolder.getPage();
		return new GridData(roleList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 角色新增界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddRole")
	public String toAddRole(Model model, HttpServletRequest request){
		User user = SecurityContext.getCurrentUser();
		model.addAttribute("operModel", CommonConst.ADD_OPERMODEL );
		model.addAttribute("YHJB", user.getYhjb());
		return "common/role/roleEdit";
	}
	
	/**
	 * 角色编辑界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toEditRole")
	public String toEditRole(Model model, HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Role roleInfo = roleService.getRoleById(roleId);
		User user = SecurityContext.getCurrentUser();
		model.addAttribute("roleInfo", roleInfo);
		model.addAttribute("operModel", CommonConst.UPDATE_OPERMODEL );
		model.addAttribute("YHJB", user.getYhjb());
		return "common/role/roleEdit";
		
	}
	
	/**
	 * 角色详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toViewRole")
	public String toViewRole(Model model, HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Role roleInfo = roleService.getRoleById(roleId);
		model.addAttribute("roleInfo", roleInfo);
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "角色信息管理", "[查看] 查看[角色名称："+roleInfo.getJsmc()+"] [角色代码："+roleInfo.getJsdm()+"]的角色信息");
		model.addAttribute("operModel", CommonConst.VIEW_OPERMODEL );
		return "common/role/roleView";
		
	}
	
	
	/**
	 * 禁用角色
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("forbidRole")
	public @ResponseBody void forbidRole(Model model, HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				Role role = roleService.getRoleById(id);
				roleService.forbidRole(role);
			}
		}
		model.addAttribute("isSuccess", true);
	}
	
	/**
	 * 启用角色
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("enabledRole")
	public @ResponseBody void enabledRole(Model model, HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				Role role = roleService.getRoleById(id);
				roleService.enabledRole(role);
			}
		}	
	}
	
	
	/**
	 * 逻辑删除角色
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("logicDelRole")
	public @ResponseBody void logicDelRole(Model model, HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				Role role = roleService.getRoleById(id);
				roleService.logicDelRole(role);
			}
		}
	}
	
	/**
	 * 新增角色信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("addRoleInfo")
	public @ResponseBody void addRoleInfo(HttpServletRequest request){
		String  roleName = StringUtils.getNullBlankStr(request.getParameter("roleName"));
		String  roleCode = StringUtils.getNullBlankStr(request.getParameter("roleCode"));
		String  remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		String  jsjb = StringUtils.getNullBlankStr(request.getParameter("jsjb"));
		Role role = new Role();
		role.setJsmc(roleName);
		role.setJsdm(roleCode);
		role.setJslx("01");
		role.setJsjb(jsjb);
		role.setBz(remark);
		role.setJlscsj("");
		roleService.addRole(role);
	}
	
	/**
	 * 修改角色信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("updateRoleInfo")
	public @ResponseBody void updateRoleInfo(HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("roleId"));
		String  roleName = StringUtils.getNullBlankStr(request.getParameter("roleName"));
		String  roleCode = StringUtils.getNullBlankStr(request.getParameter("roleCode"));
		String  remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		String  jsjb = StringUtils.getNullBlankStr(request.getParameter("jsjb"));
		Role role = new Role();
		role.setZjid(roleId);
		role.setJsmc(roleName);
		role.setJsdm(roleCode);
		role.setJslx("01");
		role.setJsjb(jsjb);
		role.setBz(remark);
		roleService.updateRole(role);
	}
	
	/**
	 * 校验角色代码唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkRoleCode")
	public @ResponseBody Object[] checkRoleCode(Model model, HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("roleId"));
		String roleCode = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		Role role = new Role();
		role.setZjid(roleId);
		role.setJsdm(roleCode);
		boolean isUnique = roleService.validateUniqueRole(role);
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = isUnique;
		return result;
	}
	
	/**
	 * 通用的角色选择器
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("cmp/roleSelector")
	public String roleCmpSelector(Model model, HttpServletRequest request){
		boolean multiselect = StringUtils.getBoolean(StringUtils.getNullBlankStr(request.getParameter("multiselect")), true);
		model.addAttribute("multiselect", multiselect);
		return "component/roleSelector";
	}
	@RequestMapping("cmp/selectedRoleList")
	public @ResponseBody GridData selectedRoleList(HttpServletRequest request){
		String selectedRole = StringUtils.getNullBlankStr(request.getParameter("selectedRole"));
		List<Role> result = null;
		if(StringUtils.isEmpty(selectedRole)){
			result = new ArrayList<Role>();
		}else{
			result = roleService.getRoleByIds(selectedRole.split(","));
		}
		return new GridData(result);
	}
	
	/**
	 * 角色详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("roleUserInfo")
	public String roleUserInfo(Model model, HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Role roleInfo = roleService.getRoleById(roleId);
		model.addAttribute("roleInfo", roleInfo);
		return "common/role/roleUserInfo";
	}
}
