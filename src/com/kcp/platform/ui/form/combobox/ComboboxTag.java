package com.kcp.platform.ui.form.combobox;

import javax.servlet.jsp.JspException;

import com.kcp.platform.ui.constants.UiConstants;
import com.kcp.platform.ui.core.UiTagSupport;
import com.kcp.platform.ui.util.ResponseUtils;
import com.kcp.platform.ui.util.StringBuilderUtil;
import com.kcp.platform.ui.util.StringFormat;
import com.kcp.platform.util.JsonParser;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class ComboboxTag extends UiTagSupport {
	
	private static final String LAYOUT_COMBOBOX_CSS = UiConstants.CMP_CLS_PRE+"combobox";
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 文本框的宽度
	 */
	private String width;
	
	/**
	 * 下拉框的宽度
	 */
	private String dropdownWidth;
	
	/**
	 * 下拉框的高度
	 */
	private String dropdownHeight;
	
	/**
	 * 是否是多选
	 */
	private boolean multiple = false;
	
	/**
	 * 每个选项前是否有图标
	 */
	private boolean hasIcon = false;
	
	/**
	 * 多选值的分隔符号
	 */
	private String separator = ",";
	
	/**
	 * 文本输入框是否可编辑
	 */
	private boolean editable = true;
	
	/**
	 * 是否禁用
	 */
	private boolean disabled = false;
	
	/**
	 * 是否可见
	 */
	private boolean visiable = true;
	
	/**
	 * 数据源值属性名称
	 */
	private String valueField;
	
	/**
	 * 数据源值显示文本属性名称
	 */
	private String textField;
	
	/**
	 * 图标值属性名称
	 */
	private String imgField;
	
	/**
	 * 远程获取值的url，当数据源使用Ajax远程获取时进行设置
	 */
	private String url;
	
	/**
	 * 数据源
	 */
	private Object data;
	
	/**
	 * 是否打开查询过滤功能
	 */
	private boolean filteringMode = false;
	
	/**
	 * 初始化时候的选中值
	 */
	private String selValue;
	

	public int doStartTag() throws JspException{
		setCss(LAYOUT_COMBOBOX_CSS);
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<select");
		appendBaseProStr(sb);
		appendProperties(sb, "name", getName());
		appendProperties(sb, "width", getWidth());
		appendProperties(sb, "dropdownWidth", getDropdownWidth());
		appendProperties(sb, "dropdownHeight", getDropdownHeight());
		appendProperties(sb, "multiple", isMultiple(),false);
		appendProperties(sb, "hasIcon", isHasIcon(),false);
		appendProperties(sb, "separator", getSeparator());
		appendProperties(sb, "editable", isEditable(), true);
		appendProperties(sb, "disabled", isDisabled(), false);
		appendProperties(sb, "visiable", isVisiable(), true);
		appendProperties(sb, "valueField", getValueField());
		appendProperties(sb, "textField", getTextField());
		appendProperties(sb, "imgField", getImgField());
		appendProperties(sb, "url", getUrl());
		appendProperties(sb, "filteringMode", isFilteringMode(),false);
		appendProperties(sb, "selValue", getSelValue());
		if(getData()!=null){
			sb.append(" data=\"")
			  .append(StringFormat.encode4Html(JsonParser.convertArrayObjectToJson(getData())))
			  .append("\"");
		}
		sb.append(">");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.appendln("</select>");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_PAGE;
	}
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getDropdownWidth() {
		return dropdownWidth;
	}

	public void setDropdownWidth(String dropdownWidth) {
		this.dropdownWidth = dropdownWidth;
	}

	public String getDropdownHeight() {
		return dropdownHeight;
	}

	public void setDropdownHeight(String dropdownHeight) {
		this.dropdownHeight = dropdownHeight;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean isHasIcon() {
		return hasIcon;
	}

	public void setHasIcon(boolean hasIcon) {
		this.hasIcon = hasIcon;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isVisiable() {
		return visiable;
	}

	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getImgField() {
		return imgField;
	}

	public void setImgField(String imgField) {
		this.imgField = imgField;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isFilteringMode() {
		return filteringMode;
	}

	public void setFilteringMode(boolean filteringMode) {
		this.filteringMode = filteringMode;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSelValue() {
		return selValue;
	}

	public void setSelValue(String selValue) {
		this.selValue = selValue;
	}
}
