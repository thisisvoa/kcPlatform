/**
 * @(#)com.casic27.jcxxgl.action.cdxxglAction.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-19
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.para.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casic27.platform.common.log.Logger;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.log.core.SqlContextHolder;
import com.casic27.platform.common.para.entity.Parameter;
import com.casic27.platform.common.para.service.ParaService;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 *@Author： 施阳钦(shiyangqin@casic27.com)
 *@Version：1.0
 */
@Controller
@RequestMapping("/para")
public class ParaController extends BaseMultiActionController{
	@Autowired
	private ParaService paraService;
	public ParaService getParaService() {
		return paraService;
	}
	public void setParaService(ParaService paraService) {
		this.paraService = paraService;
	}

	@RequestMapping("para_main")
	public String paraMain(Model model, HttpServletRequest request) {
		return "common/para/paraMain";
	}
	
	@RequestMapping("toParaList")
	public String toParaList(Model model, HttpServletRequest request) {
		String res = request.getParameter("res");
		model.addAttribute("res", res);
		return "common/para/paraList";
	}
	
	@RequestMapping("paraList")
	public @ResponseBody GridData paraList(HttpServletRequest request){
		String csmc = StringUtils.escapeSqlLike(request.getParameter("csmc"));
		String csdm = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("csdm")));
		String useFlag = StringUtils.escapeSqlLike(request.getParameter("useFlag"));
		String res = request.getParameter("res");
		Parameter paraMap = new Parameter();
		paraMap.setJlyxzt("1");
		paraMap.setSybz(res);
		paraMap.setCsmc(csmc);
		paraMap.setCsdm(csdm);
		paraMap.setSybz(useFlag);
		List<Parameter> resultList = paraService.queryParaList(paraMap);
		Page page = PageContextHolder.getPage();
		return new GridData(resultList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	@RequestMapping("toParaAdd")
	public String toParaAdd(Model model, HttpServletRequest request) {
		Parameter paraMap = new Parameter();
		paraMap.setSybz("1");
		model.addAttribute("para", paraMap);
		model.addAttribute("type", "ADD");
		return "common/para/paraDetail";
	}
	
	@RequestMapping("toParaEdit")
	public String toParaEdit(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		Parameter para = paraService.getParaById(zjId);
		model.addAttribute("para", para);
		model.addAttribute("type", "UPDATE");
		return "common/para/paraDetail";
	}
	
	@RequestMapping("paraView")
	public String paraView(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		Parameter para = paraService.getParaById(zjId);
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "参数信息管理", "[查看] 查看[参数名称："+para.getCsmc()+"] [参数代码："+para.getCsdm()+"] [参数值："+para.getCsz()+"]的参数信息");
		model.addAttribute("para", para);
		return "common/para/paraView";
	}
	
	@RequestMapping("addPara")
	public @ResponseBody void addPara(HttpServletRequest request){
		Parameter paraMap = new Parameter();
		setMap(paraMap, request);
		paraMap.setJlxzsj(DateUtils.getCurrentDateTime14());
		paraMap.setJlxgsj(DateUtils.getCurrentDateTime14());
		paraMap.setJlyxzt("1");
		paraService.insert(paraMap);
	}
	
	@RequestMapping("updatePara")
	public @ResponseBody void updatePara(HttpServletRequest request){
		Parameter paraMap = new Parameter();
		setMap(paraMap, request);
		paraMap.setZjId(request.getParameter("zjId"));
		setMap(paraMap, request);
		paraMap.setJlxgsj(DateUtils.getCurrentDateTime14());
		paraService.update(paraMap);
	}
	
	@RequestMapping("updateParaSybz")
	public @ResponseBody int updateParaSybz(HttpServletRequest request){
		int succFlag = 1;
		try{
			String zjIds = request.getParameter("zjIds");
			String sybz = request.getParameter("sybz");
			if(zjIds != null && !"".equals(zjIds.trim())){
				String[] zjidArr = zjIds.split(",");
				for(int i = 0, len = zjidArr.length; i < len; i++){
					Parameter param = paraService.getParaById(zjidArr[i]);
					paraService.updateSybz(param, sybz);
				}
			}
		}catch(Exception e){
			succFlag = 0;
		}
		
		return succFlag;
	}
	
	@RequestMapping("deleteParams")
	public @ResponseBody int deleteParams(HttpServletRequest request){
		int succFlag = 1;
		try{
			String ids = request.getParameter("ids");
			if(StringUtils.isNotEmpty(ids)){
				String[] idArr = ids.split(",");
				for (String zjId:idArr) {
					Parameter paraMap = paraService.getParaById(zjId);
					paraMap.setJlscsj(DateUtils.getCurrentDateTime14());
					paraService.logicDelParam(paraMap);
				}
			}
		}catch(Exception e){
			succFlag = 0;
		}
		return succFlag;
	}

	/**
	 * 设定 Map 内容
	 * 
	 * @param paraMap
	 * @param request
	 */
	private void setMap(Parameter paraMap, HttpServletRequest request) {
		paraMap.setCsmc(StringUtils.getNullBlankStr(request.getParameter("csmc")));
		paraMap.setCsdm(StringUtils.getNullBlankStr(request.getParameter("csdm")));
		paraMap.setCsz(StringUtils.getNullBlankStr(request.getParameter("csz")));
		paraMap.setSybz(StringUtils.getNullBlankStr(request.getParameter("sybz")));
		paraMap.setBz(StringUtils.getNullBlankStr(request.getParameter("bz")));
	}
	/**
	 * 校验唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("checkCsdm")
	public @ResponseBody Object[] checkCsdm(Model model, HttpServletRequest request){
		String zjId = request.getParameter("zjId");
		String csdm = request.getParameter("fieldValue");
		String fieldId = request.getParameter("fieldId");
		Parameter param = new Parameter();
		param.setZjId(zjId);
		param.setCsdm(csdm);
		boolean isExist = paraService.isCsdmExist(param);
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = !isExist;
		return result;
	}
}
