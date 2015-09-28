package com.kcp.platform.bpm.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmCatalog;
import com.kcp.platform.bpm.service.BpmCatalogService;

@Controller
@RequestMapping(value = "/workflow/bpmCatalog")
public class BpmCatalogController extends BaseMultiActionController{
	
	@Autowired
	private BpmCatalogService bpmCatalogService;
	
	/**
	 * 根节点列表，树形展示
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public List<BpmCatalog> bpmCatalogList(HttpServletRequest request){
		String catalogType = StringUtils.getNullBlankStr(request.getParameter("catalogType"));
		List<BpmCatalog> result = bpmCatalogService.findAllBpmCatalog(catalogType);
		return result;
	}
	
	@RequestMapping("treeXml")
	@ResponseBody
	public String bpmCatalogXml(HttpServletRequest request){
		String catalogType = StringUtils.getNullBlankStr(request.getParameter("catalogType"));
		List<BpmCatalog> bpmCatalogList = bpmCatalogService.findAllBpmCatalog(catalogType);
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("node");
		root.addAttribute("id", "0");
		root.addAttribute("label", "全部");
		root.addAttribute("key", "");
		if(bpmCatalogList!=null){
			createChildXML(root, "0", bpmCatalogList);
		}
		return document.asXML();
	}
	
	private void createChildXML(Element parent, String parentId, List<BpmCatalog> bpmCatalogList){
		for(BpmCatalog bpmCatalog:bpmCatalogList){
			if(parentId.equals(bpmCatalog.getParentId())){
				Element element = parent.addElement("node");
				element.addAttribute("id", bpmCatalog.getId());
				element.addAttribute("label", bpmCatalog.getCatalogName());
				element.addAttribute("key", bpmCatalog.getCatalogKey());
				createChildXML(element, bpmCatalog.getId(), bpmCatalogList);	
			}
		}
	}
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		String parentId = StringUtils.getNullBlankStr(request.getParameter("parentId"));
		String catalogType = StringUtils.getNullBlankStr(request.getParameter("catalogType"));
		BpmCatalog parent = null;
		if("0".equals(parentId)){
			parent = new BpmCatalog();
			parent.setId("0");
			parent.setCatalogName("根节点");
		}else{
			parent = bpmCatalogService.getBpmCatalogById(parentId);
		}
		if(parent==null){
			throw new BusinessException("父节点不存在!");
		}
		BpmCatalog bpmCatalog = new BpmCatalog();
		bpmCatalog.setCatalogType(catalogType);
		int orderNo = bpmCatalogService.getMaxOrderNo(parentId);
		bpmCatalog.setOrderNo(orderNo);
		return new ModelAndView("workflow/bpm/bpmCatalogEdit")
					.addObject("type","add")
					.addObject("parent",parent)
					.addObject("bpmCatalog", bpmCatalog);
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	@ResponseBody
	public void addProcessType(HttpServletRequest request){
		BpmCatalog bpmCatalog = assembleBpmCatalog(request);
		bpmCatalogService.addBpmCatalog(bpmCatalog);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmCatalog bpmCatalog = bpmCatalogService.getBpmCatalogById(id);
		if(bpmCatalog==null){
			throw new BusinessException("选择的记录不存在！");
		}
		BpmCatalog parent = null;
		if("0".equals(bpmCatalog.getParentId())){
			parent = new BpmCatalog();
			parent.setId("0");
			parent.setCatalogName("根节点");
		}else{
			parent = bpmCatalogService.getBpmCatalogById(bpmCatalog.getParentId());
		}
		return new ModelAndView("workflow/bpm/bpmCatalogEdit")
					.addObject("bpmCatalog",bpmCatalog)
					.addObject("type","update")
					.addObject("parent",parent);
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	@ResponseBody
	public void updateProcessType(HttpServletRequest request){
		BpmCatalog bpmCatalog = assembleBpmCatalog(request);
		bpmCatalogService.updateBpmCatalog(bpmCatalog);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	@ResponseBody
	public void deleteProcessType(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		if(StringUtils.isNotEmpty(id)){
			bpmCatalogService.deleteBpmCatalog(id);
		}
	}
	
	@RequestMapping("validateKey")
	@ResponseBody
	public Object validateKey(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		String procTypeKey = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		BpmCatalog bpmCatalog = new BpmCatalog();
		bpmCatalog.setId(id);
		bpmCatalog.setCatalogKey(procTypeKey.toUpperCase());
		Object[] result = new Object[2];
		result[0] = fieldId;
		int count = bpmCatalogService.countBpmCatalog(bpmCatalog);
		if(count>0){
			result[1] = false;
		}else{
			result[1] = true;
		}
		return result;
	}
	/**
	 * 组装页面传递过来的参数
	 */
	private BpmCatalog assembleBpmCatalog(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		String catalogKey = StringUtils.getNullBlankStr(request.getParameter("catalogKey"));
		String catalogName = StringUtils.getNullBlankStr(request.getParameter("catalogName"));
		String catalogType = StringUtils.getNullBlankStr(request.getParameter("catalogType"));
		String parentId = StringUtils.getNullBlankStr(request.getParameter("parentId"));
		int orderNo = Integer.parseInt(request.getParameter("orderNo"));
		BpmCatalog bpmCatalog = null;
		if(StringUtils.isEmpty(id)){
			bpmCatalog = new BpmCatalog();
		}else{
			bpmCatalog = bpmCatalogService.getBpmCatalogById(id);
		}
		bpmCatalog.setCatalogKey(catalogKey);
		bpmCatalog.setCatalogName(catalogName);
		bpmCatalog.setCatalogType(catalogType);
		bpmCatalog.setParentId(parentId);
		bpmCatalog.setOrderNo(orderNo);
		return bpmCatalog;
	}
}