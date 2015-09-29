package com.kcp.platform.common.para.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.para.entity.SysParameter;
import com.kcp.platform.common.para.service.ParaService;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
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
		SysParameter paraMap = new SysParameter();
		paraMap.setStatus("1");
		paraMap.setParmName(csmc);
		paraMap.setParmCode(csdm);
		paraMap.setIsUsed(useFlag);
		List<SysParameter> resultList = paraService.queryParaList(paraMap);
		Page page = PageContextHolder.getPage();
		return new GridData(resultList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	@RequestMapping("toParaAdd")
	public String toParaAdd(Model model, HttpServletRequest request) {
		SysParameter paraMap = new SysParameter();
		paraMap.setIsUsed("1");
		model.addAttribute("para", paraMap);
		model.addAttribute("type", "ADD");
		return "common/para/paraDetail";
	}
	
	@RequestMapping("toParaEdit")
	public String toParaEdit(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		SysParameter para = paraService.getParaById(zjId);
		model.addAttribute("para", para);
		model.addAttribute("type", "UPDATE");
		return "common/para/paraDetail";
	}
	
	@RequestMapping("paraView")
	public String paraView(Model model, HttpServletRequest request) {
		String zjId = request.getParameter("zjId");
		SysParameter para = paraService.getParaById(zjId);
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "参数信息管理", "[查看] 查看[参数名称："+para.getParmName()+"] [参数代码："+para.getParmCode()+"] [参数值："+para.getParmValue()+"]的参数信息");
		model.addAttribute("para", para);
		return "common/para/paraView";
	}
	
	@RequestMapping("addPara")
	public @ResponseBody void addPara(HttpServletRequest request){
		SysParameter paraMap = new SysParameter();
		setMap(paraMap, request);
		paraMap.setCreateTime(DateUtils.getCurrentDateTime());
		paraMap.setUpdateTime(DateUtils.getCurrentDateTime());
		paraMap.setStatus("1");
		paraService.insert(paraMap);
	}
	
	@RequestMapping("updatePara")
	public @ResponseBody void updatePara(HttpServletRequest request){
		SysParameter paraMap = new SysParameter();
		setMap(paraMap, request);
		paraMap.setId(request.getParameter("zjId"));
		setMap(paraMap, request);
		paraMap.setCreateTime(DateUtils.getCurrentDateTime());
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
					SysParameter param = paraService.getParaById(zjidArr[i]);
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
					SysParameter paraMap = paraService.getParaById(zjId);
					paraMap.setDeleteTime(DateUtils.getCurrentDateTime());
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
	private void setMap(SysParameter paraMap, HttpServletRequest request) {
		paraMap.setParmName(StringUtils.getNullBlankStr(request.getParameter("csmc")));
		paraMap.setParmCode(StringUtils.getNullBlankStr(request.getParameter("csdm")));
		paraMap.setParmValue(StringUtils.getNullBlankStr(request.getParameter("csz")));
		paraMap.setIsUsed(StringUtils.getNullBlankStr(request.getParameter("sybz")));
		paraMap.setMemo(StringUtils.getNullBlankStr(request.getParameter("bz")));
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
		SysParameter param = new SysParameter();
		param.setId(zjId);
		param.setParmCode(csdm);
		boolean isExist = paraService.isCsdmExist(param);
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = !isExist;
		return result;
	}
}
