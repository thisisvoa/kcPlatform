/**
 * @(#)com.casic27.platform.bpm.action.BpmAgentSettingController
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.bpm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.JsonParser;
import com.casic27.platform.util.RequestUtil;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmAgentDef;
import com.casic27.platform.bpm.entity.BpmAgentSetting;
import com.casic27.platform.bpm.service.BpmAgentSettingService;

@Controller
@RequestMapping("workflow/bpm/agentSetting")
public class BpmAgentSettingController extends BaseMultiActionController{
	
	@Autowired
	private BpmAgentSettingService bpmAgentSettingService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("workflow/bpm/bpmAgentSettingMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmAgentSettingList(HttpServletRequest request)throws Exception{
		Date beginStartDate = RequestUtil.getDate(request, "beginStartDate");
		Date endStartDate = RequestUtil.getDate(request, "endStartDate");
		Date beginEndDate = RequestUtil.getDate(request, "beginEndDate");
		Date endEndDate = RequestUtil.getDate(request, "endEndDate");
		int enabled = RequestUtil.getInt(request, "enabled", -1);
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("beginStartDate", beginStartDate);
	 	queryMap.put("endStartDate", endStartDate);
	 	queryMap.put("beginEndDate", beginEndDate);
	 	queryMap.put("endEndDate", endEndDate);
	 	queryMap.put("enabled", enabled);
	 	queryMap.put("authId", SecurityContext.getCurrentUserId());
	 	
		List<BpmAgentSetting> result = bpmAgentSettingService.findBpmAgentSettingPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("workflow/bpm/bpmAgentSettingEdit")
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("save")
	public @ResponseBody void save(HttpServletRequest request)throws Exception{
		BpmAgentSetting bpmAgentSetting = assembleBpmAgentSetting(request);
		User user = SecurityContext.getCurrentUser();
		bpmAgentSetting.setAuthId(user.getZjid());
		bpmAgentSetting.setAuthName(user.getYhmc());
		bpmAgentSetting.setCreateTime(new Date());
		String defListStr = StringUtils.getNullBlankStr(request.getParameter("defList"));
		List<JSONObject> array = JsonParser.parseJsonArray(defListStr);
		List<BpmAgentDef> defs = new ArrayList<BpmAgentDef>();
		for(JSONObject obj:array){
			BpmAgentDef def = new BpmAgentDef();
			BeanUtils.copyProperties(def, obj);
			defs.add(def);
		}
		bpmAgentSettingService.save(bpmAgentSetting, defs);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmAgentSetting bpmAgentSetting = bpmAgentSettingService.getBpmAgentSettingById(id);
		if(bpmAgentSetting==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmAgentSettingEdit")
					.addObject("bpmAgentSetting",bpmAgentSetting)
					.addObject("type","update");
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("del")
	public @ResponseBody void deleteBpmAgentSetting(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				bpmAgentSettingService.deleteBpmAgentSetting(id);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmAgentSetting bpmAgentSetting = bpmAgentSettingService.getBpmAgentSettingById(id);
		if(bpmAgentSetting==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmAgentSettingView")
					.addObject("bpmAgentSetting",bpmAgentSetting);
	}
	

	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmAgentSetting assembleBpmAgentSetting(HttpServletRequest request)throws Exception{
		String id = RequestUtil.getString(request, "id");
		Date startDate = RequestUtil.getDate(request, "startDate");
		Date endDate = RequestUtil.getDate(request, "endDate");
		int enabled = RequestUtil.getInt(request, "enabled");
		int authType = RequestUtil.getInt(request, "authType");
		String agentId = RequestUtil.getString(request, "agentId");
		String agent = RequestUtil.getString(request, "agent");
		BpmAgentSetting bpmAgentSetting = null;
		if(StringUtils.isEmpty(id)){
			bpmAgentSetting = new BpmAgentSetting();
		}else{
			bpmAgentSetting = bpmAgentSettingService.getBpmAgentSettingById(id);
		}
		bpmAgentSetting.setStartDate(startDate);
		bpmAgentSetting.setEndDate(endDate);
		bpmAgentSetting.setEnabled(enabled);
		bpmAgentSetting.setAuthType(authType);
		bpmAgentSetting.setAgentId(agentId);
		bpmAgentSetting.setAgent(agent);
		return bpmAgentSetting;
	}
}