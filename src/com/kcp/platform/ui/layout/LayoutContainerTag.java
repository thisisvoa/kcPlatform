package com.kcp.platform.ui.layout;

import javax.servlet.jsp.JspException;

import com.kcp.platform.ui.constants.UiConstants;
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
