/**
 * @(#)com.casic27.platform.ui.nav.TabNavigatorTag.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-5-25
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.nav;

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
public class TabNavigatorTag extends UiTagSupport {
	
	private static final String LAYOUT_TAB_CSS = UiConstants.CMP_CLS_PRE+"tabs";
	
	/**
	 * Tabs 容器的宽度
	 */
	private String width;
	
	/**
	 * Tabs 容器的高度
	 */
	private String height;
	
	/**
	 * True 就设置 Tabs 容器的尺寸以适应它的父容器
	 */
	private boolean fit = false;
	
	/**
	 * True 就显示 Tabs 容器边框
	 */
	private boolean border = true;
	
	/**
	 * 每按一次tab 滚动按钮，滚动的像素数
	 */
	private int scrollIncrement = 0;
	
	/**
	 * 每一个滚动动画应该持续的毫秒数
	 */
	private int scrollDuration = 0;
	
	/**
	 * 右侧工具栏，每个工具选项都和 Linkbutton 一样
	 */
	private String tools;

	public int doStartTag() throws JspException{
		//设置样式
		setCss(LAYOUT_TAB_CSS);
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<div");
		appendBaseProStr(sb);
		appendProperties(sb, "width", width);
		appendProperties(sb, "height", height);
		appendProperties(sb, "fit", isFit(),false);
		appendProperties(sb, "border", isBorder(), true);
		appendProperties(sb, "scrollIncrement", getScrollIncrement(), 0);
		appendProperties(sb, "scrollDuration", getScrollDuration(), 0);
		appendProperties(sb, "tools", getTools());
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
	
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}
	
	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public int getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(int scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	public int getScrollDuration() {
		return scrollDuration;
	}

	public void setScrollDuration(int scrollDuration) {
		this.scrollDuration = scrollDuration;
	}
	
	public String getTools() {
		return tools;
	}

	public void setTools(String tools) {
		this.tools = tools;
	}
}
