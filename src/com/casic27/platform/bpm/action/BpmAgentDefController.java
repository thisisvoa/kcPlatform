/**
 * @(#)com.casic27.platform.bpm.action.BpmAgentDefController
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmAgentDef;
import com.casic27.platform.bpm.service.BpmAgentDefService;

@Controller
@RequestMapping("workflow/bpm/agentDef")
public class BpmAgentDefController extends BaseMultiActionController{
	@Autowired
	private BpmAgentDefService bpmAgentDefService;

	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmAgentDefList(HttpServletRequest request){
	  	String settingId = StringUtils.getNullBlankStr(request.getParameter("settingId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("settingId", settingId);
	 	List<BpmAgentDef> result = null;
	 	if(StringUtils.isNotEmpty(settingId)){
	 		result = bpmAgentDefService.findBpmAgentDef(queryMap);
	 	}
		return new GridData(result);
	}
}