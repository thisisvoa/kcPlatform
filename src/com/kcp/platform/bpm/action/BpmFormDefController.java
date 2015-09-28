package com.kcp.platform.bpm.action;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.engine.FreemarkEngine;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmFormDef;
import com.kcp.platform.bpm.entity.BpmFormField;
import com.kcp.platform.bpm.entity.BpmFormTable;
import com.kcp.platform.bpm.entity.BpmFormTemplate;
import com.kcp.platform.bpm.service.BpmFormDefService;
import com.kcp.platform.bpm.service.BpmFormFieldService;
import com.kcp.platform.bpm.service.BpmFormTableService;
import com.kcp.platform.bpm.service.BpmFormTemplateService;

@Controller
@RequestMapping("workflow/formDef")
public class BpmFormDefController extends BaseMultiActionController{
	@Autowired
	private BpmFormDefService bpmFormDefService;
	
	@Autowired
	private BpmFormTemplateService bpmFormTemplateService;
	
	@Autowired
	private BpmFormTableService bpmFormTableService;
	
	@Autowired
	private BpmFormFieldService bpmFormFieldService;
	
	@Autowired
	private FreemarkEngine freemarkEngine;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormDefMain");
	}
	
	
	@RequestMapping("/toList")
	public ModelAndView toList(HttpServletRequest request){
		String catalogId = StringUtils.getNullBlankStr(request.getParameter("catalogId"));
		return new ModelAndView("workflow/form/bpmFormDefList")
						.addObject("catalogId", catalogId);
	}
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmFormDefList(HttpServletRequest request){
	  	String catalogId = StringUtils.getNullBlankStr(request.getParameter("catalogId"));
	  	String formKey = StringUtils.getNullBlankStr(request.getParameter("formKey"));
	  	String subject = StringUtils.getNullBlankStr(request.getParameter("subject"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("catalogId", catalogId);
	 	queryMap.put("formKey", formKey);
	 	queryMap.put("subject", subject);
		List<BpmFormDef> result = bpmFormDefService.findBpmFormDefPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 新增设置页面
	 * @param request
	 * @return
	 */
	@RequestMapping("toSet")
	public ModelAndView toSet(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormDefSet");
	}
	
	/**
	 * 选择模板
	 * @param request
	 * @return
	 */
	@RequestMapping("selectTmpl")
	public ModelAndView selectTmpl(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormDefTmplChooser");
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request)throws Exception{
		String catalogId = StringUtils.getNullBlankStr(request.getParameter("catalogId"));
		String formKey = URLDecoder.decode(StringUtils.getNullBlankStr(request.getParameter("formKey")),"utf-8");;
		String subject = URLDecoder.decode(StringUtils.getNullBlankStr(request.getParameter("subject")),"utf-8");
		String formDesc = URLDecoder.decode(StringUtils.getNullBlankStr(request.getParameter("formDesc")),"utf-8");
		String tableId = StringUtils.getNullBlankStr(request.getParameter("tableId"));
		String tableDesc = URLDecoder.decode(StringUtils.getNullBlankStr(request.getParameter("tableDesc")),"utf-8");
		String mainTmplId = StringUtils.getNullBlankStr(request.getParameter("mainTmplId"));
		BpmFormDef bpmFormDef = new BpmFormDef();
		bpmFormDef.setCatalogId(catalogId);
		bpmFormDef.setFormKey(formKey);
		bpmFormDef.setSubject(subject);
		bpmFormDef.setFormDesc(formDesc);
		bpmFormDef.setTableId(tableId);
		bpmFormDef.setTableDesc(tableDesc);
		String reult = genTemplate(tableId, mainTmplId);
	    bpmFormDef.setHtml(reult);
		return new ModelAndView("workflow/form/bpmFormDefEdit")
					.addObject("bpmFormDef", bpmFormDef)
					.addObject("type","add");
	}
	private String genTemplate(String tableId, String tmplId)throws Exception{
		BpmFormTable bpmFormTable = bpmFormTableService.getBpmFormTableById(tableId);
		List<BpmFormField> fields = this.bpmFormFieldService.getBpmFormFieldByTableId(tableId);
		for (BpmFormField field : fields){
			field.setFieldName(new StringBuilder().append(bpmFormTable.getIsMain().equals("1") ? "m:" : "s:").append(bpmFormTable.getTableName()).append(":").append(field.getFieldName()).toString());
	    }
		Map<String,Object> fieldsMap = new HashMap<String,Object>();
		fieldsMap.put("table", bpmFormTable);
	    fieldsMap.put("fields", fields);
		BpmFormTemplate mainTemplate = bpmFormTemplateService.getBpmFormTemplateById(tmplId);
		BpmFormTemplate macroTemplate = bpmFormTemplateService.getBpmFormTemplateByAlias(mainTemplate.getMacroTemplateAlias());
		String macroHtml = "";
		if (macroTemplate != null) {
			macroHtml = StringEscapeUtils.unescapeHtml(macroTemplate.getHtml());
		}
		String mainTplHtml = StringEscapeUtils.unescapeHtml(mainTemplate.getHtml());
		String result = this.freemarkEngine.parseByStringTemplate(fieldsMap, new StringBuilder().append(macroHtml).append(mainTplHtml).toString());
		return result;
	}
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public void addBpmFormDef(HttpServletRequest request){
		BpmFormDef bpmFormDef = assembleBpmFormDef(request);
		bpmFormDefService.addBpmFormDef(bpmFormDef);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormDef bpmFormDef = bpmFormDefService.getBpmFormDefById(id);
		if(bpmFormDef==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormDefEdit")
					.addObject("bpmFormDef",bpmFormDef)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public void updateBpmFormDef(HttpServletRequest request){
		BpmFormDef bpmFormDef = assembleBpmFormDef(request);
		bpmFormDefService.updateBpmFormDef(bpmFormDef);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	public void deleteBpmFormDef(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				bpmFormDefService.deleteBpmFormDef(id);
			}
		}
	}
	
	@RequestMapping("validateFormKey")
	public @ResponseBody Object[] validateFormKey(HttpServletRequest request){
		String formDefId = StringUtils.getNullBlankStr(request.getParameter("formDefId"));
		String formKey = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmFormDef bpmFormDef = new BpmFormDef();
		bpmFormDef.setFormDefId(formDefId);
		bpmFormDef.setFormKey(formKey);
		int count = bpmFormDefService.countBpmFormDef(bpmFormDef);
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
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormDef bpmFormDef = bpmFormDefService.getBpmFormDefById(id);
		if(bpmFormDef==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormDefView")
					.addObject("bpmFormDef",bpmFormDef);
	}
	

	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmFormDef assembleBpmFormDef(HttpServletRequest request){
		String formDefId = StringUtils.getNullBlankStr(request.getParameter("formDefId"));
		String catalogId = StringUtils.getNullBlankStr(request.getParameter("catalogId"));
		String formKey = StringUtils.getNullBlankStr(request.getParameter("formKey"));
		String subject = StringUtils.getNullBlankStr(request.getParameter("subject"));
		String formDesc = StringUtils.getNullBlankStr(request.getParameter("formDesc"));
		String html = StringUtils.getNullBlankStr(request.getParameter("html"));
		BpmFormDef bpmFormDef = null;
		if(StringUtils.isEmpty(formDefId)){
			bpmFormDef = new BpmFormDef();
		}else{
			bpmFormDef = bpmFormDefService.getBpmFormDefById(formDefId);
		}
		bpmFormDef.setCatalogId(catalogId);
		bpmFormDef.setFormKey(formKey);
		bpmFormDef.setSubject(subject);
		bpmFormDef.setFormDesc(formDesc);
		bpmFormDef.setHtml(html);
		return bpmFormDef;
	}
}