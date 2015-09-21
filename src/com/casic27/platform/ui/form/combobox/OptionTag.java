/**
 * @(#)com.casic27.platform.ui.form.combobox.Option.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-6-1
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.form.combobox;

import javax.servlet.jsp.JspException;

import com.casic27.platform.ui.core.UiTagSupport;
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
