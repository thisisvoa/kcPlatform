package com.kcp.platform.common.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.user.entity.UserGroup;
import com.kcp.platform.common.user.service.UserGroupService;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.CollectionUtils;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 */
@Controller
@RequestMapping("userGroupCtl")
public class UserGroupController extends BaseMultiActionController {
	@Autowired
	UserGroupService userGroupService;
	/**
	 * 用户组管理界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toUserGroupMain")
	public String toUserGroupMain(Model model, HttpServletRequest request){
		return "common/user/userGroupMain";
	}
	
	/**
	 * 用户组对应用户信息列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toUserGroupUserList")
	public String toUserGroupUserList(Model model, HttpServletRequest request){
		UserGroup userGroupInfo = new UserGroup();
		Map<String, Object> userGroupMap =  assembleParaMap(request);
		List<UserGroup> userGroupList = userGroupService.queryUserGroup(userGroupMap);
		if(!CollectionUtils.isEmpty(userGroupList)){
			userGroupInfo = userGroupList.get(0);
		}
		model.addAttribute("userGroupInfo", userGroupInfo);	
		return "common/user/userGroupUserList";
	}
	
	/**
	 * 用户组分配界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toUserGroupAllot")
	public String toUserGroupAllot(Model model, HttpServletRequest request){
		String userGroupId = request.getParameter("userGroupId");
		String allot = StringUtils.getNullBlankStr(request.getParameter("isAllot"));
		model.addAttribute("userGroupId", userGroupId);
		model.addAttribute("isAllot", allot);
		return "common/user/userGroupAllot";
	}
	
	@RequestMapping("getAssignUserList")
	public @ResponseBody GridData getAssignUserList(HttpServletRequest request){
		String userGroupId = request.getParameter("userGroupId");
		String userName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("userName")));
		String allot = StringUtils.getNullBlankStr(request.getParameter("isAllot"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("zjid", userGroupId);
		paramMap.put("allot", allot);
		paramMap.put("yhmc", userName);
		List<Map<String, Object>> userList = userGroupService.queryAlloteUserByUserGroupMap(paramMap);
		Page page = PageContextHolder.getPage();
		return new GridData(userList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 用户组列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toUserGroupList")
	public String toUserGroupList(Model model, HttpServletRequest request){
		return "common/user/userGroupList";		
	}
	
	@RequestMapping("userGroupList")
	public @ResponseBody GridData userGroupList(HttpServletRequest request){
		Map<String, Object> userGroupMap =  assembleParaMap(request);
		userGroupMap.put("jlyxzt", "1");
		List<UserGroup> userGroupList = userGroupService.queryUserGroup(userGroupMap);
		Page page = PageContextHolder.getPage();
		return new GridData(userGroupList, page.getPage(),page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 用户组详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toViewUserGroup")
	public String toViewUserGroup(Model model, HttpServletRequest request){
		Map<String, Object> paramMap = assembleParaMap(request);
		List<Map<String, Object>> userGroupList = userGroupService.queryUserGroupByMap(paramMap);
		Map<String, Object> userGroupInfo = new HashMap<String, Object>();
		if(!CollectionUtils.isEmpty(userGroupList)){
			userGroupInfo = userGroupList.get(0);
		}
		model.addAttribute("userGroupInfo", userGroupInfo);	
		return "common/user/userGroupView";
		
	}
	
	/**
	 * 用户组新增界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddUserGroup")
	public String toAddUserGroup(Model model, HttpServletRequest request){
		model.addAttribute("operModel", CommonConst.ADD_OPERMODEL );
		return "common/user/userGroupEdit";
		
	}
	
	/**
	 * 用户组编辑界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toEditUserGroup")
	public String toEditUserGroup(Model model, HttpServletRequest request){
		Map<String, Object> paramMap = assembleParaMap(request);
		List<UserGroup> userGroupList = userGroupService.queryUserGroup(paramMap);
		UserGroup userGroupInfo = new UserGroup();
		if(!CollectionUtils.isEmpty(userGroupList)){
			userGroupInfo = userGroupList.get(0);
		}
		model.addAttribute("userGroupInfo", userGroupInfo);	
		model.addAttribute("operModel", CommonConst.UPDATE_OPERMODEL );
		return "common/user/userGroupEdit";		
	}
	
	/**
	 * 新增用户组
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("addUserGroup")
	public void addUserGroup(HttpServletRequest request){
		Map<String, Object> userGroupInfo = assembleParaMap(request);
		userGroupInfo.put("cjyh", "");//后续添加当前登录用户
		userGroupInfo.put("jlxzsj", DateUtils.getCurrentDateTime14());
		userGroupInfo.put("jlyxzt", CommonConst.ENABLE_FLAG);
		userGroupInfo = userGroupService.addUserGroupMap(userGroupInfo);
	}
	
	/**
	 * 修改用户组
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("updateUserGroup")
	public void updateUserGroup(HttpServletRequest request){
		Map<String, Object> userGroupInfo = assembleParaMap(request);
		userGroupInfo.put("gxyh", "");//后续添加当前登录用户
		userGroupInfo.put("jlxgsj", DateUtils.getCurrentDateTime14());
		userGroupService.updateUserGroupMap(userGroupInfo);
	}
	
	/**
	 * 逻辑删除用户组信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("logicDelUserGroup")
	public void logicDelUserGroup(HttpServletRequest request){
		String ids = request.getParameter("ids");
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				UserGroup userGroup = new UserGroup();
				userGroup.setZjid(id);
				userGroup.setJlscsj(DateUtils.getCurrentDateTime14());
				userGroup.setJlyxzt(CommonConst.DISABLE_FLAG);
				userGroupService.updateUserGroup(userGroup);
			}
		}
	}
	/**
	 * 保存用户组和用户的关联关系
	 * @param model
	 * @param request
	 */
	@RequestMapping("saveRelaction")
	public void saveRelaction(HttpServletRequest request){
		String userIds = StringUtils.getNullBlankStr(request.getParameter("userIds"));
		String selectedIds = StringUtils.getNullBlankStr(request.getParameter("selectedIds"));
		String userGroupId = StringUtils.getNullBlankStr(request.getParameter("userGroupId"));
		if(StringUtils.isNotEmpty(selectedIds)){
			List<String> selectedIdList =  StringUtils.stringToList(selectedIds, ",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("list", selectedIdList);
			paramMap.put("yhzid", userGroupId);
			userGroupService.deleteUserGroup_R_User(paramMap);//批量删除取消分配的关联关系	
		}
		if(StringUtils.isNotEmpty(userIds)){
			List<String> userIdList =  StringUtils.stringToList(userIds, ",");
			for(String id : userIdList){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("yhzid", userGroupId);
				paramMap.put("yhid", id);
				userGroupService.AddUserGroup_R_User(paramMap);//增加新			
			}
		}
	}
	
	/**
	 * 组装参数为map类型
	 * @param request
	 * @return
	 */
	private Map<String, Object> assembleParaMap(HttpServletRequest request){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String userGroupId = StringUtils.getNullBlankStr(request.getParameter("userGroupId"));
		String userGroupName = StringUtils.getNullBlankStr(request.getParameter("userGroupName"));
		String userGroupType = StringUtils.getNullBlankStr(request.getParameter("userGroupType"));
		String userGroupLevel = StringUtils.getNullBlankStr(request.getParameter("userGroupLevel"));
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));		
		String creator = StringUtils.getNullBlankStr(request.getParameter("creator"));
		String updator= StringUtils.getNullBlankStr(request.getParameter("updator"));
		String createTime = StringUtils.getNullBlankStr(request.getParameter("createTime"));
		String updateTime = StringUtils.getNullBlankStr(request.getParameter("updateTime"));
		String deleteTime = StringUtils.getNullBlankStr(request.getParameter("deleteTime"));
		String enableTarg = StringUtils.getNullBlankStr(request.getParameter("enableTarg"));
		if(StringUtils.isNotEmpty(userGroupId)){
			paramMap.put("zjid", userGroupId);
		}
		if(StringUtils.isNotEmpty(userGroupName)){
			paramMap.put("yhzmc", userGroupName);
		}
		if(StringUtils.isNotEmpty(userGroupType)){
			paramMap.put("yhzlx", userGroupType);
		}
		if(StringUtils.isNotEmpty(userGroupLevel)){
			paramMap.put("yhzjb", userGroupLevel);
		}
		if(StringUtils.isNotEmpty(useTarg)){
			paramMap.put("sybz", useTarg);
		}
		if(StringUtils.isNotEmpty(remark)){
			paramMap.put("bz", remark);
		}
		if(StringUtils.isNotEmpty(creator)){
			paramMap.put("cjyh", creator);
		}
		if(StringUtils.isNotEmpty(updator)){
			paramMap.put("gxyh", updator);
		}
		if(StringUtils.isNotEmpty(createTime)){
			paramMap.put("jlxzsj", createTime);
		}
		if(StringUtils.isNotEmpty(updateTime)){
			paramMap.put("jlxgsj", updateTime);
		}
		if(StringUtils.isNotEmpty(deleteTime)){
			paramMap.put("jlscsj", deleteTime);
		}
		if(StringUtils.isNotEmpty(enableTarg)){
			paramMap.put("jlyxzt", enableTarg);
		}
		return paramMap;		
	}
	
	/**
	 * 组装参数为实体类型
	 * @param request
	 * @return
	 */
	public UserGroup assembleParaEntity(HttpServletRequest request){
		UserGroup userGroup = new UserGroup();
		String userGroupId = StringUtils.getNullBlankStr(request.getParameter("userGroupId"));
		String userGroupName = StringUtils.getNullBlankStr(request.getParameter("userGroupName"));
		String userGroupType = StringUtils.getNullBlankStr(request.getParameter("userGroupType"));
		String userGroupLevel = StringUtils.getNullBlankStr(request.getParameter("userGroupLevel"));
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));		
		String creator = StringUtils.getNullBlankStr(request.getParameter("creator"));
		String updator= StringUtils.getNullBlankStr(request.getParameter("updator"));
		String createTime = StringUtils.getNullBlankStr(request.getParameter("createTime"));
		String updateTime = StringUtils.getNullBlankStr(request.getParameter("updateTime"));
		String deleteTime = StringUtils.getNullBlankStr(request.getParameter("deleteTime"));
		String enableTarg = StringUtils.getNullBlankStr(request.getParameter("enableTarg"));	
		if(StringUtils.isNotEmpty(userGroupId)){
			userGroup.setZjid(userGroupId);
		}
		if(StringUtils.isNotEmpty(userGroupName)){
			userGroup.setYhzmc(userGroupName);
		}
		if(StringUtils.isNotEmpty(userGroupType)){
			userGroup.setYhzlx(userGroupType);
		}
		if(StringUtils.isNotEmpty(userGroupLevel)){
			userGroup.setYhzjb(userGroupLevel);
		}
		if(StringUtils.isNotEmpty(useTarg)){
			userGroup.setSybz(useTarg);
		}
		if(StringUtils.isNotEmpty(remark)){
			userGroup.setBz(remark);
		}
		if(StringUtils.isNotEmpty(creator)){
			userGroup.setCjyh(creator);
		}
		if(StringUtils.isNotEmpty(updator)){
			userGroup.setGxyh(updator);
		}
		if(StringUtils.isNotEmpty(createTime)){
			userGroup.setJlxzsj(createTime);
		}
		if(StringUtils.isNotEmpty(updateTime)){
			userGroup.setJlxgsj(updateTime);
		}
		if(StringUtils.isNotEmpty(deleteTime)){
			userGroup.setJlscsj(deleteTime);
		}
		if(StringUtils.isNotEmpty(enableTarg)){
			userGroup.setJlyxzt(enableTarg);
		}
		return userGroup;
	}
	

	
}
