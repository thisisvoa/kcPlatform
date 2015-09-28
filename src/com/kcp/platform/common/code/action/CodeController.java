package com.kcp.platform.common.code.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.common.code.entity.Code;
import com.kcp.platform.common.code.service.CodeService;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;

/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
@Controller
@RequestMapping("codeCtl")
public class CodeController extends BaseMultiActionController{
	
	@Autowired
	private CodeService codeService;

	
	/**
	 * 代码管理界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toCodeMain")
	public String toCodeMain(Model model,HttpServletRequest request){		
		return "common/code/codeMain";
	}
	
	/**
	 * 代码树
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toCodeTree")
	public String toCodeTree(Model model,HttpServletRequest request){		
		return "common/code/codeTree";
	}
	
	/**
	 * 代码列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toCodeList")
	public String toCodeList(Model model,HttpServletRequest request){	
		String codeId = StringUtils.getNullBlankStr(request.getParameter("codeId"));		
		if(!codeId.isEmpty()){
			Code code = codeService.getCodeById(codeId);
			if(code != null){			
				if(code.getSfdmx().equals(CommonConst.YES_FLAG)){  //点击代码选项时进入代码选项编辑界面
					model.addAttribute("codeInfo", code );
					return "common/code/codeEdit";
				}else{
					model.addAttribute("codeInfo",code);
					return "common/code/codeOptionList";//点击代码时进入该代码的代码选项列表界面
				}
			}
		}
		return "common/code/codeValidList";//代码列表	
	}
	
	/**
	 * 代码项列表
	 * @param request
	 * @return
	 */
	@RequestMapping("codeOptionList")
	public @ResponseBody GridData codeOptionList(HttpServletRequest request){
		String codeName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("codeName")));
		String codeItemCode = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("codeItemCode")));
		String codeSimpleName = StringUtils.getNullBlankStr(request.getParameter("codeSimpleName"));
		String useFlag = request.getParameter("useFlag");
		
		Code codeOption = new Code();
		codeOption.setJlyxzt(CommonConst.ENABLE_FLAG);
		codeOption.setSfdmx(CommonConst.YES_FLAG);
		codeOption.setDmx_mc(codeName);
		codeOption.setDmjc(codeSimpleName);
		codeOption.setDmx_bh(codeItemCode);
		codeOption.setSybz(useFlag);
		List<Code> codeList = codeService.queryCodeListByCode(codeOption);
		Page page = PageContextHolder.getPage();
		return new GridData(codeList, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	/**
	 * 参数列表
	 * @param request
	 * @return
	 */
	@RequestMapping("codeValidList")
	public @ResponseBody GridData codeValidList(HttpServletRequest request){
		String codeName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("codeName")));
		String codeSimpleName = StringUtils.escapeSqlLike(StringUtils.getNullBlankStr(request.getParameter("codeSimpleName")));
		String type = request.getParameter("type");
		String useFlag = request.getParameter("useFlag");
		List<Code> codeList = null;
		if(!"query".equals(type)){
			Code code = assembleParaEntity(request);
			code.setDmjc(codeSimpleName);
			code.setSfdmx(CommonConst.NO_FLAG);
			code.setJlyxzt(CommonConst.ENABLE_FLAG);
			codeList = codeService.queryCodeListByCode(code);
		}
		if("query".equals(type)){
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("dmmc", codeName);
			paramMap.put("dmjc", codeSimpleName);
			paramMap.put("sybz", useFlag);
			paramMap.put("sfdmx", "0");
			codeList = codeService.queryCodeListByParamMap(paramMap);
		}
		Page page = PageContextHolder.getPage();
		GridData result = new GridData(codeList, page.getPage(), page.getTotalPages(), page.getTotalItems());
		return result;
	}
	/**
	 * 代码可用列表
	 * @param model
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("toCodeValidList")
	public String toCodeValidList(Model model,HttpServletRequest request) throws UnsupportedEncodingException{
		String codeName = StringUtils.getNullBlankStr(request.getParameter("codeName"));
		String codeSimpleName = StringUtils.getNullBlankStr(request.getParameter("codeSimpleName"));
		Code code = assembleParaEntity(request);
		code.setDmmc(codeName);
		code.setDmjc(codeSimpleName);
		code.setSfdmx(CommonConst.NO_FLAG);
		code.setJlyxzt(CommonConst.ENABLE_FLAG);
		model.addAttribute("codeInfo", code );
		return "common/code/codeValidList";			
	}
	
	/**
	 * 代码新增界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddCode")
	public String toAddCode(Model model, HttpServletRequest request){
		Code code = assembleParaEntity(request);
		if(code.getSfdmx().equals(CommonConst.YES_FLAG)){
			String pCodeId = StringUtils.getNullBlankStr(request.getParameter("pCodeId")); 
			Code pCode = codeService.getCodeById(pCodeId);
			code.setDmmc(pCode.getDmmc());
			code.setDmjc(pCode.getDmjc());
			
			Code pxhCode = new Code();
			pxhCode.setSfdmx(code.getSfdmx());
			pxhCode.setDmjc(pCode.getDmjc());
			String maxPxh = codeService.getCodeMaxPxh(pxhCode);
			if(StringUtils.isNullBlank(maxPxh)){
				maxPxh="1";
			}
			code.setPxh(maxPxh);
		}
		if(code.getSfdmx().equals(CommonConst.NO_FLAG)){
			Code pCode = new Code();
			pCode.setSfdmx(code.getSfdmx());
			String maxPxh = codeService.getCodeMaxPxh(pCode);
			if(StringUtils.isNullBlank(maxPxh)){
				maxPxh="1";
			}
			code.setPxh(maxPxh);
		}
		model.addAttribute("codeInfo", code);
		model.addAttribute("isDialog", request.getParameter("isDialog"));
		model.addAttribute("operModel", CommonConst.ADD_OPERMODEL );
		
		return "common/code/codeEdit";
	}
	
	/**
	 * 代码编辑界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toEditCode")
	public String toEditCode(Model model, HttpServletRequest request){
		String codeId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Code codeInfo = codeService.getCodeById(codeId);
		if(codeInfo.getSfdmx().equals(CommonConst.NO_FLAG)){
			Code codeOption = new Code();
			codeOption.setDmmc(codeInfo.getDmmc());
			codeOption.setDmjc(codeInfo.getDmjc());
			codeOption.setJlyxzt(CommonConst.ENABLE_FLAG);
			codeOption.setSfdmx(CommonConst.YES_FLAG);
			List<Code> codeList = codeService.queryCodeListByCode(codeOption);
			model.addAttribute("codeList", codeList);
		}
		model.addAttribute("codeInfo", codeInfo);
		model.addAttribute("isDialog", request.getParameter("isDialog"));
		model.addAttribute("operModel", CommonConst.UPDATE_OPERMODEL );
		return "common/code/codeEdit";
		
	}
	
	/**
	 * 代码详细信息查看界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("toViewCode")
	public String toViewCode(Model model, HttpServletRequest request){
		String codeId = StringUtils.getNullBlankStr(request.getParameter("id"));
		Code codeInfo = codeService.getCodeById(codeId);
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.QUERY.value(), sql, "代码信息管理", "[查看] 查看[代码名称："+codeInfo.getDmmc()+"] [代码简称："+codeInfo.getDmjc()+"] [代码项名称："+codeInfo.getDmx_mc()+"] [代码项编号："+codeInfo.getDmx_bh()+"]的代码信息");
		model.addAttribute("codeInfo", codeInfo);
		model.addAttribute("operModel", CommonConst.VIEW_OPERMODEL);
		return "common/code/codeView";
		
	}
	
	
	/**
	 * 禁用代码
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("forbidCode")
	public @ResponseBody int forbidCode(HttpServletRequest request){
		int succFlag = 1;
		try {
			String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
			String[] idArray = null;
			if(StringUtils.isNotEmpty(ids)){
				idArray = ids.split(",");
				for(String id : idArray){
					Code code =codeService.getCodeById(id);
					if(code!=null){
						codeService.updateSybz(code,CommonConst.DISABLE_FLAG);
					}
				}
			}
		} catch (Exception e) {
			succFlag = 0;
			throw new BusinessException("禁用编码项失败!");
		}
		
		return succFlag;
	}
	
	/**
	 * 启用代码
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("enabledCode")
	public @ResponseBody int enabledCode(Model model, HttpServletRequest request){
		int succFlag = 1;
		try {
			String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
			String[] idArray = null;
			if(StringUtils.isNotEmpty(ids)){
				idArray = ids.split(",");
				for(String id : idArray){
					Code code =codeService.getCodeById(id);
					if(code!=null){
						codeService.updateSybz(code, CommonConst.YES);
					}
					
				}
			}
		} catch (Exception e) {
			succFlag = 0;
			throw new BusinessException("启用编码项失败!");
		}
		
		return succFlag;
	}
		
	/**
	 * 逻辑删除代码
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("logicDelCode")
	public @ResponseBody void logicDelCode(Model model, HttpServletRequest request){
		try {
			String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
			String[] idArray = null;
			if(StringUtils.isNotEmpty(ids)){
				idArray = ids.split(",");
				for(String id : idArray){
					Code code =codeService.getCodeById(id);
					if(code!=null){
						codeService.logicDel(code);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException("删除编码失败!", e);
		}
	}
	/**
	 * 提交代码信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("submitCodeInfo")
	public @ResponseBody void submitCodeInfo(Model model, HttpServletRequest request){
		String operModel = StringUtils.getNullBlankStr(request.getParameter("operModel"));
		if("ADD".equalsIgnoreCase(operModel)){
			addCodeInfo(model, request);
		}else{
			updateCodeInfo(model, request);
		}		
	}
	
	/**
	 * 新增代码信息
	 * @param model
	 * @param request
	 * @return
	 */
	public @ResponseBody void addCodeInfo(Model model, HttpServletRequest request){
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User)session.getAttribute(CommonConst.SESSION_USER_KEY);
		String userId = user.getZjid();
		Code code = assembleParaEntity(request);
		code.setJlxzsj(DateUtils.getCurrentDateTime14());
		code.setJlxgsj(DateUtils.getCurrentDateTime14());
		code.setCjyh(userId);
		code.setJlyxzt(CommonConst.ENABLE_FLAG);
		code = codeService.insertCode(code);
		boolean isAddSuccess = code.getZjid() != null && !code.getZjid().isEmpty() ? true : false;
		if(!isAddSuccess){
			throw new BusinessException(CommonConst.OPER_FAIL);
		}
	}
	
	/**
	 * 修改代码信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("updateCodeInfo")
	public @ResponseBody void updateCodeInfo(Model model, HttpServletRequest request){
		try {
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User)session.getAttribute(CommonConst.SESSION_USER_KEY);
			String userId = user.getZjid();
			Code code = assembleParaEntity(request);
			code.setJlxgsj(DateUtils.getCurrentDateTime14());
			code.setGxyh(userId);
			code = codeService.updateCode(code);
		} catch (Exception e) {
			throw new BusinessException(CommonConst.OPER_FAIL);
		}
	}
	
	/**
	 * 获取代码项列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getCodeItemList")
	public @ResponseBody List<Code> getCodeItemList(HttpServletRequest request){
		String dmjc = StringUtils.getNullBlankStr(request.getParameter("dmjc"));
		if(StringUtils.isEmpty(dmjc)){
			return null;
		}
		return codeService.getCodeItemList(dmjc);
	}
	
	@RequestMapping("getCodeList")
	public @ResponseBody List<Code> getCodeList(HttpServletRequest request){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("sfdmx", CommonConst.NO);
		queryMap.put("sybz", CommonConst.YES);
		queryMap.put("jlyxzt", CommonConst.YES);
		return codeService.findCodeList(queryMap);
	}
	
	/**
	 * 异步代码树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/asyncCodeTree")
	public @ResponseBody List<Map> asyncCodeTree(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		if(StringUtils.isEmpty(id)){
			Code code =  assembleParaEntity(request);
		    code.setSfdmx(CommonConst.NO_FLAG);//非代码项
		    code.setJlyxzt(CommonConst.ENABLE_FLAG);
			List<Code> codeList = codeService.queryCodeListByCode(code);
			List<Map> result = new ArrayList();
			Map root = new HashMap();
			root.put("id", "0");
			root.put("name", "编码树");
			root.put("isParent", true);
			result.add(root);
			if(null!=codeList){
				Iterator<Code> it = codeList.iterator();
				while(it.hasNext()){
					Code c = it.next();
					Map map = new HashMap();
					map.put("id", c.getZjid());
					map.put("name", c.getDmmc());
					map.put("pId", "0");
					map.put("isParent", true);
					map.put("sfdmx", c.getSfdmx());
					result.add(map);
				}
			}
			return result;
		}else{
			Code code = codeService.getCodeById(id);
			if(code != null){
				if(code.getSfdmx().equals(CommonConst.NO_FLAG)){
					Code codeOption = new Code();
					codeOption.setSfdmx(CommonConst.YES_FLAG);
					//codeOption.setDmmc(code.getDmmc());
					codeOption.setDmjc(code.getDmjc());
					codeOption.setJlyxzt(CommonConst.ENABLE_FLAG);
					List<Code> codeList = codeService.queryCodeListByCode(codeOption);
					List<Map> result = new ArrayList();
					if(null!=codeList){
						Iterator<Code> it = codeList.iterator();
						while(it.hasNext()){
							Code c = it.next();
							Map map = new HashMap();
							map.put("id", c.getZjid());
							map.put("name", c.getDmx_mc());
							map.put("pId", code.getZjid());
							map.put("sfdmx", c.getSfdmx());
							result.add(map);
						}
					}
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * 校验代码简称唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("/checkDmjcUnique")
	public @ResponseBody Object[] checkDmjcUnique(Model model, HttpServletRequest request){
		String dmjc = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		String zjid = StringUtils.getNullBlankStr(request.getParameter("zjid"));
		Code map = new Code();
		boolean isUnique = true;
		map.setDmjc(dmjc);
		map.setZjid(zjid);
		map.setSfdmx(CommonConst.NO);
		int count = codeService.staticCode(map);
		if (count > 0) {
			isUnique=false;
		}
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = isUnique;
		return result;
		}
	
	/**
	 * 校验代码项编号唯一性
	 * @param model
	 * @param request
	 */
	@RequestMapping("/checkDmxBhUnique")
	public @ResponseBody Object[] checkDmxBhUnique(Model model, HttpServletRequest request){
		String dmxBh = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		String dmjc = StringUtils.getNullBlankStr(request.getParameter("codeSimpleName"));
		String zjid = StringUtils.getNullBlankStr(request.getParameter("zjid"));
		Code map = new Code();
		boolean isUnique = true;
		map.setDmx_bh(dmxBh);
		map.setDmjc(dmjc);
		map.setZjid(zjid);
		map.setSfdmx(CommonConst.YES);
		int count = codeService.staticCode(map);
		if (count > 0) {
			isUnique=false;
		}
		Object[] result = new Object[2];
		result[0] = fieldId;
		result[1] = isUnique;
		return result;
	}
	
	
	/**
	 * 异步代码项树(只有点击代码时才异步加载所属的代码项树)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	/**
	 * 组装参数为map类型
	 * @param request
	 * @return
	 */
	public Map<String, Object> assembleParaMap(HttpServletRequest request){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String codeId = StringUtils.getNullBlankStr(request.getParameter("codeId"));
		String codeName = StringUtils.getNullBlankStr(request.getParameter("codeName"));
		String codeSimpleName = StringUtils.getNullBlankStr(request.getParameter("codeSimpleName"));
		String isCodeOption = StringUtils.getNullBlankStr(request.getParameter("isCodeOption"));		
		String codeOptionNum = StringUtils.getNullBlankStr(request.getParameter("codeOptionNum"));
		String codeOptionName = StringUtils.getNullBlankStr(request.getParameter("codeOptionName"));
		String orderNo = StringUtils.getNullBlankStr(request.getParameter("orderNo"));		
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));		
		String creator = StringUtils.getNullBlankStr(request.getParameter("creator"));
		String updator= StringUtils.getNullBlankStr(request.getParameter("updator"));
		String createTime = StringUtils.getNullBlankStr(request.getParameter("createTime"));
		String updateTime = StringUtils.getNullBlankStr(request.getParameter("updateTime"));
		String deleteTime = StringUtils.getNullBlankStr(request.getParameter("deleteTime"));
		String enableTarg = StringUtils.getNullBlankStr(request.getParameter("enableTarg"));
		if(StringUtils.isNotEmpty(codeId)){
			paramMap.put("zjid", codeId);
		}
		if(StringUtils.isNotEmpty(codeName)){
			paramMap.put("dmmc", codeName);
		}
		if(StringUtils.isNotEmpty(codeSimpleName)){
			paramMap.put("dmjc", codeSimpleName);
		}
		if(StringUtils.isNotEmpty(isCodeOption)){
			paramMap.put("sfdmx", isCodeOption);
		}	
		if(StringUtils.isNotEmpty(codeOptionNum)){
			paramMap.put("dmx_bh", codeOptionNum);
		}
		if(StringUtils.isNotEmpty(codeOptionName)){
			paramMap.put("dmx_mc", codeOptionName);
		}
		if(StringUtils.isNotEmpty(orderNo)){
			paramMap.put("pxh", orderNo);
		}	
		if(StringUtils.isNotEmpty(useTarg)){
			paramMap.put("sybz", useTarg);
		}
		if(StringUtils.isNotEmpty(remark)){
			paramMap.put("bz", remark);
		}
		if(StringUtils.isNotEmpty(creator)){
			paramMap.put("cjyh", creator);
		}
		if(StringUtils.isNotEmpty(updator)){
			paramMap.put("gxyh", updator);
		}
		if(StringUtils.isNotEmpty(createTime)){
			paramMap.put("jlxzsj", createTime);
		}
		if(StringUtils.isNotEmpty(updateTime)){
			paramMap.put("jlxgsj", updateTime);
		}
		if(StringUtils.isNotEmpty(deleteTime)){
			paramMap.put("jlscsj", deleteTime);
		}
		if(StringUtils.isNotEmpty(enableTarg)){
			paramMap.put("jlyxzt", enableTarg);
		}
		return paramMap;		
	}
	
	/**
	 * 组装参数为实体类型
	 * @param request
	 * @return
	 */
	public Code assembleParaEntity(HttpServletRequest request){
		Code code = new Code();
		String codeId = StringUtils.getNullBlankStr(request.getParameter("codeId"));
		String codeName = StringUtils.getNullBlankStr(request.getParameter("codeName"));
		String codeSimpleName = StringUtils.getNullBlankStr(request.getParameter("codeSimpleName"));
		String isCodeOption = StringUtils.getNullBlankStr(request.getParameter("isCodeOption"));		
		String codeOptionNum = StringUtils.getNullBlankStr(request.getParameter("codeOptionNum"));
		String codeOptionName = StringUtils.getNullBlankStr(request.getParameter("codeOptionName"));
		String orderNo = StringUtils.getNullBlankStr(request.getParameter("orderNo"));		
		String useTarg = StringUtils.getNullBlankStr(request.getParameter("useTarg"));
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));		
		String creator = StringUtils.getNullBlankStr(request.getParameter("creator"));
		String updator= StringUtils.getNullBlankStr(request.getParameter("updator"));
		String createTime = StringUtils.getNullBlankStr(request.getParameter("createTime"));
		String updateTime = StringUtils.getNullBlankStr(request.getParameter("updateTime"));
		String deleteTime = StringUtils.getNullBlankStr(request.getParameter("deleteTime"));
		String enableTarg = StringUtils.getNullBlankStr(request.getParameter("enableTarg"));	
		
		System.out.println(useTarg);
		
		if(StringUtils.isNotEmpty(codeId)){
			code.setZjid(codeId);
		}
		if(StringUtils.isNotEmpty(codeName)){
			code.setDmmc(codeName);
		}
		if(StringUtils.isNotEmpty(codeSimpleName)){
			code.setDmjc(codeSimpleName);
		}
		if(StringUtils.isNotEmpty(isCodeOption)){
			code.setSfdmx(isCodeOption);
		}
		
		if(StringUtils.isNotEmpty(codeOptionNum)){
			code.setDmx_bh(codeOptionNum);
		}
		if(StringUtils.isNotEmpty(codeOptionName)){
			code.setDmx_mc(codeOptionName);
		}
		if(StringUtils.isNotEmpty(orderNo)){
			code.setPxh(orderNo);
		}
		if(StringUtils.isNotEmpty(useTarg)){
			code.setSybz(useTarg);
		}
		if(StringUtils.isNotEmpty(remark)){
			code.setBz(remark);
		}
		if(StringUtils.isNotEmpty(creator)){
			code.setCjyh(creator);
		}
		if(StringUtils.isNotEmpty(updator)){
			code.setGxyh(updator);
		}
		if(StringUtils.isNotEmpty(createTime)){
			code.setJlxzsj(createTime);
		}
		if(StringUtils.isNotEmpty(updateTime)){
			code.setJlxgsj(updateTime);
		}
		if(StringUtils.isNotEmpty(deleteTime)){
			code.setJlscsj(deleteTime);
		}
		if(StringUtils.isNotEmpty(enableTarg)){
			code.setJlyxzt(enableTarg);
		}
		return code;
	}
	/**
	 * 编码转换
	 * @param str
	 * @return
	 */
	private String getURLDecode(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		try {
			return java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
