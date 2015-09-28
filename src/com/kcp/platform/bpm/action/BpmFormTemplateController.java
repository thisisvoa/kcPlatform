package com.kcp.platform.bpm.action;

import java.util.HashMap;
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
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmFormTemplate;
import com.kcp.platform.bpm.service.BpmFormTemplateService;

@Controller
@RequestMapping("workflow/formTemplate")
public class BpmFormTemplateController extends BaseMultiActionController{
	@Autowired
	private BpmFormTemplateService bpmFormTemplateService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormTemplateMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmFormTemplateList(HttpServletRequest request){
	  	String templateName = StringUtils.getNullBlankStr(request.getParameter("templateName"));
	  	String templateType = StringUtils.getNullBlankStr(request.getParameter("templateType"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("templateName", templateName);
	 	queryMap.put("templateType", templateType);
		List<BpmFormTemplate> result = bpmFormTemplateService.findBpmFormTemplatePaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		BpmFormTemplate bpmFormTemplate = new BpmFormTemplate();
		bpmFormTemplate.setCanEdit(CommonConst.YES);
		return new ModelAndView("workflow/form/bpmFormTemplateEdit")
					.addObject("type","add")
					.addObject("bpmFormTemplate", bpmFormTemplate);
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public @ResponseBody void addBpmFormTemplate(HttpServletRequest request){
		BpmFormTemplate bpmFormTemplate = assembleBpmFormTemplate(request);
		bpmFormTemplateService.addBpmFormTemplate(bpmFormTemplate);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormTemplate bpmFormTemplate = bpmFormTemplateService.getBpmFormTemplateById(id);
		if(bpmFormTemplate==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormTemplateEdit")
					.addObject("bpmFormTemplate",bpmFormTemplate)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody void updateBpmFormTemplate(HttpServletRequest request){
		BpmFormTemplate bpmFormTemplate = assembleBpmFormTemplate(request);
		bpmFormTemplateService.updateBpmFormTemplate(bpmFormTemplate);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	public @ResponseBody void deleteBpmFormTemplate(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				bpmFormTemplateService.deleteBpmFormTemplate(id);
			}
		}
	}
	
	@RequestMapping("validateAlias")
	public @ResponseBody Object[] validateAlias(HttpServletRequest request){
		String templateId = StringUtils.getNullBlankStr(request.getParameter("templateId"));
		String alias = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmFormTemplate template = new BpmFormTemplate();
		template.setTemplateId(templateId);
		template.setAlias(alias);
		int count = bpmFormTemplateService.countBpmFormTemplate(template);
		Object[] result = new Object[2];
		result[0] = filedId;
		if(count>0){
			result[1] = false;
		}else{
			result[1] = true;
		}
		
		return result;
	}
	
	@RequestMapping("getTemplateList")
	public @ResponseBody List<BpmFormTemplate> getTemplateList(HttpServletRequest request){
		String templateType = StringUtils.getNullBlankStr(request.getParameter("templateType"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("templateType", templateType);
		List<BpmFormTemplate> result = bpmFormTemplateService.findBpmFormTemplate(queryMap);
		return result;
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormTemplate bpmFormTemplate = bpmFormTemplateService.getBpmFormTemplateById(id);
		if(bpmFormTemplate==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormTemplateView")
					.addObject("bpmFormTemplate",bpmFormTemplate);
	}
	

	
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmFormTemplate assembleBpmFormTemplate(HttpServletRequest request){
		String templateId = StringUtils.getNullBlankStr(request.getParameter("templateId"));
		String templateName = StringUtils.getNullBlankStr(request.getParameter("templateName"));
		String templateType = StringUtils.getNullBlankStr(request.getParameter("templateType"));
		String macroTemplateAlias = StringUtils.getNullBlankStr(request.getParameter("macroTemplateAlias"));
		String html = StringUtils.getNullBlankStr(request.getParameter("html"));
		String templateDesc = StringUtils.getNullBlankStr(request.getParameter("templateDesc"));
		String canEdit = StringUtils.getNullBlankStr(request.getParameter("canEdit"));
		String alias = StringUtils.getNullBlankStr(request.getParameter("alias"));
		BpmFormTemplate bpmFormTemplate = new BpmFormTemplate();
		bpmFormTemplate.setTemplateId(templateId);
		bpmFormTemplate.setTemplateName(templateName);
		bpmFormTemplate.setTemplateType(templateType);
		bpmFormTemplate.setMacroTemplateAlias(macroTemplateAlias);
		bpmFormTemplate.setHtml(html);
		bpmFormTemplate.setTemplateDesc(templateDesc);
		bpmFormTemplate.setCanEdit(canEdit);
		bpmFormTemplate.setAlias(alias);
		return bpmFormTemplate;
	}
}