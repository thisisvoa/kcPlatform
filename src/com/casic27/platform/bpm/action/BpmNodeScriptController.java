/**
 * @(#)com.casic27.platform.bpm.action.NodeScriptController
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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmNodeScript;
import com.casic27.platform.bpm.service.BpmNodeScriptService;

@Controller
@RequestMapping("workflow/bpmDef/nodeScript")
public class BpmNodeScriptController extends BaseMultiActionController{
	@Autowired
	private BpmNodeScriptService nodeScriptService;

	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("edit")
	public ModelAndView edit(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String type = StringUtils.getNullBlankStr(request.getParameter("type"));
		Map<String,BpmNodeScript> map = nodeScriptService.getScriptMapByNodeId(nodeId, defId);
		return new ModelAndView("workflow/bpm/nodeScriptEdit")
				.addObject("map", map)
				.addObject("type", type)
				.addObject("nodeId", nodeId)
				.addObject("defId", defId)
				.addObject("actDefId", actDefId);
	}
	

	@RequestMapping("save")
	public @ResponseBody void save(HttpServletRequest request){
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
	    String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
	    String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
	    String[] aryRemark = request.getParameterValues("remark");
	    String[] aryScript = request.getParameterValues("script");
	    String[] aryScriptType = request.getParameterValues("scriptType");
	    List<BpmNodeScript> list = new ArrayList<BpmNodeScript>();
	    for (int i = 0; i < aryScriptType.length; i++) {
	        String remark = aryRemark[i];
	        String script = aryScript[i];
	        String scriptType = aryScriptType[i];
	        if (StringUtils.isNotEmpty(script)) {
	          BpmNodeScript nodeScript = new BpmNodeScript();
	          nodeScript.setRemark(remark);
	          nodeScript.setScript(script);
	          nodeScript.setScriptType(scriptType);
	          list.add(nodeScript);
	        }
	    }
	    nodeScriptService.saveScript(nodeId, defId, actDefId, list);
	}
}