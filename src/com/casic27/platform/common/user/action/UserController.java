/**
 * @(#)com.casic27.platform.common.user.action.UserController.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-20
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.org.service.OrgService;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.CipherUtil;
import com.casic27.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
@Controller
@RequestMapping("/userCtl")
public class UserController extends BaseMultiActionController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService; 
	
	/**
	 * 获取用户管理界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toUserMainList")
	public String toUserMainList(Model model, HttpServletRequest request){
		return "common/user/userMain";
	}
	
	/**
	 * 获得用户信息列表（当前单位用户）
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/toOrgUserList")
	public String toOrgUserList(Model model, HttpServletRequest request){
		String orgId =StringUtils.getNullBlankStr(request.getParameter("orgId"));//所属单位
		model.addAttribute("orgId", orgId);
		return "common/user/userList";		
	}
	
	
	@RequestMapping("/userList")
	public @ResponseBody GridData userList(HttpServletRequest request){
		String orgId =StringUtils.getNullBlankStr(request.getParameter("orgId"));//所属单位
		String userName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("userName")));//用户名称
		String loginName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("loginName")));//登录名称
		String userNum = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("userNum")));//警员编号
		String sex = StringUtils.getNullBlankStr(request.getParameter("sex"));//性别
		String telephoneNo = StringUtils.getNullBlankStr(request.getParameter("telephoneNo"));//联系电话
		String mobileNo = StringUtils.getNullBlankStr(request.getParameter("mobileNo"));//移动电话
		String email = StringUtils.getNullBlankStr(request.getParameter("email"));//电子邮箱
		String userType = StringUtils.getNullBlankStr(request.getParameter("userType"));//用户类型
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));//使用标示
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));//备注
		User currentUser = SecurityContext.getCurrentUser();
		User user = new User();
		user.setYhmc(userName);
		user.setYhdlzh(loginName);
		user.setLxdh(telephoneNo);
		user.setDzyx(email);
		user.setJybh(userNum);
		if(StringUtils.isEmpty(orgId)){
			if(!currentUser.isSuperAdmin()){
				String rootDwId = orgService.getPermissionRootOrgId();
				if(StringUtils.isEmpty(rootDwId)){
					return null;
				}else{
					user.setSsdw_zjid(rootDwId);
				}
				
			}
		}else{
			user.setSsdw_zjid(orgId);
		}
		
		user.setXb(sex);
		user.setYddh(mobileNo);
		user.setYhlx(userType);
		user.setSybz(useTarg);
		user.setBz(remark);
		user.setJlyxzt("1");//默认查询有效
		List<Map<String, Object>> userList = userService.queryCurAndChildOrgUserList(user);
		Page page = PageContextHolder.getPage();
		return new GridData(userList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 查找单位直属用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/findUserByOrg")
	public @ResponseBody GridData findUserByOrg(HttpServletRequest request){
		String ssdwZjid =StringUtils.getNullBlankStr(request.getParameter("orgId"));//所属单位
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ssdwZjid", ssdwZjid);
		queryMap.put("sybz", "1");
		queryMap.put("jlyxzt", "1");
		List<Map<String, Object>> userList = userService.findUserByOrg(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(userList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 用户新增界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddUser")
	public String toAddUser(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		if(orgId != null && !orgId.isEmpty()){
			Org org = orgService.getOrgById(orgId);
			userInfo.put("DWMC", org.getDwmc());
			userInfo.put("SSDW_ZJID", org.getZjid());
		}		
		userInfo.put("YHDLMM",CommonConst.INIT_PASSWORD);
		User user = SecurityContext.getCurrentUser();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("operModel", CommonConst.ADD_OPERMODEL );
		model.addAttribute("YHJB", user.getYhjb());
		return "common/user/userEdit";
	}
	
	/**
	 * 用户编辑界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toEditUser")
	public String toEditUser(Model model, HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Map<String, Object> userInfo = userService.getUserInfoMapById(userId);
		User user = SecurityContext.getCurrentUser();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("operModel", CommonConst.UPDATE_OPERMODEL );
		model.addAttribute("YHJB", user.getYhjb());
		return "common/user/userEdit";
		
	}
	
	/**
	 * 用户详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toViewUser")
	public String toViewUser(Model model, HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Map<String, Object> userInfo = userService.getUserInfoMapById(userId);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("operModel", CommonConst.VIEW_OPERMODEL );
		return "common/user/userView";
		
	}
	
	/**
	 * 用户调动界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toTransfer")
	public String toTransfer(Model model, HttpServletRequest request){
		String userIds = StringUtils.getNullBlankStr(request.getParameter("ids"));
		model.addAttribute("userIds", userIds);
		return "common/user/userTransferOrgTree";
		
	}
	
	
	/**
	 * 获取用户所属部门
	 * @param model
	 * @param request
	 */
	@RequestMapping("getUserOrg")
	public void getUserOrg(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));
		Org org = orgService.getOrgById(orgId);
		model.addAttribute("orgName", org.getDwmc());
	}
	/**
	 * 逻辑删除用户信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("logicDelUser")
	public @ResponseBody void logicDelUser(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User)session.getAttribute(CommonConst.SESSION_USER_KEY);
		String userId = user.getZjid();
		if(StringUtils.isNotEmpty(ids)){
			List<String> list = StringUtils.stringToList(ids, ",");
			if(list.contains(userId)){
				throw new BusinessException("不能删除当前登录用户");
			}else{
				for(String id : list){
					User usr = userService.getUserById(id);
					userService.logicDelUser(usr);
				}
			}
		}	
	}
	
	/**
	 * 启用用户
	 * @param model
	 * @param request
	 */
	@RequestMapping("enabledUser")
	public @ResponseBody void enabledUser(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				User user = userService.getUserById(id);
				userService.enabledUser(user);
			}
		}	
	}
	/**
	 * 重置密码
	 * @param model
	 * @param request
	 */
	@RequestMapping("resetPsw")
	public @ResponseBody void resetPsw(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				User user = userService.getUserById(id);
				userService.resetPsw(user);
			}
		}
	}
	
	/**
	 * 禁用用户
	 * @param model
	 * @param request
	 */
	@RequestMapping("forbidUser")
	public @ResponseBody void forbidUser(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		User user = SecurityContext.getCurrentUser();
		String userId = user.getZjid();
		if(StringUtils.isNotEmpty(ids)){
			List<String> list = StringUtils.stringToList(ids, ",");
			if(list.contains(userId)){
				throw new BusinessException("当前登录用户不能禁用");
			}else{
				for(String id : list){
					User usr = userService.getUserById(id);
					userService.forbidUser(usr);
				}
			}
		}	
		
	}
	
	/**
	 * 调动用户单位
	 * @param model
	 * @param request
	 */
	@RequestMapping("transfer")
	public  @ResponseBody void transferUserOrg(Model model, HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String orgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				User usr = userService.getUserById(id);
				userService.transferUserOrg(usr,orgId);
			}
		}
	}
	
	/**
	 * 校验警员编号唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkUserNum")
	public @ResponseBody Object[] checkUniqueUserNum(HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		String userNum = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		User user = new User();
		user.setZjid(userId);
		user.setJybh(userNum);
		boolean isUnique = userService.validateUniqueUser(user);
		Object[] result = new Object[2];
		result[0] = filedId;
		result[1] = isUnique;
		return result;
	}
	
	/**
	 * 校验用户登录账号名唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkLoginName")
	public @ResponseBody Object[] checkUniqueLoginName(Model model, HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		String loginName = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		User user = new User();
		user.setZjid(userId);
		user.setYhdlzh(loginName);
		boolean isUnique = userService.validateUniqueUser(user);
		Object[] result = new Object[2];
		result[0] = filedId;
		result[1] = isUnique;
		return result;
	}
	
	/**
	 * 校验身份证号唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkSfzh")
	public @ResponseBody Object[] checkUniqueSfzh(Model model, HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		String sfzh = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		User user = new User();
		user.setZjid(userId);
		user.setSfzh(sfzh);
		boolean isUnique = userService.validateUniqueUser(user);
		Object[] result = new Object[2];
		result[0] = filedId;
		result[1] = isUnique;
		return result;
	}
	/**
	 * 提交用户信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("submitUserInfo")
	public @ResponseBody void submitUserInfo(HttpServletRequest request){
		String operModel = StringUtils.getNullBlankStr(request.getParameter("operModel"));
		if("ADD".equalsIgnoreCase(operModel)){
			addUserInfo(request);
		}else{
			updateUserInfo(request);
		}
		
	}
	
	/**
	 * 新增用户信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("addUserInfo")
	public @ResponseBody void addUserInfo(HttpServletRequest request){
		String userName = StringUtils.getNullBlankStr(request.getParameter("userName"));
		String loginName = StringUtils.getNullBlankStr(request.getParameter("loginName"));
		String psw = StringUtils.getNullBlankStr(request.getParameter("psw"));
		String sfzh = StringUtils.getNullBlankStr(request.getParameter("sfzh"));
		String userNum = StringUtils.getNullBlankStr(request.getParameter("userNum"));
		String org = StringUtils.getNullBlankStr(request.getParameter("org"));
		String sex = StringUtils.getNullBlankStr(request.getParameter("sex"));
		String telephoneNo = StringUtils.getNullBlankStr(request.getParameter("telephoneNo"));
		String mobileNo = StringUtils.getNullBlankStr(request.getParameter("mobileNo"));
		String email = StringUtils.getNullBlankStr(request.getParameter("email"));
		String userType = StringUtils.getNullBlankStr(request.getParameter("userType"));
		String userLevel = StringUtils.getNullBlankStr(request.getParameter("userLevel"));
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		User user = new User();
		user.setYhmc(userName);
		user.setYhdlzh(loginName);
		user.setSfzh(sfzh);
		user.setYhdlmm(CipherUtil.generatePassword(psw));
		user.setLxdh(telephoneNo);
		user.setDzyx(email);
		user.setJybh(userNum);
		user.setSsdw_zjid(org);
		user.setXb(sex);
		user.setYddh(mobileNo);
		user.setYhlx(userType);
		user.setYhjb(userLevel);
		user.setSybz(useTarg);
		user.setBz(remark);
		user.setJlyxzt("1");
		user = userService.addUser(user);
	}
	
	/**
	 * 修改用户信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("updateUserInfo")
	public @ResponseBody void updateUserInfo(HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		String userName = StringUtils.getNullBlankStr(request.getParameter("userName"));
		String loginName = StringUtils.getNullBlankStr(request.getParameter("loginName"));
		String sfzh = StringUtils.getNullBlankStr(request.getParameter("sfzh"));
		String userNum = StringUtils.getNullBlankStr(request.getParameter("userNum"));
		String org = StringUtils.getNullBlankStr(request.getParameter("org"));
		String sex = StringUtils.getNullBlankStr(request.getParameter("sex"));
		String telephoneNo = StringUtils.getNullBlankStr(request.getParameter("telephoneNo"));
		String mobileNo = StringUtils.getNullBlankStr(request.getParameter("mobileNo"));
		String email = StringUtils.getNullBlankStr(request.getParameter("email"));
		String userType = StringUtils.getNullBlankStr(request.getParameter("userType"));
		String userLevel = StringUtils.getNullBlankStr(request.getParameter("userLevel"));
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		User user = new User();
		user.setZjid(userId);
		user.setYhmc(userName);
		user.setYhdlzh(loginName);
		user.setSfzh(sfzh);
		user.setLxdh(telephoneNo);
		user.setDzyx(email);
		user.setJybh(userNum);
		user.setSsdw_zjid(org);
		user.setXb(sex);
		user.setYddh(mobileNo);
		user.setYhlx(userType);
		user.setYhjb(userLevel);
		user.setSybz(useTarg);
		user.setBz(remark);
		user = userService.updateUser(user);
	}
	
	@RequestMapping("toViewUserPermission")
	public ModelAndView toViewUserPermission(HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		User user = userService.getUserById(userId);
		if(user==null){
			throw new BusinessException("用户不存在!");
		}
		return new ModelAndView("common/user/viewUserPermission")
					.addObject("user",user);
	}
	@RequestMapping("getUserPermission")
	public @ResponseBody List<Map<String, Object>> getUserPermission(HttpServletRequest request){
		String userId = StringUtils.getNullBlankStr(request.getParameter("userId"));
		User user = userService.getUserById(userId);
		if(user.isSuperAdmin()){
			return userService.getAllPermission();
		}else{
			return userService.getUserPermission(userId);
		}
	}
	
	/**
	 * 通用的用户选择器
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("cmp/userSelector")
	public String userCmpSelector(Model model, HttpServletRequest request){
		boolean multiselect = StringUtils.getBoolean(StringUtils.getNullBlankStr(request.getParameter("multiselect")), true);
		model.addAttribute("multiselect", multiselect);
		return "component/userSelector";
	}
	
	/**
	 * 通用用户选择器
	 * @param request
	 * @return
	 */
	@RequestMapping("cmp/selectedUserList")
	public @ResponseBody GridData selectedUserList(HttpServletRequest request){
		String selectedUser = StringUtils.getNullBlankStr(request.getParameter("selectedUser"));
		List<Map<String,Object>> result = null;
		if(StringUtils.isEmpty(selectedUser)){
			result = new ArrayList<Map<String,Object>>();
		}else{
			result = userService.getUserByIds(selectedUser.split(","));
		}
		return new GridData(result);
	}
	
	@RequestMapping("findUserByRole")
	public @ResponseBody GridData findUserByRole(HttpServletRequest request){
		String roleId = StringUtils.getNullBlankStr(request.getParameter("roleId"));
		List<Map<String,Object>> userList = userService.findUserByRole(roleId);
		Page page = PageContextHolder.getPage();
		return new GridData(userList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
}
