package com.kcp.platform.ui.form.combobox;

import javax.servlet.jsp.JspException;

import com.kcp.platform.ui.core.UiTagSupport;
import com.kcp.platform.ui.util.ResponseUtils;
import com.kcp.platform.ui.util.StringBuilderUtil;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class OptionTag extends UiTagSupport {
	/**
	 * 值
	 */
	private String value;
	
	/**
	 * 图标地址
	 */
	private String img;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public int doStartTag() throws JspException{
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<option");
		appendProperties(sb, "value", getValue());
		appendProperties(sb, "img", getImg());
		sb.append(">");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.appendln("</option>");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_PAGE;
	}
}
