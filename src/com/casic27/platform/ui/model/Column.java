/**
 * @(#)com.casic27.platform.ui.model.Column.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-6-21
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.model;


import com.casic27.platform.util.StringUtils;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class Column extends Component {
	/**
	 * 对齐 left, center, right
	 */
	private String align;
	
	/**
	 * 设置列的 css
	 */
	private String classes;
	
	/**
	 * 日期格式化，”/”, ”-”, and ”.” 都是有效的日期分隔符
	 */
	private String datefmt;
	
	/**
	 * 列宽度是否要固定不可 变
	 */
	private boolean fixed;
	
	/**
	 * 对某些列进行格式化的设 置
	 */
	private String formatoptions;
	
	/**
	 * 对列进行格式化时设置的函数名或者类 型
	 */
	private String formatter;
	
	/**
	 * 在初始化表格时是否要隐藏此 列
	 */
	private boolean hidden;
	
	/**
	 * 当从服务器端返回的数据中没有 id 时，将此作为唯一 rowid 使 用 只有一个列可以做这项设置。如果设置多于一个，那么只选取第一个，其他被忽 略
	 */
	private boolean key;
	
	/**
	 * 定义了返回的 json 数据映 射
	 */
	private String jsonmap;
	
	/**
	 * 是否可以被 resizable
	 */
	private boolean resizable;
	
	/**
	 * 是否可排序
	 */
	private boolean sortable;
	
	/**
	 * 用在当 datatype 为 local 时，定义搜索列的类型，可选值： int/integer - 对 integer 排序 float/number/currency - 排序数字 date - 排序日期 text - 排序文 本
	 */
	private String sorttype;
	
	/**
	 * 默认列的宽度，只能是象素值，不能是百分 比
	 */
	private int width;
	
	/**
	 * ‘unformat’ 单元格 值
	 */
	private String unformat;
	
	/**
	 * 表格列的名称，所有关键字，保留字都不能作为名称使用包括 subgrid, cb and rn.
	 */
	private String name;
	
	/**
	 * 是否可编辑
	 */
	private boolean editable;
	
	/**
	 * 可以编辑的类型。可选值：text, textarea, select, checkbox, password, button, image and file
	 */
	private String edittype;
	
	/**
	 * 对列进行编辑时设置的一些属性
	 */
	private String editoptions;
	
	/**
	 * 对于可编辑单元格的一些额外属性设置
	 */
	private String editrules;
	
	/**
	 * 是否冻结
	 */
	private boolean frozen; 
	
	/**
	 * 分组统计类型
	 */
	private String summaryType;
	
	/**
	 * 分组描述模板
	 */
	private String summaryTpl;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getDatefmt() {
		return datefmt;
	}

	public void setDatefmt(String datefmt) {
		this.datefmt = datefmt;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public String getFormatoptions() {
		return formatoptions;
	}

	public void setFormatoptions(String formatoptions) {
		this.formatoptions = formatoptions;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public String getJsonmap() {
		return jsonmap;
	}

	public void setJsonmap(String jsonmap) {
		this.jsonmap = jsonmap;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public String getSorttype() {
		return sorttype;
	}

	public void setSorttype(String sorttype) {
		this.sorttype = sorttype;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getUnformat() {
		return unformat;
	}

	public void setUnformat(String unformat) {
		this.unformat = unformat;
	}
	
	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	public String getEdittype() {
		return edittype;
	}

	public void setEdittype(String edittype) {
		this.edittype = edittype;
	}
	
	public String getEditoptions() {
		return editoptions;
	}

	public void setEditoptions(String editoptions) {
		this.editoptions = editoptions;
	}
	
	public String getEditrules() {
		return editrules;
	}

	public void setEditrules(String editrules) {
		this.editrules = editrules;
	}
	
	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getSummaryTpl() {
		return summaryTpl;
	}

	public void setSummaryTpl(String summaryTpl) {
		this.summaryTpl = summaryTpl;
	}
	
	/**
	 * 转化为类Json格式
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String spliter = "";
		sb.append("{");
		
		if(StringUtils.isNotEmpty(name)){
			sb.append(spliter).append("name:'").append(name).append("'");
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(align)){
			sb.append(spliter).append("align:'").append(align).append("'");
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(classes)){
			sb.append(spliter).append("classes:'").append(classes).append("'");
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(datefmt)){
			sb.append(spliter).append("datefmt:'").append(datefmt).append("'");
			spliter = ",";
		}
		
		if(fixed){
			sb.append(spliter).append("fixed:").append(fixed);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(formatoptions)){
			sb.append(spliter).append("formatoptions:").append(formatoptions);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(formatter)){
			sb.append(spliter).append("formatter:").append(formatter);
			spliter = ",";
		}
		
		if(hidden){
			sb.append(spliter).append("hidden:").append(hidden);
			spliter = ",";
		}
		
		if(key){
			sb.append(spliter).append("key:").append(key);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(jsonmap)){
			sb.append(spliter).append("jsonmap:").append(jsonmap);
			spliter = ",";
		}
		
		if(!resizable){
			sb.append(spliter).append("resizable:").append(resizable);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(sorttype)){
			sb.append(spliter).append("sorttype:'").append(sorttype).append("'");
			spliter = ",";
		}
		if(width>0){
			sb.append(spliter).append("width:").append(width);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(unformat)){
			sb.append(spliter).append("unformat:").append(unformat);
			spliter = ",";
		}
		if(!sortable){
			sb.append(spliter).append("sortable:").append(sortable);
			spliter = ",";
		}
		
		if(editable){
			sb.append(spliter).append("editable:").append(editable);
			spliter = ",";
		}
		
		if(StringUtils.isNotEmpty(edittype)){
			sb.append(spliter).append("edittype:'").append(edittype).append("'");
			spliter = ",";
		}
		if(StringUtils.isNotEmpty(editoptions)){
			sb.append(spliter).append("editoptions:").append(editoptions);
			spliter = ",";
		}
		if(StringUtils.isNotEmpty(editrules)){
			sb.append(spliter).append("editrules:").append(editrules);
			spliter = ",";
		}
		if(frozen){
			sb.append(spliter).append("frozen:").append(frozen);
			spliter = ",";
		}
		if(StringUtils.isNotEmpty(summaryType)){
			sb.append(spliter).append("summaryType:'").append(summaryType).append("'");
			spliter = ",";
		}
		if(StringUtils.isNotEmpty(summaryTpl)){
			sb.append(spliter).append("summaryTpl:'").append(summaryTpl).append("'");
			spliter = ",";
		}
		
		sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args){
		Column col = new Column();
		col.setAlign("center");
		col.setClasses("col");
		col.setFixed(true);
		col.setFormatter("formater");
		col.setName("aa");
		System.out.println(col);
	}
}
