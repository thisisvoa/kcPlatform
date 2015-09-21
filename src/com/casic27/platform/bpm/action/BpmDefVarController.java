/**
 * @(#)com.casic27.platform.bpm.action.BpmDefVarController
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
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmDefVar;
import com.casic27.platform.bpm.service.BpmDefVarService;

@Controller
@RequestMapping("workflow/bpmDef/var")
public class BpmDefVarController extends BaseMultiActionController{
	@Autowired
	private BpmDefVarService bpmDefVarService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		return new ModelAndView("workflow/bpm/bpmDefVarMain")
						.addObject("defId", defId);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmDefVarList(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("defId", defId);
		List<BpmDefVar> result = bpmDefVarService.findBpmDefVar(queryMap);
		return new GridData(result);
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefVar bpmDefVar = new BpmDefVar();
		bpmDefVar.setDefId(defId);
		return new ModelAndView("workflow/bpm/bpmDefVarEdit")
					.addObject("bpmDefVar",bpmDefVar)
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("addBpmDefVar")
	public @ResponseBody void addBpmDefVar(HttpServletRequest request){
		BpmDefVar bpmDefVar = assembleBpmDefVar(request);
		bpmDefVarService.addBpmDefVar(bpmDefVar);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmDefVar bpmDefVar = bpmDefVarService.getBpmDefVarById(id);
		if(bpmDefVar==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmDefVarEdit")
					.addObject("bpmDefVar",bpmDefVar)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("updateBpmDefVar")
	public @ResponseBody void updateBpmDefVar(HttpServletRequest request){
		BpmDefVar bpmDefVar = assembleBpmDefVar(request);
		bpmDefVarService.updateBpmDefVar(bpmDefVar);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteBpmDefVar")
	public @ResponseBody void deleteBpmDefVar(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				bpmDefVarService.deleteBpmDefVar(id);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmDefVar bpmDefVar = bpmDefVarService.getBpmDefVarById(id);
		if(bpmDefVar==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmDefVarView")
					.addObject("bpmDefVar",bpmDefVar);
	}
	
	@RequestMapping("validateVarKey")
	public @ResponseBody Object[] validateVarKey(HttpServletRequest request){
		String varId = StringUtils.getNullBlankStr(request.getParameter("varId"));
		String varKey = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmDefVar bpmDefVar = new BpmDefVar();
		bpmDefVar.setVarId(varId);
		bpmDefVar.setVarKey(varKey);
		int count = bpmDefVarService.countBpmDefVar(bpmDefVar);
		
		Object[] result = new Object[2];
		result[0] = filedId;
		if(count>0){
			result[1] = false;
		}else{
			result[1] = true;
		}
		return result;
	}

	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmDefVar assembleBpmDefVar(HttpServletRequest request){
		String varId = StringUtils.getNullBlankStr(request.getParameter("varId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String varName = StringUtils.getNullBlankStr(request.getParameter("varName"));
		String varKey = StringUtils.getNullBlankStr(request.getParameter("varKey"));
		String varDataType = StringUtils.getNullBlankStr(request.getParameter("varDataType"));
		String defValue = StringUtils.getNullBlankStr(request.getParameter("defValue"));
		String nodeName = StringUtils.getNullBlankStr(request.getParameter("nodeName"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String actDeployId = StringUtils.getNullBlankStr(request.getParameter("actDeployId"));
		String varScope = StringUtils.getNullBlankStr(request.getParameter("varScope"));
		BpmDefVar bpmDefVar = null;
		if(StringUtils.isEmpty(varId)){
			bpmDefVar = new BpmDefVar();
		}else{
			bpmDefVar = bpmDefVarService.getBpmDefVarById(varId);
		}
		bpmDefVar.setDefId(defId);
		bpmDefVar.setVarName(varName);
		bpmDefVar.setVarKey(varKey);
		bpmDefVar.setVarDataType(varDataType);
		bpmDefVar.setDefValue(defValue);
		bpmDefVar.setNodeName(nodeName);
		bpmDefVar.setNodeId(nodeId);
		bpmDefVar.setActDeployId(actDeployId);
		bpmDefVar.setVarScope(varScope);
		return bpmDefVar;
	}
}