package com.kcp.platform.bpm.action;

import java.util.ArrayList;
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

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.JsonParser;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.bpm.entity.BpmNodeButton;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.service.BpmDefinitionService;
import com.kcp.platform.bpm.service.BpmNodeButtonService;
import com.kcp.platform.bpm.service.BpmNodeConfigService;
import com.kcp.platform.bpm.util.BpmButton;
import com.kcp.platform.bpm.util.BpmButtonParser;
import com.kcp.platform.bpm.util.BpmNodeButtonXml;

@Controller
@RequestMapping("workflow/bpmDef/nodeButton")
public class BpmNodeButtonController extends BaseMultiActionController{
	@Autowired
	private BpmNodeButtonService nodeButtonService;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	@Autowired
	private BpmNodeConfigService bpmNodeConfigService;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		return new ModelAndView("workflow/bpm/bpmNodeButtonMain")
						.addObject("defId", defId);
	}
	
	
	@RequestMapping("nodeBtnList")
	public @ResponseBody GridData nodeBtnList(HttpServletRequest request)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		List<Map<String,Object>> nodeButtons = nodeButtonService.getNodeButtonsByDefId(defId);
		return new GridData(nodeButtons);
	}
	
	@RequestMapping("/toBtnList")
	public ModelAndView bpmNodeButtonList(HttpServletRequest request)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType"));
		String btnTypesEditoptions = BpmButtonParser.parse().toEditOption();
		return new ModelAndView("workflow/bpm/bpmNodeButtonList")
					.addObject("nodeId", nodeId)
					.addObject("nodeType", nodeType)
					.addObject("defId", defId)
					.addObject("btnTypesEditoptions", btnTypesEditoptions);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData nodeButtonList(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("defId", defId);
	 	queryMap.put("nodeId", nodeId);
	 	queryMap.put("nodeType", nodeType);
		List<BpmNodeButton> result = nodeButtonService.findNodeButton(queryMap);
		return new GridData(result);
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		BpmNodeButton bpmNodeButton = new BpmNodeButton();
		bpmNodeButton.setActDefId(bpmDefinition.getActDefId());
		bpmNodeButton.setDefId(defId);
		bpmNodeButton.setNodeId(nodeId);
		bpmNodeButton.setNodeType(nodeType);
		return new ModelAndView("workflow/bpm/bpmNodeButtonEdit")
					.addObject("bpmNodeButton", bpmNodeButton)
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public @ResponseBody void addNodeButton(HttpServletRequest request){
		BpmNodeButton nodeButton = assembleNodeButton(request);
		nodeButtonService.addNodeButton(nodeButton);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmNodeButton nodeButton = nodeButtonService.getNodeButtonById(id);
		if(nodeButton==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/bpmNodeButtonEdit")
					.addObject("bpmNodeButton",nodeButton)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody void updateNodeButton(HttpServletRequest request){
		BpmNodeButton nodeButton = assembleNodeButton(request);
		nodeButtonService.updateNodeButton(nodeButton);
	}
	
	/**
	 * 保存序号
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("saveSn")
	public @ResponseBody void saveSn(HttpServletRequest request)throws Exception{
		String btnJson = StringUtils.getNullBlankStr(request.getParameter("btnJson"));
		List<JSONObject> array = JsonParser.parseJsonArray(btnJson);
		List<BpmNodeButton> btnList = new ArrayList<BpmNodeButton>();
		for(JSONObject obj:array){
			BpmNodeButton btn = new BpmNodeButton();
			BeanUtils.copyProperties(btn, obj);
			btnList.add(btn);
		}
		nodeButtonService.updateSns(btnList);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	public @ResponseBody void deleteNodeButton(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				nodeButtonService.deleteNodeButtonById(id);
			}
		}
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteByDefId")
	public @ResponseBody void deleteNodeButtonByDefId(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		if(StringUtils.isNotEmpty(defId)){
			nodeButtonService.deleteNodeButtonByDefId(defId);
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmNodeButton nodeButton = nodeButtonService.getNodeButtonById(id);
		if(nodeButton==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/nodeButtonView")
					.addObject("nodeButton",nodeButton);
	}
	
	/**
	 * 校验按钮名称
	 * @param request
	 * @return
	 */
	@RequestMapping("validateBtnName")
	public @ResponseBody Object[] validateBtnName(HttpServletRequest request){
		String btnId = StringUtils.getNullBlankStr(request.getParameter("btnId"));
		String btnName = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmNodeButton nodeButton = new BpmNodeButton();
		nodeButton.setBtnId(btnId);
		nodeButton.setBtnName(btnName);
		int count = nodeButtonService.countNodeButton(nodeButton);
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
	 * 初始化所有操作按钮
	 * @param request
	 */
	@RequestMapping("initNodeButtons")
	public @ResponseBody void initNodeButtons(HttpServletRequest request)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		nodeButtonService.initAll(defId);
	}
	
	/**
	 * 初始化单节点操作按钮
	 * @param request
	 */
	@RequestMapping("initNodeButton")
	public @ResponseBody void initNodeButton(HttpServletRequest request)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType"));
		nodeButtonService.init(defId, nodeId, nodeType);
	}
	
	/**
	 * 按钮类型列表 
	 * @param request
	 */
	@RequestMapping("btnTypesList")
	public @ResponseBody List<BpmButton> btnTypesList(HttpServletRequest request)throws Exception{
	     BpmNodeButtonXml bpmButtonList = BpmButtonParser.parse();
	     List<BpmButton> list = bpmButtonList.getButtons();
	     return list;
	}
	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmNodeButton assembleNodeButton(HttpServletRequest request){
		String btnId = StringUtils.getNullBlankStr(request.getParameter("btnId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String btnName = StringUtils.getNullBlankStr(request.getParameter("btnName"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType1"));
		String type = StringUtils.getNullBlankStr(request.getParameter("type"));
		String beforeHandler = StringUtils.getNullBlankStr(request.getParameter("beforeHandler"));
		String afterHandler = StringUtils.getNullBlankStr(request.getParameter("afterHandler"));
		BpmNodeButton nodeButton = null;
		if(StringUtils.isEmpty(btnId)){
			nodeButton = new BpmNodeButton();
		}else{
			nodeButton = nodeButtonService.getNodeButtonById(btnId);
		}
		nodeButton.setDefId(defId);
		nodeButton.setActDefId(actDefId);
		nodeButton.setNodeId(nodeId);
		nodeButton.setBtnName(btnName);
		nodeButton.setNodeType(nodeType);
		nodeButton.setType(type);
		nodeButton.setBeforeHandler(beforeHandler);
		nodeButton.setAfterHandler(afterHandler);
		return nodeButton;
	}
	
	@RequestMapping("validateType")
	public @ResponseBody Object[] validateType(HttpServletRequest request){
		String btnId = StringUtils.getNullBlankStr(request.getParameter("btnId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String nodeType = StringUtils.getNullBlankStr(request.getParameter("nodeType"));
		String type = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmNodeButton nodeButton = new BpmNodeButton();
		nodeButton.setBtnId(btnId);
		nodeButton.setDefId(defId);
		nodeButton.setNodeId(nodeId);
		nodeButton.setNodeType(nodeType);
		nodeButton.setType(type);
		int count = nodeButtonService.countNodeButton(nodeButton);
		Object[] result = new Object[2];
		result[0] = filedId;
		if(count>0){
			result[1] = false;
		}else{
			result[1] = true;
		}
		return result;
	}
}