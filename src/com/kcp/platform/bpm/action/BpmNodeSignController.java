package com.kcp.platform.bpm.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmNodePrivilege;
import com.kcp.platform.bpm.entity.BpmNodeSign;
import com.kcp.platform.bpm.service.BpmNodeSignService;

@Controller
@RequestMapping("workflow/bpmDef/nodeSign")
public class BpmNodeSignController extends BaseMultiActionController{
	@Autowired
	private BpmNodeSignService nodeSignService;
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("edit")
	public ModelAndView edit(HttpServletRequest request){
		String actDefId = StringUtils.getNullBlankStr(request.getParameter("actDefId"));
		String nodeId = StringUtils.getNullBlankStr(request.getParameter("nodeId"));
		BpmNodeSign nodeSign = nodeSignService.getNodeSignByActDefId(actDefId, nodeId);
		if(nodeSign==null){
			nodeSign = new BpmNodeSign();
			nodeSign.setActDefId(actDefId);
			nodeSign.setNodeId(nodeId);
		}
		List<String> modeList = Arrays.asList("所有特权,允许直接处理,允许一票制,允许补签".split(","));
		List<BpmNodePrivilege> bpmNodePrivileges = this.nodeSignService.getPrivilegesByDefIdAndNodeId(actDefId, nodeId);
		BpmNodePrivilege[] bpmNodePrivilegeList = new BpmNodePrivilege[modeList.size()];
		if (bpmNodePrivileges != null) {
			for (int i = 0; i < bpmNodePrivileges.size(); i++) {
				BpmNodePrivilege vo = (BpmNodePrivilege) bpmNodePrivileges.get(i);
				if (vo.getPrivilegeMode() != null) {
					bpmNodePrivilegeList[vo.getPrivilegeMode().intValue()] = vo;
				}
			}
		}
		return new ModelAndView("workflow/bpm/nodeSignEdit")
					.addObject("nodeSign", nodeSign)
					.addObject("modeList", modeList)
					.addObject("bpmNodePrivilegeList", bpmNodePrivilegeList);
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("save")
	public @ResponseBody void saveNodeSign(HttpServletRequest request){
		BpmNodeSign nodeSign = assembleNodeSign(request);
		List<BpmNodePrivilege> privileges = new ArrayList<BpmNodePrivilege>();
		String[] usertypes = request.getParameterValues("userType");
		String[] cmpIds = request.getParameterValues("cmpIds");
		String[] cmpNames = request.getParameterValues("cmpNames");
		for (int i = 0; i < usertypes.length; i++) {
			String usertype = usertypes[i];
			String cmpId = cmpIds[i];
			String cmpName = cmpNames[i];
			if ((StringUtils.isNotEmpty(usertype))) {
				Short uType = Short.valueOf(usertype);
				switch (uType) {
				case 1:
					break;
				case 6:
					if (StringUtils.isEmpty(cmpName)) continue;
					break;
				case 2:
				case 3:
				case 4:
				case 5:
					if (StringUtils.isEmpty(cmpId)) continue;
					break;
				}
				BpmNodePrivilege vo = new BpmNodePrivilege();
				vo.setPrivilegeMode((short) i);
				vo.setUserType(uType);
				vo.setCmpIds(cmpId);
				vo.setCmpNames(cmpName);
				privileges.add(vo);
			}
		}
		nodeSignService.saveNodeSign(nodeSign, privileges);
	}
	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmNodeSign assembleNodeSign(HttpServletRequest request){
		String signId = RequestUtil.getString(request, "signId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		int voteAmount = RequestUtil.getInt(request, "voteAmount");
		Short decideType = RequestUtil.getShort(request, "decideType");
		Short voteType = RequestUtil.getShort(request, "voteType");
		Short flowMode = RequestUtil.getShort(request, "flowMode");
		BpmNodeSign nodeSign = new BpmNodeSign();
		nodeSign.setSignId(signId);
		nodeSign.setActDefId(actDefId);
		nodeSign.setNodeId(nodeId);
		nodeSign.setVoteAmount(voteAmount);
		nodeSign.setDecideType(decideType);
		nodeSign.setVoteType(voteType);
		nodeSign.setFlowMode(flowMode);
		return nodeSign;
	}
}