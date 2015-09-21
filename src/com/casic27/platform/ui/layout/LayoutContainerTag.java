/**
 * @(#)com.casic27.platform.ui.layout.LayoutContainerTag.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-5-23
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.layout;

import javax.servlet.jsp.JspException;

import com.casic27.platform.ui.constants.UiConstants;
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
public class LayoutContainerTag extends UiTagSupport {
	
	private static final String LAYOUT_CONTAINER_CSS = UiConstants.CMP_CLS_PRE+"layout";
	/**
	 * 是否自动适应屏幕大小
	 */
	private boolean fit = false;

	public int doStartTag() throws JspException{
		//设置样式
		setCss(LAYOUT_CONTAINER_CSS);
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<div");
		appendBaseProStr(sb);
		appendProperties(sb, "fit", isFit());
		sb.append(">");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.appendln("</div>");
		ResponseUtils.write(this.pageContext, sb.toString());
		return EVAL_PAGE;
	}
	
	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}
}
