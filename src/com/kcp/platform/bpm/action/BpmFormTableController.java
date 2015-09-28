package com.kcp.platform.bpm.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.JsonParser;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.entity.BpmFormField;
import com.kcp.platform.bpm.entity.BpmFormTable;
import com.kcp.platform.bpm.service.BpmFormTableService;

@Controller
@RequestMapping("workflow/formTable")
public class BpmFormTableController extends BaseMultiActionController{
	@Autowired
	private BpmFormTableService bpmFormTableService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormTableMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("list")
	public @ResponseBody GridData bpmFormTableList(HttpServletRequest request){
	  	String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
	  	String tableDesc = StringUtils.getNullBlankStr(request.getParameter("tableDesc"));
	  	String isMain = StringUtils.getNullBlankStr(request.getParameter("isMain"));
	  	String isPublished = StringUtils.getNullBlankStr(request.getParameter("isPublished"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("tableName", tableName);
	 	queryMap.put("tableDesc", tableDesc);
	 	queryMap.put("isMain", isMain);
	 	queryMap.put("isPublished", isPublished);
		List<BpmFormTable> result = bpmFormTableService.findBpmFormTablePaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormTableEdit")
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("add")
	public @ResponseBody void addBpmFormTable(HttpServletRequest request)throws Exception{
		String tableJson = StringUtils.getNullBlankStr(request.getParameter("table"));
	    String fieldsJson = StringUtils.getNullBlankStr(request.getParameter("fields"));
		BpmFormTable bpmFormTable = (BpmFormTable)JSONObject.toBean(JSONObject.fromObject(tableJson), BpmFormTable.class);
		List<JSONObject> array = JsonParser.parseJsonArray(fieldsJson);
		List<BpmFormField> fields = new ArrayList<BpmFormField>();
		for(JSONObject obj:array){
			BpmFormField field = new BpmFormField();
			BeanUtils.copyProperties(field, obj);
			fields.add(field);
		}
		bpmFormTableService.addBpmFormTable(bpmFormTable, fields);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormTable bpmFormTable = bpmFormTableService.getBpmFormTableById(id);
		if(bpmFormTable==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormTableEdit")
					.addObject("bpmFormTable",bpmFormTable)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("update")
	public @ResponseBody void updateBpmFormTable(HttpServletRequest request)throws Exception{
		String tableJson = StringUtils.getNullBlankStr(request.getParameter("table"));
	    String fieldsJson = StringUtils.getNullBlankStr(request.getParameter("fields"));
	    BpmFormTable bpmFormTable = (BpmFormTable)JSONObject.toBean(JSONObject.fromObject(tableJson), BpmFormTable.class);
		List<JSONObject> array = JsonParser.parseJsonArray(fieldsJson);
		List<BpmFormField> fields = new ArrayList<BpmFormField>();
		for(JSONObject obj:array){
			BpmFormField field = new BpmFormField();
			BeanUtils.copyProperties(field, obj);
			fields.add(field);
		}
		bpmFormTableService.updateBpmFormTable(bpmFormTable, fields);
	}
	
	@RequestMapping("publish")
	public @ResponseBody void publish(HttpServletRequest request)throws Exception{
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormTable bpmFormTable = bpmFormTableService.getBpmFormTableById(id);
		bpmFormTableService.publishBpmFormTable(bpmFormTable);
	}
	/**
	 * 删除操作
	 */
	@RequestMapping("delete")
	public @ResponseBody void deleteBpmFormTable(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				bpmFormTableService.deleteBpmFormTable(id);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		BpmFormTable bpmFormTable = bpmFormTableService.getBpmFormTableById(id);
		if(bpmFormTable==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("workflow/form/bpmFormTableView")
					.addObject("bpmFormTable",bpmFormTable);
	}
	@RequestMapping("chooser")
	public ModelAndView chooser(HttpServletRequest request){
		return new ModelAndView("workflow/form/bpmFormTableChooser");
	}
	
	
	
	@RequestMapping("tableChooser")
	public ModelAndView tableChooser(HttpServletRequest request){
		return new ModelAndView("workflow/form/DBTableChooser");
	}
	
	@RequestMapping("getAllTables")
	public @ResponseBody GridData getAllTables(HttpServletRequest request){
		List<Map<String,Object>> tableList = bpmFormTableService.getAllTables();
		return new GridData(tableList);
	}
	
	@RequestMapping("getTableColumns")
	public @ResponseBody List<BpmFormField> getTableColumns(HttpServletRequest request){
		String tableName = StringUtils.getNullBlankStr(request.getParameter("tableName"));
		List<Map<String,Object>> columnList = bpmFormTableService.getTableColumns(tableName);
		List<BpmFormField> fieldList = new ArrayList<BpmFormField>();
		for(Map<String,Object> column:columnList){
			BpmFormField field = new BpmFormField();
			field.setFieldName((String)column.get("COLUMN_NAME"));
			field.setFieldDesc((String)column.get("COMMENTS"));
			if("Y".equals((String)column.get("NULLABLE"))){
				field.setIsRequired("0");
			}else{
				field.setIsRequired("1");
			}
			String dataType = (String)column.get("DATA_TYPE");
			if("VARCHAR2".equals(dataType)||"CHAR".equals(dataType)||"NVARCHAR2".equals(dataType)){
				field.setFieldType("varchar");
				int charLen = ((BigDecimal)column.get("CHAR_LENGTH")).intValue();
				field.setCharLen(charLen);
				field.setControlType("2");
			}else if("NUMBER".equals(dataType)){
				field.setFieldType("number");
				BigDecimal precision = (BigDecimal)column.get("DATA_PRECISION");
				BigDecimal scale = (BigDecimal)column.get("DATA_SCALE");
			    int decimalLen = precision==null?0:precision.intValue();
				int intLen = scale==null?0:scale.intValue();
				field.setDecimalLen(decimalLen);
				field.setIntLen(intLen);
				field.setControlType("2");
				field.setValidRule("custom[number]");
			}else if("LONG".equals(dataType)){
				field.setFieldType("long");
				field.setControlType("2");
				field.setValidRule("custom[integer]");
			}else if("INTEGER".equals(dataType)){
				field.setFieldType("integer");
				field.setControlType("2");
				field.setValidRule("custom[integer]");
			}else if("CLOB".equals(dataType)){
				field.setFieldType("clob");
				field.setControlType("3");
			}else if("TIMESTAMP(6)".equals(dataType)){
				field.setFieldType("date");
				field.setCtlProperty("{\"dateFormat\":\"yyyy-MM-dd HH:mm:ss\",\"isCurrentDate\":\"0\"}");
				field.setControlType("8");
			}else if("DATE".equals(dataType)){
				field.setCtlProperty("{\"dateFormat\":\"yyyy-MM-dd\",\"isCurrentDate\":\"0\"}");
				field.setControlType("8");
				field.setFieldType("date");
			}else{
				field.setFieldType("varchar");
			}
			fieldList.add(field);
		}
		return fieldList;
	}
}