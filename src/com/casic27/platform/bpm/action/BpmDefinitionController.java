/**
 * @(#)com.casic27.platform.bpm.action.BpmDefinitionController
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
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.RequestUtil;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmNode;
import com.casic27.platform.bpm.entity.BpmCatalog;
import com.casic27.platform.bpm.graph.ShapeMeta;
import com.casic27.platform.bpm.service.BpmDefinitionService;
import com.casic27.platform.bpm.service.BpmService;
import com.casic27.platform.bpm.service.BpmCatalogService;
import com.casic27.platform.bpm.util.BpmUtil;

@Controller
@RequestMapping("workflow/bpmDef")
public class BpmDefinitionController extends BaseMultiActionController{
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private BpmCatalogService bpmCatalogService;
	
	@Autowired
	private BpmService bpmService;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("/workflow/bpm/bpmDefMain");
	}
	
	@RequestMapping("tolist")
	public ModelAndView tolist(HttpServletRequest request){
		String typeId = StringUtils.getNullBlankStr(request.getParameter("typeId"));
		return new ModelAndView("/workflow/bpm/bpmDefList")
					.addObject("typeId", typeId);
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmDefinitionList(HttpServletRequest request){
	  	String typeId = StringUtils.getNullBlankStr(request.getParameter("typeId"));
	  	String subject = StringUtils.getNullBlankStr(request.getParameter("subject"));
	  	String defKey = StringUtils.getNullBlankStr(request.getParameter("defKey"));
	  	String status = StringUtils.getNullBlankStr(request.getParameter("status"));
	  	String usable = StringUtils.getNullBlankStr(request.getParameter("usable"));
	  	String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("typeId", typeId);
	 	queryMap.put("subject", subject);
	 	queryMap.put("defKey", defKey.toUpperCase());
	 	queryMap.put("status", status);
	 	queryMap.put("usable", usable);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
	 	queryMap.put("isMain", CommonConst.YES);
	 	
	 	List<BpmDefinition> result = bpmDefinitionService.findBpmDefinition(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("/workflow/bpm/bpmDefinitionEdit")
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public @ResponseBody String addBpmDefinition(HttpServletRequest request){
		BpmDefinition bpmDefinition = assembleBpmDefinition(request);
		if(StringUtils.isEmpty(bpmDefinition.getInstNameRule())){
			bpmDefinition.setInstNameRule(BpmDefinition.DefaultSubjectRule);
		}
		bpmDefinition.setStatus(BpmDefinition.STATUS_UNDEPLOYED.toString());//默认未发布
		bpmDefinition.setToFirstNode(CommonConst.YES);
		bpmDefinition.setDirectStart(CommonConst.NO);
		bpmDefinition.setSameExecutorJump(CommonConst.NO);
		bpmDefinition.setUsable(CommonConst.YES);
		bpmDefinition.setIsMain(CommonConst.YES);
		bpmDefinitionService.addBpmDefinition(bpmDefinition);
		return bpmDefinition.getDefId();
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody boolean updateBpmDefinition(HttpServletRequest request)throws Exception{
		try {
			Boolean isDeploy = Boolean.valueOf(Boolean.parseBoolean(request.getParameter("deploy")));
			BpmDefinition bpmDefinition = assembleBpmDefinition(request);
			bpmDefinitionService.save(bpmDefinition, isDeploy);
			return true;
		} catch (Exception e) {
			logger.error("流程修改失败!", e);
			return false;
		}
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("del")
	public @ResponseBody void deleteBpmDefinition(HttpServletRequest request){
		String[] ids = RequestUtil.getStringAryByStr(request, "ids");
		String isOnlyVersion = request.getParameter("isOnlyVersion");
		boolean onlyVersion = "true".equals(isOnlyVersion) ? true:false;
		for(String id:ids){
			bpmDefinitionService.deleteBpmDefinition(id, onlyVersion);
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(id);
		if(bpmDefinition==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("/workflow/bpm/bpmDefinitionView")
					.addObject("bpmDefinition",bpmDefinition);
	}
	/**
	 * 获取流程完整定义信息
	 * @param request
	 * @return
	 */
	@RequestMapping("getFullDef")
	public @ResponseBody BpmDefinition getBpmDefinitionById(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		if(bpmDefinition==null){
			throw new BusinessException("流程定义不存在！");
		}
		return bpmDefinition;
	}
	
	/**
	 * 流程编辑器
	 * @param request
	 * @return
	 */
	@RequestMapping("bpmEditor")
	public ModelAndView bpmEditor(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		if(StringUtils.isEmpty(defId)){
			return new ModelAndView("workflow/bpm/bpmEditor");
		}else{
			BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
			if(bpmDefinition==null){
				throw new BusinessException("选择的记录不存在！");
			}
			return new ModelAndView("workflow/bpm/bpmEditor")
				.addObject("bpmDefinition", bpmDefinition);
		}
		
	}
	
	/**
	 * 发布流程
	 * @param request
	 */
	@RequestMapping("deploy")
	@ResponseBody
	public void deploy(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		bpmDefinitionService.deploy(defId);
	}
	
	/**
	 * 流程设置页面
	 * @param request
	 * @return
	 */
	@RequestMapping("defConfig")
	public ModelAndView defConfig(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefinition bpmDef = bpmDefinitionService.getBpmDefinitionById(defId);
		if(bpmDef==null){
			throw new BusinessException("流程定义不存在!");
		}
		return new ModelAndView("workflow/bpm/bpmDefConfig")
						.addObject("bpmDef", bpmDef);
	}
	
	/**
	 * 流程定义明细
	 * @param request
	 * @return
	 */
	@RequestMapping("detail")
	public ModelAndView detail(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefinition bpmDef = bpmDefinitionService.getBpmDefinitionById(defId);
		if(bpmDef==null){
			throw new BusinessException("流程定义不存在!");
		}
		User creator = userService.getUserById(bpmDef.getCreator());
		User modifyUser = userService.getUserById(bpmDef.getModifyUser());
		BpmCatalog bpmCatalog = bpmCatalogService.getBpmCatalogById(bpmDef.getTypeId());
		return new ModelAndView("workflow/bpm/bpmDefDetail")
					.addObject("bpmDef", bpmDef)
					.addObject("creator", creator)
					.addObject("modifyUser", modifyUser)
					.addObject("bpmCatalog", bpmCatalog);
	}
	
	/**
	 * 节点设置
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("graphNav")
	public ModelAndView graphNav(HttpServletRequest request)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		ModelAndView  mv = new ModelAndView("workflow/bpm/bpmGraphNav");
		if(StringUtils.isNotEmpty(bpmDefinition.getActDeployId())){
			String defXml = this.bpmService.getDefXmlByDeployId(bpmDefinition.getActDeployId());
			mv.addObject("defXml", defXml);
			ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
			mv.addObject("shapeMeta", shapeMeta);
		}
		return mv.addObject("bpmDefinition", bpmDefinition);
	}
	
	/**
	 * 设置流程分支跳转条件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("setCondition" )
	public ModelAndView setCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deployId = StringUtils.getNullBlankStr(request.getParameter("deployId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		ProcessDefinitionEntity proDefEntity = bpmService.getProcessDefinitionByDeployId(deployId);
		ActivityImpl curActivity = proDefEntity.findActivity(nodeId);
		List<BpmNode> incomeNodes = new ArrayList<BpmNode>();
		List<BpmNode> outcomeNodes = new ArrayList<BpmNode>();

		List<PvmTransition> inTrans = curActivity.getIncomingTransitions();
		for (PvmTransition tran : inTrans) {
			ActivityImpl act = (ActivityImpl) tran.getSource();
			String id = act.getId();
			String nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = Boolean.valueOf(act.getProperty("multiInstance") != null);
			incomeNodes.add(new BpmNode(id, nodeName, nodeType, isMultiple));
		}

		String xml = this.bpmService.getDefXmlByDeployId(deployId);
		Map<String, String> conditionMap = BpmUtil.getDecisionConditions(xml, nodeId);
		List<PvmTransition> outTrans = curActivity.getOutgoingTransitions();
		for (PvmTransition tran : outTrans) {
			ActivityImpl act = (ActivityImpl) tran.getDestination();
			String id = act.getId();
			String nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = Boolean.valueOf(act
					.getProperty("multiInstance") != null);
			BpmNode bpmNode = new BpmNode(id, nodeName, nodeType, isMultiple);
			String condition = (String) conditionMap.get(id);
			if (condition != null) {
				bpmNode.setCondition(condition);
			}
			outcomeNodes.add(bpmNode);
		}
		return new ModelAndView("workflow/bpm/bpmDefSetCondition")
					.addObject("defId", defId)
					.addObject("nodeId", nodeId)
					.addObject("deployId", deployId)
					.addObject("incomeNodes", incomeNodes)
					.addObject("outcomeNodes", outcomeNodes);
	}
	
	/**
	 * 保存流程分支跳转条件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveCondition")
	public @ResponseBody void saveCondition(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		String[] aryTask = request.getParameterValues("task");
		String[] aryCondition = request.getParameterValues("condition");
		Map<String,String> map = new HashMap<String,String>();
		for (int i = 0; i < aryTask.length; i++) {
			map.put(aryTask[i], aryCondition[i]);
		}
		bpmService.saveCondition(defId, nodeId, map);
	}
	/**
	 * 任务节点选择器
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("taskNodeSelector")
	public ModelAndView taskNodeSelector(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		return new ModelAndView("workflow/bpm/taskNodeSelector").addObject("actDefId", actDefId)
								.addObject("nodeId", nodeId);
	}
	
	/**
	 * 任务节点选择列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("taskNodeList")
	public @ResponseBody GridData taskNodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		List<Map<String, String>> taskList = this.bpmService.getTaskNodes(actDefId, nodeId);
		return new GridData(taskList);
	}
	
	/**
	 * 任务节点选择器
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("taskNodeRoleSelector")
	public ModelAndView taskNodeRoleSelector(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		return new ModelAndView("workflow/bpm/taskNodeRoleSelector").addObject("actDefId", actDefId)
								.addObject("nodeId", nodeId);
	}
	
	/**
	 * 其他参数页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("otherParam")
	public ModelAndView otherParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		return new ModelAndView("workflow/bpm/bpmDefOtherParam")
					.addObject("bpmDefinition", bpmDefinition);
	}
	
	/**
	 * 保存参数
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("saveParam")
	public @ResponseBody void saveParam(HttpServletRequest request) throws Exception{
		String defId = RequestUtil.getString(request,"defId");
		String instNameRule = RequestUtil.getString(request, "instNameRule");
		String toFirstNode = RequestUtil.getString(request, "toFirstNode");
		String directStart = RequestUtil.getString(request, "directStart");
		String sameExecutorJump = RequestUtil.getString(request, "sameExecutorJump");
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		bpmDefinition.setInstNameRule(instNameRule);
		bpmDefinition.setToFirstNode(toFirstNode);
		bpmDefinition.setDirectStart(directStart);
		bpmDefinition.setSameExecutorJump(sameExecutorJump);
		bpmDefinitionService.updateBpmDefinition(bpmDefinition);
	}
	
	
	/**
	 * 判断能否不填写表单直接启动
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getCanDirectStart")
	public @ResponseBody boolean getCanDirectStart(HttpServletRequest request) throws Exception {
		String defId = RequestUtil.getString(request, "defId");
		boolean rtn = this.bpmDefinitionService.getCanDirectStart(defId);
		return rtn;
	}
	
	/**
	 * 跳转至个人发起流程主页面
	 */
	@RequestMapping("myListMain")
	public ModelAndView myListMain(HttpServletRequest request){
		return new ModelAndView("/workflow/bpm/bpmDefMyListMain");
	}
	
	/**
	 * 跳转至个人发起流程列表页面
	 */
	@RequestMapping("toMylist")
	public ModelAndView toMylist(HttpServletRequest request){
		String typeId = StringUtils.getNullBlankStr(request.getParameter("typeId"));
		return new ModelAndView("/workflow/bpm/bpmDefMyList")
					.addObject("typeId", typeId);
	}
	
	/**
	 * 流程定义数据
	 */
	@RequestMapping("myList")
	public @ResponseBody GridData myList(HttpServletRequest request){
	  	String typeId = StringUtils.getNullBlankStr(request.getParameter("typeId"));
	  	String subject = StringUtils.getNullBlankStr(request.getParameter("subject"));
	  	String defKey = StringUtils.getNullBlankStr(request.getParameter("defKey"));
	  	String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("typeId", typeId);
	 	queryMap.put("subject", subject);
	 	queryMap.put("defKey", defKey.toUpperCase());
	 	queryMap.put("status", BpmDefinition.STATUS_DEPLOYED);
	 	queryMap.put("usable", CommonConst.YES);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
	 	queryMap.put("isMain", CommonConst.YES);
	 	List<BpmDefinition> result = bpmDefinitionService.findBpmDefinition(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 历史版本维护
	 * @param request
	 * @return
	 */
	@RequestMapping("toHisList")
	public ModelAndView toHisList(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		return new ModelAndView("/workflow/bpm/bpmDefHisList")
						.addObject("defId", defId);
	}
	/**
	 * 历史版本列表
	 * @param request
	 * @return
	 */
	@RequestMapping("hisList")
	public @ResponseBody GridData hisList(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("parentDefId", defId);
		queryMap.put("isMain", CommonConst.NO);
		List<BpmDefinition> result = bpmDefinitionService.findBpmDefinition(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 设置为主版本
	 * @param request
	 */
	@RequestMapping("setMain")
	public @ResponseBody void setMain(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		bpmDefinitionService.setMain(defId);
	}
	
	/**
	 * 校验流程Key
	 * @param request
	 * @return
	 */
	@RequestMapping("validateDefKey")
	public @ResponseBody boolean validateDefKey(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String defKey = StringUtils.getNullBlankStr(request.getParameter("defKey")).toUpperCase();
		BpmDefinition bpmDefinition = new BpmDefinition();
		bpmDefinition.setDefId(defId);
		bpmDefinition.setDefKey(defKey);
		int count =  bpmDefinitionService.countBpmDefinition(bpmDefinition);
		if(count>0){
			return false;
		}
		return true;
	}
	/**
	 * 启用|禁用
	 */
	@RequestMapping("updateUsable")
	public @ResponseBody void updateUsable(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String usable = StringUtils.getNullBlankStr(request.getParameter("usable"));
		if(StringUtils.isNotEmpty(ids)){
			bpmDefinitionService.updateUsable(ids.split(","), usable);
		}
	}
	
	@RequestMapping("toMyListSelector")
	public ModelAndView toMyListSelector(HttpServletRequest request){
		return new ModelAndView("/workflow/bpm/bpmDefMyListSelector");
	}
	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmDefinition assembleBpmDefinition(HttpServletRequest request){
		String defId = StringUtils.getNullBlankStr(request.getParameter("defId"));
		String typeId = StringUtils.getNullBlankStr(request.getParameter("typeId"));
		String subject = StringUtils.getNullBlankStr(request.getParameter("subject"));
		String defKey = StringUtils.getNullBlankStr(request.getParameter("defKey"));
		String description = StringUtils.getNullBlankStr(request.getParameter("description"));
		String defXml = StringUtils.getNullBlankStr(request.getParameter("designXml"));
		BpmDefinition bpmDefinition = null;
		if(StringUtils.isEmpty(defId)){
			bpmDefinition = new BpmDefinition();
		}else{
			bpmDefinition = bpmDefinitionService.getBpmDefinitionById(defId);
		}
		bpmDefinition.setTypeId(typeId);
		bpmDefinition.setSubject(subject);
		bpmDefinition.setDefKey(defKey.toUpperCase());
		bpmDefinition.setDescription(description);
		bpmDefinition.setDefXml(defXml);
		return bpmDefinition;
	}
}