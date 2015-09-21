/**
 * @(#)com.casic27.platform.ui.list.grid.columns.GridTag.java
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
package com.casic27.platform.ui.list.grid;


import javax.servlet.jsp.JspException;

import com.casic27.platform.ui.constants.UiConstants;
import com.casic27.platform.ui.core.UiTagSupport;
import com.casic27.platform.ui.model.Column;
import com.casic27.platform.ui.model.Component;
import com.casic27.platform.ui.model.Grid;
import com.casic27.platform.ui.util.ResponseUtils;
import com.casic27.platform.ui.util.StringBuilderUtil;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class GridTag extends UiTagSupport {
	
	private static final String GRID_CSS = UiConstants.CMP_CLS_PRE+"grid";
	/**
	 * id
	 */
	private String id;
	

	/**
	 * 获取数据的地 址
	 */
	private String url;

	/**
	 * 如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整表格宽度。
	 */
	private boolean autowidth;
	
	/**
	 * 从服务器端返回的数据类型，（表格期望接收的数据类型）。可选类型：xml，xmlstring，json，local，function
	 */
	private String datatype;
	
	/**
	 * 当返回的数据行数为 0 时显示的信息。只有当属性 viewrecords 设置为 ture 时起作 用
	 */
	private String emptyrecords;
	
	/**
	 * 定义当前表格的状态： 'visible' or 'hidden'
	 */
	private String gridstate;
	
	/**
	 * 表格高度，可以是数字，像素值或者百分 比
	 */
	private String height;
	/**
	 * 是否多选
	 */
	private boolean multiselect;
	
	/**
	 * 当multiboxonly为true时只有选择多选框该行才能选中
	 */
	private boolean multiboxonly = true;
	
	/**
	 * 此数组内容直接赋值到 url 上，参数类型： {name1:value1…}
	 */
	private String postData;
	
	/**
	 * 用于设置Grid中一次显示的行数，默认值为20
	 */
	private String rowNum;
	
	/**
	 * 一个数组用来调整表格显示的记录数，此参数值会替代 rowNum 参数值传给服务器端 。
	 */
	private String rowList;
	
	/**
	 * 自定义
	 */
	private String userData;
	
	/**
	 * 是否要显示总记录 数
	 */
	private boolean viewrecords;
	
	/**
	 * 如果设置则按此设置为主，如果没有设置则按 colModel 中定义的宽度计 算
	 */
	private int width;
	
	/**
	 * 模型
	 */
	private Component component;
	
	/**
	 * 是否显示序号列
	 */
	private boolean rownumbers = true;
	
	/**
	 * 是否分页
	 */
	private boolean pageable = false;
	
	/**
	 * 是否自适应父节点大小
	 */
	private boolean fit;

	/**
	 * 需要横向滚动条时，把该属性设置为false
	 */
	private boolean shrinkToFit = true;
	
	/**
	 * 当datatype为local时,用来指定数据源对象
	 */
	private String data;
	
	/**
	 * 是否分组
	 */
	private boolean grouping;
	
	/**
	 * 分组设置
	 */
	private String groupingView;
	
	/**
	 * 分页栏位置 
	 */
	private String pagerpos;
	
	/**
	 * 是否显示分页按钮
	 */
	private boolean pgbuttons = true;
	
	/**
	 * 是否显示分页输入框
	 */
	private boolean pginput = true;
	
	/**
	 * 记录总数位置
	 */
	private String recordpos;

	//========================事件============================
	/**
	 * 当插入每行时触发
	 */
	private String afterInsertRow;
	

	/**
	 * 向服务器端发起请求之前触发此事件但如果datatype是一个function时例外
	 */
	private String beforeRequest;
	
	/**
	 * 当用户点击当前行在未选择此行时触发。
	 */
	private String beforeSelectRow;
	
	/**
	 * 当表格所有数据都加载完成而且其他的处理也都完成时触发此事件，排序，翻页同样也会触发此事件
	 */
	private String gridComplete;
	
	/**
	 * 当从服务器返回响应时执行
	 */
	private String loadComplete;
	
	/**
	 * 如果请求服务器失败则调用此方法。
	 */
	private String loadError;
	
	/**
	 * 当点击单元格时触发。
	 */
	private String onCellSelect;
	
	/**
	 * 双击行时触发。
	 */
	private String ondblClickRow;
	
	/**
	 * 点击翻页按钮填充数据之前触发此事件，同样当输入页码跳转页面时也会触发此事件
	 */
	private String onPaging;
	
	/**
	 * 在行上右击鼠标时触发此事件。
	 */
	private String onRightClickRow;
	
	/**
	 * multiselect为ture，且点击头部的checkbox时才会触发此事件。
	 */
	private String onSelectAll;
	
	/**
	 * 当选择行时触发此事件。
	 */
	private String onSelectRow;
	
	/**
	 * 当点击排序列但是数据还未进行变化时触发此事件。
	 */
	private String onSortCol;
	
	/**
	 * 当开始改变一个列宽度时触发此事件。
	 */
	private String resizeStart;
	
	/**
	 * 当列宽度改变之后触发此事件。
	 */
	private String resizeStop;
	
	/**
	 * 向服务器发起请求时会把数据进行序列化，用户自定义数据也可以被提交到服务器端
	 */
	private String serializeGridData;
	
	/**
	 * 添加类
	 * @param col
	 */
	public void addColumn(ColumnTag col){
		Grid grid = (Grid)component;
		grid.addColName(col.getHeader());
		
		Column column = new Column();
		column.setAlign(col.getAlign());
		column.setClasses(col.getClasses());
		column.setDatefmt(col.getDatefmt());
		column.setFixed(col.isFixed());
		column.setFormatoptions(col.getFormatoptions());
		column.setFormatter(col.getFormatter());
		column.setHidden(col.isHidden());
		column.setJsonmap(col.getJsonmap());
		column.setKey(col.isKey());
		column.setName(col.getName());
		column.setResizable(col.isResizable());
		column.setSorttype(col.getSorttype());
		column.setUnformat(col.getUnformat());
		column.setWidth(col.getWidth());
		column.setSortable(col.isSortable());
		column.setEdittype(col.getEdittype());
		column.setEditoptions(col.getEditoptions());
		column.setEditrules(col.getEditrules());
		column.setFrozen(col.isFrozen());
		column.setEditable(col.isEditable());
		column.setSummaryType(col.getSummaryType());
		column.setSummaryTpl(col.getSummaryTpl());
		grid.addColumn(column);
	}
	
	public int doStartTag() throws JspException{
		component = new Grid();
		Grid grid = (Grid)component;
		grid.setUrl(url);
		grid.setAutowidth(autowidth);
		grid.setDatatype(datatype);
		grid.setEmptyrecords(emptyrecords);
		grid.setGridstate(gridstate);
		grid.setHeight(height);
		grid.setMultiselect(multiselect);
		grid.setMultiboxonly(multiboxonly);
		grid.setPostData(postData);
		grid.setRowList(rowList);
		grid.setUserData(userData);
		grid.setViewrecords(viewrecords);
		grid.setWidth(width);
		grid.setRownumbers(rownumbers);
		grid.setRowNum(rowNum);
		grid.setFit(fit);
		grid.setShrinkToFit(shrinkToFit);
		grid.setData(data);
		grid.setGrouping(grouping);
		grid.setGroupingView(groupingView);
		grid.setPagerpos(pagerpos);
		grid.setPgbuttons(pgbuttons);
		grid.setPginput(pginput);
		grid.setRecordpos(recordpos);
		
		grid.setAfterInsertRow(afterInsertRow);
		grid.setBeforeRequest(beforeRequest);
		grid.setBeforeSelectRow(beforeSelectRow);
		grid.setGridComplete(gridComplete);
		grid.setLoadComplete(loadComplete);
		grid.setLoadError(loadError);
		grid.setOnCellSelect(onCellSelect);
		grid.setOndblClickRow(ondblClickRow);
		grid.setOnPaging(onPaging);
		grid.setOnRightClickRow(onRightClickRow);
		grid.setOnSelectAll(onSelectAll);
		grid.setOnSelectRow(onSelectRow);
		grid.setOnSortCol(onSortCol);
		grid.setResizeStart(resizeStart);
		grid.setResizeStop(resizeStop);
		grid.setSerializeGridData(serializeGridData);
		
		setCss(GRID_CSS);
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<table");
		appendIdStr(sb);
		appendCssStr(sb);
		sb.appendln(">");
		sb.appendln("</table>");
		
		if(pageable){
			sb.append("<div")
			  .append(" id=\"")
			  .append(getId())
			  .append("_pager\"")
			  .append("></div>");
			grid.setPager(getId()+"_pager");
		}
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException{
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<script>");
		sb.append("$('#").append(id).append("')")
		  .append(".data(\"grid\",{options:")
		  .append(component.toString())
		  .append("});");
		sb.append("</script>");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_PAGE;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAutowidth() {
		return autowidth;
	}
	public void setAutowidth(boolean autowidth) {
		this.autowidth = autowidth;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getEmptyrecords() {
		return emptyrecords;
	}
	public void setEmptyrecords(String emptyrecords) {
		this.emptyrecords = emptyrecords;
	}
	public String getGridstate() {
		return gridstate;
	}
	public void setGridstate(String gridstate) {
		this.gridstate = gridstate;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public boolean isMultiselect() {
		return multiselect;
	}
	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}
	public String getPostData() {
		return postData;
	}
	public void setPostData(String postData) {
		this.postData = postData;
	}
	public String getRowList() {
		return rowList;
	}
	public void setRowList(String rowList) {
		this.rowList = rowList;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public boolean isViewrecords() {
		return viewrecords;
	}
	public void setViewrecords(boolean viewrecords) {
		this.viewrecords = viewrecords;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public boolean isRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(boolean rownumbers) {
		this.rownumbers = rownumbers;
	}
	
	public boolean isPageable() {
		return pageable;
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}
	
	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}
	
	public boolean isShrinkToFit() {
		return shrinkToFit;
	}

	public void setShrinkToFit(boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getAfterInsertRow() {
		return afterInsertRow;
	}

	public void setAfterInsertRow(String afterInsertRow) {
		this.afterInsertRow = afterInsertRow;
	}

	public String getBeforeRequest() {
		return beforeRequest;
	}

	public void setBeforeRequest(String beforeRequest) {
		this.beforeRequest = beforeRequest;
	}

	public String getBeforeSelectRow() {
		return beforeSelectRow;
	}

	public void setBeforeSelectRow(String beforeSelectRow) {
		this.beforeSelectRow = beforeSelectRow;
	}

	public String getGridComplete() {
		return gridComplete;
	}

	public void setGridComplete(String gridComplete) {
		this.gridComplete = gridComplete;
	}

	public String getLoadComplete() {
		return loadComplete;
	}

	public void setLoadComplete(String loadComplete) {
		this.loadComplete = loadComplete;
	}

	public String getLoadError() {
		return loadError;
	}

	public void setLoadError(String loadError) {
		this.loadError = loadError;
	}

	public String getOnCellSelect() {
		return onCellSelect;
	}

	public void setOnCellSelect(String onCellSelect) {
		this.onCellSelect = onCellSelect;
	}

	public String getOndblClickRow() {
		return ondblClickRow;
	}

	public void setOndblClickRow(String ondblClickRow) {
		this.ondblClickRow = ondblClickRow;
	}

	public String getOnPaging() {
		return onPaging;
	}

	public void setOnPaging(String onPaging) {
		this.onPaging = onPaging;
	}

	public String getOnRightClickRow() {
		return onRightClickRow;
	}

	public void setOnRightClickRow(String onRightClickRow) {
		this.onRightClickRow = onRightClickRow;
	}

	public String getOnSelectAll() {
		return onSelectAll;
	}

	public void setOnSelectAll(String onSelectAll) {
		this.onSelectAll = onSelectAll;
	}

	public String getOnSelectRow() {
		return onSelectRow;
	}

	public void setOnSelectRow(String onSelectRow) {
		this.onSelectRow = onSelectRow;
	}

	public String getOnSortCol() {
		return onSortCol;
	}

	public void setOnSortCol(String onSortCol) {
		this.onSortCol = onSortCol;
	}

	public String getResizeStart() {
		return resizeStart;
	}

	public void setResizeStart(String resizeStart) {
		this.resizeStart = resizeStart;
	}

	public String getResizeStop() {
		return resizeStop;
	}

	public void setResizeStop(String resizeStop) {
		this.resizeStop = resizeStop;
	}

	public String getSerializeGridData() {
		return serializeGridData;
	}

	public void setSerializeGridData(String serializeGridData) {
		this.serializeGridData = serializeGridData;
	}
	
	public boolean isGrouping() {
		return grouping;
	}

	public void setGrouping(boolean grouping) {
		this.grouping = grouping;
	}

	public String getGroupingView() {
		return groupingView;
	}

	public void setGroupingView(String groupingView) {
		this.groupingView = groupingView;
	}
	
	public boolean isMultiboxonly() {
		return multiboxonly;
	}

	public void setMultiboxonly(boolean multiboxonly) {
		this.multiboxonly = multiboxonly;
	}
	public boolean isPgbuttons() {
		return pgbuttons;
	}

	public void setPgbuttons(boolean pgbuttons) {
		this.pgbuttons = pgbuttons;
	}

	public boolean isPginput() {
		return pginput;
	}

	public void setPginput(boolean pginput) {
		this.pginput = pginput;
	}

	public String getPagerpos() {
		return pagerpos;
	}

	public void setPagerpos(String pagerpos) {
		this.pagerpos = pagerpos;
	}
	
	public String getRecordpos() {
		return recordpos;
	}

	public void setRecordpos(String recordpos) {
		this.recordpos = recordpos;
	}
}
