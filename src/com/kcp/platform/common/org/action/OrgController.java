package com.kcp.platform.common.org.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.org.service.OrgService;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.exception.BusinessException;
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
 *@Version：1.0
 */
@Controller
@RequestMapping("orgCtl")
public class OrgController extends BaseMultiActionController {
	
	@Autowired
	private OrgService orgService; 
	
	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * 获取单位管理信息界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/toOrgMainList")	
	public String toOrgMainList(Model model, HttpServletRequest request){
		String orgId =request.getParameter("id");
		if(orgId == null || "".equals(orgId)){
			orgId = CommonConst.TREE_ROOT_ID;
		}
		model.addAttribute("orgId", orgId);
		return "common/org/orgMain";
	}
	
	/**
	 * 获取单位树信息管理界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/toOrgTree")
	public String toOrgTree(Model model, HttpServletRequest request){
		String orgId =request.getParameter("id");
		if(orgId == null || "".equals(orgId)){
			orgId = CommonConst.TREE_ROOT_ID;
		}
		model.addAttribute("orgId", orgId);
		return "common/org/orgTree";
	}
	
	/**
	 * 获取单位信息列表管理界面
	 * @param model
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/toOrgInfoList")
	public String toOrgInfoList(Model model, HttpServletRequest request) throws UnsupportedEncodingException{
		String parentOrg = StringUtils.getNullBlankStr(request.getParameter("parentOrg"));
		Org org = new Org();
		org.setSjdw_zjid(parentOrg);
		org.setJlyxzt(CommonConst.ENABLE_FLAG);
		model.addAttribute("org", org);
		return "common/org/orgList";		
	}
	
	@RequestMapping("orgList")
	public @ResponseBody GridData orgList(HttpServletRequest request){
		String orgNum = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("orgNum")));
		String orgName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("orgName")));
		String parentOrg = StringUtils.getNullBlankStr(request.getParameter("parentOrg"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		User currentUser = SecurityContext.getCurrentUser();
		if(!currentUser.isSuperAdmin()){
			String rootDwId = orgService.getPermissionRootOrgId();
			if(StringUtils.isEmpty(rootDwId)){
				return null;
			}else{
				queryMap.put("zjid", rootDwId);
			}
		}
		queryMap.put("dwbh", orgNum);
		queryMap.put("dwmc", orgName);
		queryMap.put("sjdw_zjid", parentOrg);
		queryMap.put("jlyxzt", CommonConst.ENABLE_FLAG);
		List<Map<String, Object>> orgList = orgService.findOrgPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(orgList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 异步加载单位树
	 * @param model
	 * @param request 根据单位ID(后期数据权限扩展--当前用户所属单位Id)
	 * @return
	 */
	@RequestMapping("asynOrgTree")
	public @ResponseBody List<Map<String,Object>> asynLoadOrgTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String pOrgId = StringUtils.getNullBlankStr(request.getParameter("id"));
		User user = SecurityContext.getCurrentUser();
		if(user.isSuperAdmin()){
			List<Org> orgList = null;
			if(StringUtils.isEmpty(pOrgId)){
				orgList = orgService.queryChildOrgListByPOrgId("0");
			}else{
				orgList = orgService.queryChildOrgListByPOrgId(pOrgId);
			}
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			for(Org org:orgList){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", org.getZjid());
				node.put("name", org.getDwmc());
				node.put("pId", org.getSjdw_zjid());
				node.put("dwbh", org.getDwbh());
				node.put("dwjc", org.getDwjc());
				node.put("dwfzr", org.getDwfzr());
				node.put("dwjb", org.getDwjb());
				node.put("dwlx", org.getDwlx());
				node.put("isParent", true);
				result.add(node);
			}
			return result;
		}else{
			String rootDwId = orgService.getPermissionRootOrgId();
			if(StringUtils.isEmpty(rootDwId)){
				return null;
			}
			List<Org> orgList = null;
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(StringUtils.isEmpty(pOrgId)){
				orgList = orgService.getParentOrgList(rootDwId);
				for(Org org:orgList){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", org.getZjid());
					node.put("name", org.getDwmc());
					node.put("pId", org.getSjdw_zjid());
					node.put("dwbh", org.getDwbh());
					node.put("dwjc", org.getDwjc());
					node.put("dwfzr", org.getDwfzr());
					node.put("dwjb", org.getDwjb());
					node.put("dwlx", org.getDwlx());
					if(org.getZjid().equals(rootDwId)){
						node.put("isParent", true);
					}else{
						node.put("enable", false);
					}
					result.add(node);
				}
			}else{
				orgList = orgService.queryChildOrgListByPOrgId(pOrgId);
				for(Org org:orgList){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", org.getZjid());
					node.put("name", org.getDwmc());
					node.put("pId", org.getSjdw_zjid());
					node.put("dwbh", org.getDwbh());
					node.put("dwjc", org.getDwjc());
					node.put("dwfzr", org.getDwfzr());
					node.put("dwjb", org.getDwjb());
					node.put("dwlx", org.getDwlx());
					node.put("isParent", true);
					result.add(node);
				}
			}
			return result;
		}
	}
	
	/**
	 * 根据的单位Id查询该单位和子单位的信息列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("queryOrgList")
	public String queryOrgInfoListById(Model model, HttpServletRequest request){
		String pOrgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));
		List<Org> orgList = new ArrayList<Org>();
		orgList = orgService.queryChildOrgListByPOrgId(pOrgId);
		model.addAttribute("orgList", orgList);
		return "common/org/orgList";
	}
	
	/**
	 * 单位新增界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddOrg")
	public String toAddOrg(Model model, HttpServletRequest request){
		String pOrgId = StringUtils.getNullBlankStr(request.getParameter("pOrgId"));
		Org parentOrg = null;
		if(pOrgId != null && !pOrgId.isEmpty()){
			parentOrg = orgService.getOrgById(pOrgId);
		}
		
		model.addAttribute("parentOrg", parentOrg);
		model.addAttribute("operModel", CommonConst.ADD_OPERMODEL );
		return "common/org/orgEdit";
		
	}
	
	/**
	 * 单位编辑界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toEditOrg")
	public String toEditOrg(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Org orgInfo = orgService.getOrgById(orgId);
		if(orgInfo==null){
			throw new BusinessException("所选单位不存在!");
		}
		Org parentOrg = orgService.getOrgById(orgInfo.getSjdw_zjid());
		model.addAttribute("orgInfo", orgInfo);
		model.addAttribute("parentOrg", parentOrg);
		model.addAttribute("operModel", CommonConst.UPDATE_OPERMODEL );
		return "common/org/orgEdit";
		
	}
	
	/**
	 * 单位详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toViewOrg")
	public String toViewOrg(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Org orgInfo = orgService.getOrgById(orgId);
		if(orgInfo==null){
			throw new BusinessException("所选单位不存在!");
		}
		if(StringUtils.isNotEmpty(orgInfo.getSjdw_zjid())){
			Org parentOrg = orgService.getOrgById(orgInfo.getSjdw_zjid());
			model.addAttribute("parentOrg", parentOrg);
		}
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "单位信息管理", "[查看] 查看[单位名称："+orgInfo.getDwmc()+"] [单位编号："+orgInfo.getDwbh()+"]的单位信息");
		model.addAttribute("orgInfo", orgInfo);
		return "common/org/orgView";
		
	}
	/**
	 * 逻辑删除单位信息
	 * @param model
	 * @param request
	 */
	@RequestMapping("logicDelOrg")
	public @ResponseBody void logicDelOrg(Model model, HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = ids.split(",");
			for(String id : idArray){
				Org org = orgService.getOrgById(id);
				orgService.logicDelOrg(org);
			}
		}	
		model.addAttribute("isSuccess", true);
	}
	
	
	/**
	 * 新增单位信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("addOrgInfo")
	public @ResponseBody void addOrgInfo(Model model, HttpServletRequest request){
		String orgNum = StringUtils.getNullBlankStr(request.getParameter("orgNum"));
		String orgName = StringUtils.getNullBlankStr(request.getParameter("orgName"));
		String simpleName = StringUtils.getNullBlankStr(request.getParameter("simpleName"));
		String parentOrg = StringUtils.getNullBlankStr(request.getParameter("parentOrg"));
		parentOrg = StringUtils.isEmpty(parentOrg)?CommonConst.TREE_ROOT_ID:parentOrg;
		String orgManager = StringUtils.getNullBlankStr(request.getParameter("orgManager"));
		String telephoneNo = StringUtils.getNullBlankStr(request.getParameter("telephoneNo"));
		String email = StringUtils.getNullBlankStr(request.getParameter("email"));
		String orgType = StringUtils.getNullBlankStr(request.getParameter("orgType"));
		String orgLevel = StringUtils.getNullBlankStr(request.getParameter("orgLevel"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		Org org = new Org();
		org.setDwbh(orgNum);
		org.setDwmc(orgName);
		org.setDwjc(simpleName);
		org.setDwdh(telephoneNo);
		org.setDwyx(email);
		org.setDwfzr(orgManager);
		org.setSjdw_zjid(parentOrg);
		org.setDwlx(orgType);
		org.setDwjb(orgLevel);
		org.setBz(remark);
		org.setJlscsj("");
		org.setJlyxzt(CommonConst.YES);
		orgService.addOrg(org);
	}
	/**
	 * 编辑单位信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("editOrgInfo")
	public @ResponseBody void editOrgInfo(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));
		String orgNum = StringUtils.getNullBlankStr(request.getParameter("orgNum"));
		String orgName = StringUtils.getNullBlankStr(request.getParameter("orgName"));
		String simpleName = StringUtils.getNullBlankStr(request.getParameter("simpleName"));
		String parentOrg = StringUtils.getNullBlankStr(request.getParameter("parentOrg"));
		parentOrg = StringUtils.isEmpty(parentOrg)?CommonConst.TREE_ROOT_ID:parentOrg;
		String orgManager = StringUtils.getNullBlankStr(request.getParameter("orgManager"));
		String telephoneNo = StringUtils.getNullBlankStr(request.getParameter("telephoneNo"));
		String email = StringUtils.getNullBlankStr(request.getParameter("email"));
		String orgType = StringUtils.getNullBlankStr(request.getParameter("orgType"));
		String orgLevel = StringUtils.getNullBlankStr(request.getParameter("orgLevel"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		Org org = new Org();
		org.setZjid(orgId);
		org.setDwbh(orgNum);
		org.setDwmc(orgName);
		org.setDwjc(simpleName);
		org.setDwdh(telephoneNo);
		org.setDwyx(email);
		org.setDwfzr(orgManager);
		org.setSjdw_zjid(parentOrg);
		org.setDwlx(orgType);
		org.setDwjb(orgLevel);
		org.setBz(remark);
		if(orgId.equals(parentOrg)){
			throw new BusinessException("上级单位不能设置为同一单位！");
		}
		orgService.updateOrg(org);
	}	
	
	/**
	 * 校验单位属性唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("validateUniqueOrgNo")
	public @ResponseBody Object[] validateUniqueOrgNo(Model model, HttpServletRequest request){
		String orgId = StringUtils.getNullBlankStr(request.getParameter("orgId"));
		String orgNo = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		Org org = new Org();
		org.setZjid(orgId);
		org.setDwbh(orgNo);
		org.setJlyxzt("1");
		boolean isUnique = orgService.validateUniqueOrg(org);
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = isUnique;
		return result;
	}
	
	@RequestMapping("orgSelector")
	public String orgSelector(Model model, HttpServletRequest request){
		String orgId = request.getParameter("orgId");
		String filterId = request.getParameter("filterId");
		Org org = orgService.getOrgById(orgId);
		model.addAttribute("org", org);
		model.addAttribute("filterId", filterId);
		return "common/org/orgSelector";
	}
	
	/**
	 * 通用的组织选择器
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("cmp/orgSelector")
	public String orgCmpSelector(Model model, HttpServletRequest request){
		boolean multiselect = StringUtils.getBoolean(StringUtils.getNullBlankStr(request.getParameter("multiselect")), true);
		model.addAttribute("multiselect", multiselect);
		return "component/orgSelector";
	}
	@RequestMapping("cmp/selectedOrgList")
	public @ResponseBody GridData selectedOrgList(HttpServletRequest request){
		String selectedOrg = StringUtils.getNullBlankStr(request.getParameter("selectedOrg"));
		List<Map<String,Object>> result = null;
		if(StringUtils.isEmpty(selectedOrg)){
			result = new ArrayList<Map<String,Object>>();
		}else{
			result = orgService.getOrgByIds(selectedOrg.split(","));
		}
		return new GridData(result);
	}
	
	@RequestMapping("orgUserInfo")
	public String orgUserInfo(Model model, HttpServletRequest request){
		String orgId = request.getParameter("id");
		Org orgInfo = orgService.getOrgById(orgId);
		if(orgInfo==null){
			throw new BusinessException("所选单位不存在!");
		}
		if(StringUtils.isNotEmpty(orgInfo.getSjdw_zjid())){
			Org parentOrg = orgService.getOrgById(orgInfo.getSjdw_zjid());
			model.addAttribute("parentOrg", parentOrg);
		}
		model.addAttribute("orgInfo", orgInfo);
		return "common/org/orgUserInfo";
	}
}
