package com.kcp.platform.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.kcp.platform.ui.util.CollectionUtils;
import com.kcp.platform.util.StringUtils;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class Grid extends Component {
	
	/**
	 * 获取数据的地 址
	 */
	private String url;

	/**
	 * 如果为ture时，则当表格在首次被创建时会根据父元素比例重新调整表格宽度。
	 */
	private boolean autowidth;
	
	/**
	 * 显示的名称；index 传到服务器端用来排序用的列名称；width 列宽度；align 对齐方式；sortable 是否可以排序
	 */
	private List<Column> colModel;
	
	
	/**
	 * 放置列名称的数组，必须与colModel大小相同
	 */
	private List<String> colNames;
	
	/**
	 * 从服务器端返回的数据类型，（表格期望接收的数据类型）。可选类型：xml，xmlstring，json，local，function
	 */
	private String datatype = "json";
	
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
	 * 是否显示序号
	 */
	private boolean rownumbers;
	
	/**
	 * 页码控制条Page Bar
	 */
	private String pager;
	
	/**
	 * 是否自适应父节点大小
	 */
	private boolean fit;
	
	/**
	 * 需要横向滚动条时，把该属性设置为false
	 */
	private boolean shrinkToFit;
	
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
	
	
	public boolean isShrinkToFit() {
		return shrinkToFit;
	}

	public void setShrinkToFit(boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}

	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public String getPager() {
		return pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	public boolean isRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public boolean isAutowidth() {
		return autowidth;
	}

	public void setAutowidth(boolean autowidth) {
		this.autowidth = autowidth;
	}

	public List<Column> getColModel() {
		return colModel;
	}

	public void setColModel(List<Column> colModel) {
		this.colModel = colModel;
	}

	public List<String> getColNames() {
		return colNames;
	}

	public void setColNames(List<String> colNames) {
		this.colNames = colNames;
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void addColumn(Column column){
		if(colModel==null){
			colModel = new ArrayList<Column>();
		}
		colModel.add(column);
	}
	
	public void addColName(String name){
		if(colNames==null){
			colNames = new ArrayList<String>();
		}
		colNames.add(name);
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
	
	public String getPagerpos() {
		return pagerpos;
	}

	public void setPagerpos(String pagerpos) {
		this.pagerpos = pagerpos;
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
	
	public String getRecordpos() {
		return recordpos;
	}

	public void setRecordpos(String recordpos) {
		this.recordpos = recordpos;
	}
	
	public String toString(){
		String spilter = "";
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if(StringUtils.isNotEmpty(url)){
			sb.append(spilter).append("url:'").append(url).append("'");
			spilter=",";
		}
		
		if(autowidth){
			sb.append(spilter).append("autowidth:").append(autowidth);
			spilter=",";
		}
		
		if(colModel!=null){
			sb.append(spilter);
			sb.append("colModel:[");
			boolean first = true;
			for(Column col : colModel){
				if(!first){
					sb.append(spilter);
				}
				sb.append(col.toString());
				first = false;
				spilter=",";
			}
			sb.append("]");
		}
		
		if(colNames!=null){
			sb.append(spilter).append("colNames:").append(CollectionUtils.join2Json(colNames, ","));
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(datatype)){
			sb.append(spilter).append("datatype:'").append(datatype).append("'");
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(emptyrecords)){
			sb.append(spilter).append("emptyrecords:'").append(emptyrecords).append("'");
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(gridstate)){
			sb.append(spilter).append("gridstate:'").append(gridstate).append("'");
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(height)){
			sb.append(spilter).append("height:'").append(height).append("'");
			spilter=",";
		}
		
		if(multiselect){
			sb.append(spilter).append("multiselect:").append(multiselect);
			spilter=",";
		}
		
		if(!multiboxonly){
			sb.append(spilter).append("multiboxonly:").append(multiboxonly);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(postData)){
			sb.append(spilter).append("postData:").append(postData);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(rowList)){
			sb.append(spilter).append("rowList:").append(rowList);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(userData)){
			sb.append(spilter).append("userData:").append(userData);
			spilter=",";
		}
		
		if(viewrecords){
			sb.append(spilter).append("viewrecords:").append(viewrecords);
			spilter=",";
		}
		
		if(width>0){
			sb.append(spilter).append("width:").append(width);
			spilter=",";
		}
		
		if(!rownumbers){
			sb.append(spilter).append("rownumbers:").append(rownumbers);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(pager)){
			sb.append(spilter).append("pager:'").append("#").append(pager).append("'");
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(rowNum)){
			sb.append(spilter).append("rowNum:'").append(rowNum).append("'");
			spilter=",";
		}
		if(fit){
			sb.append(spilter).append("fit:").append(fit);
			spilter=",";
		}
		
		if(!shrinkToFit){
			sb.append(spilter).append("shrinkToFit:").append(shrinkToFit);
			spilter=",";
		}
		if(StringUtils.isNotEmpty(data)){
			sb.append(spilter).append("data:").append(data);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(afterInsertRow)){
			sb.append(spilter).append("afterInsertRow:").append(afterInsertRow);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(beforeRequest)){
			sb.append(spilter).append("beforeRequest:").append(beforeRequest);
			spilter=",";
		}

		if(StringUtils.isNotEmpty(beforeSelectRow)){
			sb.append(spilter).append("beforeSelectRow:").append(beforeSelectRow);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(gridComplete)){
			sb.append(spilter).append("gridComplete:").append(gridComplete);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(loadComplete)){
			sb.append(spilter).append("loadComplete:").append(loadComplete);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(loadError)){
			sb.append(spilter).append("loadError:").append(loadError);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onCellSelect)){
			sb.append(spilter).append("onCellSelect:").append(onCellSelect);
			spilter=",";
		}
		
		
		if(StringUtils.isNotEmpty(ondblClickRow)){
			sb.append(spilter).append("ondblClickRow:").append(ondblClickRow);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onPaging)){
			sb.append(spilter).append("onPaging:").append(onPaging);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onRightClickRow)){
			sb.append(spilter).append("onRightClickRow:").append(onRightClickRow);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onSelectAll)){
			sb.append(spilter).append("onSelectAll:").append(onSelectAll);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onSelectRow)){
			sb.append(spilter).append("onSelectRow:").append(onSelectRow);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(onSortCol)){
			sb.append(spilter).append("onSortCol:").append(onSortCol);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(resizeStart)){
			sb.append(spilter).append("resizeStart:").append(resizeStart);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(resizeStop)){
			sb.append(spilter).append("resizeStop:").append(resizeStop);
			spilter=",";
		}
		
		if(StringUtils.isNotEmpty(serializeGridData)){
			sb.append(spilter).append("serializeGridData:").append(serializeGridData);
			spilter=",";
		}
		if(grouping){
			sb.append(spilter).append("grouping:").append(grouping);
			spilter=",";
		}
		if(StringUtils.isNotEmpty(groupingView)){
			sb.append(spilter).append("groupingView:").append(groupingView);
			spilter=",";
		}
		if(StringUtils.isNotEmpty(pagerpos)){
			sb.append(spilter).append("pagerpos:'").append(pagerpos).append("'");
			spilter=",";
		}
		if(!pgbuttons){
			sb.append(spilter).append("pgbuttons:").append(pgbuttons);
			spilter=",";
		}
		if(!pginput){
			sb.append(spilter).append("pginput:").append(pginput);
			spilter=",";
		}
		if(StringUtils.isNotEmpty(recordpos)){
			sb.append(spilter).append("recordpos:'").append(recordpos).append("'");
			spilter=",";
		}
		sb.append("}");
		return sb.toString();
	}
	
}
