package com.kcp.platform.bpm.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.util.JsonParser;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmDefinition;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.service.BpmDefinitionService;
import com.kcp.platform.bpm.service.BpmNodeConfigService;

@Controller
@RequestMapping("workflow/bpmDef/nodeConfig")
public class BpmNodeConfigController extends BaseMultiActionController{
	@Autowired
	private BpmNodeConfigService nodeConfigService;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	@RequestMapping("")
	public ModelAndView nodeConfig(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		List<BpmNodeConfig> nodeConfigList = nodeConfigService.getNodeConfigByDefId(defId);
		BpmNodeConfig gblNodeConfig = null;
		if(nodeConfigList!=null){
			Iterator<BpmNodeConfig> it = nodeConfigList.iterator();
			while(it.hasNext()){
				BpmNodeConfig nodeConfig = it.next();
				if(BpmNodeConfig.TYPE_GLOBEL.equals(nodeConfig.getSetType())){
					gblNodeConfig = nodeConfig;
					it.remove();
				}
			}
		}
		return new ModelAndView("workflow/bpm/bpmNodeConfig")
					.addObject("defId", defId)
					.addObject("gblNodeConfig", gblNodeConfig)
					.addObject("nodeConfigList", nodeConfigList);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		BpmNodeConfig nodeConfig = nodeConfigService.getNodeConfig(defId, nodeId);
		if(nodeConfig==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/nodeConfigEdit")
					.addObject("nodeConfig",nodeConfig);
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody void updateNodeConfig(HttpServletRequest request){
		BpmNodeConfig nodeConfig = assembleNodeConfig(request);
		nodeConfigService.updateNodeConfig(nodeConfig);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteNodeConfig")
	public @ResponseBody void deleteNodeConfig(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				nodeConfigService.deleteNodeConfig(id);
			}
		}
	}
	
	@RequestMapping("save")
	public @ResponseBody void saveNodeConfig(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeConfigs = StringUtils.getNullBlankStr(request.getParameter("nodeConfigs"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		Map<String,String> nodeConfigMap = JsonParser.parseJsonStr(nodeConfigs);
		String nodeConfigListStr = (String)nodeConfigMap.get("nodeConfigList");
		String globalNodeStr = (String)nodeConfigMap.get("globalNode");
		List<BpmNodeConfig> nodeConfigList = JsonParser.toBeans(nodeConfigListStr,BpmNodeConfig.class);
		BpmNodeConfig globalNode = JsonParser.toBean(globalNodeStr, BpmNodeConfig.class);
		List<BpmNodeConfig> configList = new ArrayList<BpmNodeConfig>();
		if(StringUtils.isNotEmpty(globalNode.getFormType())){
			globalNode.setSetType(BpmNodeConfig.TYPE_GLOBEL);
			globalNode.setDefId(defId);
			globalNode.setActDefId(bpmDefinition.getActDefId());
			configList.add(globalNode);
		}
		if(nodeConfigList!=null){
			for(BpmNodeConfig config:nodeConfigList){
				config.setSetType(BpmNodeConfig.TYPE_TASK);
			}
		}
		configList.addAll(nodeConfigList);
		nodeConfigService.saveNodeConfig(defId, configList);
	}
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmNodeConfig nodeConfig = nodeConfigService.getNodeConfigById(id);
		if(nodeConfig==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/nodeConfigView")
					.addObject("nodeConfig",nodeConfig);
	}
	

	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmNodeConfig assembleNodeConfig(HttpServletRequest request){
		String configId = StringUtils.getNullBlankStr(request.getParameter("configId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String formType = StringUtils.getNullBlankStr(request.getParameter("formType"));
		String formUrl = StringUtils.getNullBlankStr(request.getParameter("formUrl"));
		String formDefName = StringUtils.getNullBlankStr(request.getParameter("formDefName"));
		String formKey = StringUtils.getNullBlankStr(request.getParameter("formKey"));
		String beforeHandler = StringUtils.getNullBlankStr(request.getParameter("beforeHandler"));
		String afterHandler = StringUtils.getNullBlankStr(request.getParameter("afterHandler"));
		BpmNodeConfig nodeConfig = null;
		if(StringUtils.isEmpty(configId)){
			nodeConfig = new BpmNodeConfig();
		}else{
			nodeConfig = nodeConfigService.getNodeConfigById(configId);
		}
		nodeConfig.setDefId(defId);
		nodeConfig.setActDefId(actDefId);
		nodeConfig.setNodeId(nodeId);
		nodeConfig.setFormType(formType);
		nodeConfig.setFormUrl(formUrl);
		nodeConfig.setFormDefName(formDefName);
		nodeConfig.setFormKey(formKey);
		nodeConfig.setBeforeHandler(beforeHandler);
		nodeConfig.setAfterHandler(afterHandler);
		return nodeConfig;
	}
}