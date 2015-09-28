package com.kcp.platform.bpm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmAgentDef;
import com.kcp.platform.bpm.service.BpmAgentDefService;

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