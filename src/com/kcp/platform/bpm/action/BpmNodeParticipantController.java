package com.kcp.platform.bpm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.kcp.platform.common.code.service.CodeService;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.JsonParser;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.constants.BpmConstants;
import com.kcp.platform.bpm.entity.BpmNodeConfig;
import com.kcp.platform.bpm.entity.BpmNodeParticipant;
import com.kcp.platform.bpm.service.BpmNodeConfigService;
import com.kcp.platform.bpm.service.BpmNodeParticipantService;

@Controller
@RequestMapping("workflow/bpmDef/nodeParticipant")
public class BpmNodeParticipantController extends BaseMultiActionController{
	@Autowired
	private BpmNodeParticipantService nodeParticipantService;
	
	@Autowired
	private BpmNodeConfigService nodeConfigService;
	
	@Autowired
	private CodeService codeService;
	/**
	 * 跳转至节点人员设置，针对单个节点
	 */
	@RequestMapping("single")
	public ModelAndView single(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		BpmNodeConfig nodeConfig = nodeConfigService.getNodeConfig(defId, nodeId);
		String participantTypeDict = codeService.getCodeItemListAsString(BpmConstants.CODE_PARTICIPANT_TYPE);
		return new ModelAndView("workflow/bpm/nodeParticipantConfig")
				.addObject("nodeConfig", nodeConfig)
				.addObject("participantTypeDict", participantTypeDict);
	}
	
	@RequestMapping("all")
	public ModelAndView all(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		List<BpmNodeConfig> nodeConfigList = nodeConfigService.getNodeConfigByDefId(defId);
		String participantTypeDict = codeService.getCodeItemListAsString(BpmConstants.CODE_PARTICIPANT_TYPE);
		if(nodeConfigList!=null){
			Iterator<BpmNodeConfig> it = nodeConfigList.iterator();
			while(it.hasNext()){
				BpmNodeConfig nodeConfig = it.next();
				if(BpmNodeConfig.TYPE_GLOBEL.equals(nodeConfig.getSetType())){
					it.remove();
				}
			}
		}
		return new ModelAndView("workflow/bpm/bpmParticipantConfig")
					.addObject("nodeConfigList", nodeConfigList)
					.addObject("participantTypeDict", participantTypeDict);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData nodeParticipantList(HttpServletRequest request){
	  	String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
	  	String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("defId", defId);
	 	queryMap.put("nodeId", nodeId);
		List<BpmNodeParticipant> result = nodeParticipantService.findNodeParticipant(queryMap);
		return new GridData(result);
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		BpmNodeConfig nodeConfig = nodeConfigService.getNodeConfig(defId, nodeId);
		BpmNodeParticipant nodeParticipant = new BpmNodeParticipant();
		nodeParticipant.setActDefId(nodeConfig.getActDefId());
		nodeParticipant.setDefId(defId);
		nodeParticipant.setNodeId(nodeId);
		nodeParticipant.setConfigId(nodeConfig.getConfigId());
		return new ModelAndView("workflow/bpm/nodeParticipantEdit")
					.addObject("nodeParticipant", nodeParticipant)
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public @ResponseBody void addNodeParticipant(HttpServletRequest request){
		BpmNodeParticipant nodeParticipant = assembleNodeParticipant(request);
		nodeParticipantService.addNodeParticipant(nodeParticipant);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmNodeParticipant nodeParticipant = nodeParticipantService.getNodeParticipantById(id);
		if(nodeParticipant==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/nodeParticipantEdit")
					.addObject("nodeParticipant",nodeParticipant)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody void updateNodeParticipant(HttpServletRequest request){
		BpmNodeParticipant nodeParticipant = assembleNodeParticipant(request);
		nodeParticipantService.updateNodeParticipant(nodeParticipant);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	public @ResponseBody void deleteNodeParticipant(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				nodeParticipantService.deleteNodeParticipant(id);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmNodeParticipant nodeParticipant = nodeParticipantService.getNodeParticipantById(id);
		if(nodeParticipant==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/bpm/nodeParticipantView")
					.addObject("nodeParticipant",nodeParticipant);
	}
	
	@RequestMapping("saveSn")
	public @ResponseBody void saveSn(HttpServletRequest request)throws Exception{
		String participantJson = StringUtils.getNullBlankStr(request.getParameter("participantJson"));
		List<JSONObject> array = JsonParser.parseJsonArray(participantJson);
		List<BpmNodeParticipant> participantList = new ArrayList<BpmNodeParticipant>();
		for(JSONObject obj:array){
			BpmNodeParticipant btn = new BpmNodeParticipant();
			BeanUtils.copyProperties(btn, obj);
			participantList.add(btn);
		}
		nodeParticipantService.updateSns(participantList);
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("scriptEdit")
	public ModelAndView scriptEdit(HttpServletRequest request){
		return new ModelAndView("workflow/bpm/nodeParticipantScriptEdit");
	}
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmNodeParticipant assembleNodeParticipant(HttpServletRequest request){
		String participantId = StringUtils.getNullBlankStr(request.getParameter("participantId"));
		String configId = StringUtils.getNullBlankStr(request.getParameter("configId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String participantType = StringUtils.getNullBlankStr(request.getParameter("participantType"));
		String participan = StringUtils.getNullBlankStr(request.getParameter("participan"));
		String participanDesc = StringUtils.getNullBlankStr(request.getParameter("participanDesc"));
		String computeType = StringUtils.getNullBlankStr(request.getParameter("computeType"));
		Short extractUser = RequestUtil.getShort(request, "extractUser");
		BpmNodeParticipant nodeParticipant = null;
		if(StringUtils.isEmpty(participantId)){
			nodeParticipant = new BpmNodeParticipant();
		}else{
			nodeParticipant = nodeParticipantService.getNodeParticipantById(participantId);
		}
		nodeParticipant.setConfigId(configId);
		nodeParticipant.setDefId(defId);
		nodeParticipant.setActDefId(actDefId);
		nodeParticipant.setNodeId(nodeId);
		nodeParticipant.setParticipantType(participantType);
		nodeParticipant.setParticipan(participan);
		nodeParticipant.setParticipanDesc(participanDesc);
		nodeParticipant.setComputeType(computeType);
		nodeParticipant.setExtractUser(extractUser);
		return nodeParticipant;
	}
}