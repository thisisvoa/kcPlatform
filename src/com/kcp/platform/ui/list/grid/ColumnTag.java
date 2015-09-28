package com.kcp.platform.ui.list.grid;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class ColumnTag extends TagSupport {
	/**
	 * 表头
	 */
	private String header;
	
	/**
	 * 表格列的名称，所有关键字，保留字都不能作为名称使用包括 subgrid, cb and rn.
	 */
	private String name;
	
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
	private boolean resizable = true;
	
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
	 * 是否可排序
	 */
	private boolean sortable;
	
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
	
	
	public GridTag getGrid() {
		GridTag grid = null;
        Tag tag = this.getParent();
        while (tag != null) {
            if (tag instanceof GridTag) {
            	grid = (GridTag) tag;
            }
            tag = tag.getParent();
        }
        return grid;
    }
	
	public int doStartTag() throws JspException{
		GridTag grid = getGrid();
		grid.addColumn(this);
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException{
		return EVAL_PAGE;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

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
	
	public String getEdittype() {
		return edittype;
	}

	public void setEdittype(String edittype) {
		this.edittype = edittype;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
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
}
