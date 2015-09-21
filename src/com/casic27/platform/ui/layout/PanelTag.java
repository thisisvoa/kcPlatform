/**
 * @(#)com.casic27.platform.ui.layout.PanelTag.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-5-24
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
public class PanelTag extends UiTagSupport {
	
	private static final String PANEL_CONTAINER_CSS = UiConstants.CMP_CLS_PRE+"panel"; 
	
	/**
	 * 显示在控制面板顶部的标题文本
	 */
	private String title;
	
	/**
	 * 在控制面板标题前显示一个16x16大小图标的CSS类
	 */
	private String iconCls;
	
	/**
	 * 设置控制面板的宽度
	 */
	private String width;
	
	/**
	 * 设置控制面板的高度
	 */
	private String height;
	
	/**
	 * 对控制面板引用一个CSS类
	 */
	private String cls;
	
	/**
	 * 对内容面板主体引用一个CSS类
	 */
	private String bodyCls;
	
	/**
	 * 设置为true时，控制面板的大小将铺满它所在的容器
	 */
	private boolean fit=false; 
	
	/**
	 * 定义是否显示控制面板边框
	 */
	private boolean border = true;
	
	/**
	 * 如果设置为true，在控制面板被创建时将被重置大小并且自动布局
	 */
	private boolean doSize = true;;
	
	/**
	 * 如果设置为true，控制面板头部将不被创建
	 */
	private boolean noheader = false;
	
	/**
	 * 控制面板主体的内容
	 */
	private String content;
	
	/**
	 * 定义是否显示可折叠按钮
	 */
	private boolean collapsible = false;
	
	/**
	 * 定义是否显示最小化按钮
	 */
	private boolean minimizable = false;
	
	/**
	 * 定义是否显示最大化按钮
	 */
	private boolean maximizable = false;
	
	/**
	 * 定义是否显示关闭按钮
	 */
	private boolean closable = false;
	
	/**
	 * 自定义工具栏，每一个工具都可以包含2个属性：图标类 和句柄
	 */
	private String tools;
	
	/**
	 * 定义控制面板初始化时是否折叠
	 */
	private boolean collapsed = false;
	
	
	/**
	 * 定义控制面板初始化时是否最小化
	 */
	private boolean minimized = false;
	
	/**
	 * 定义控制面板初始化时是否最大化
	 */
	private boolean maximized = false;
	
	/**
	 * 定义控制面板初始化时是否关闭
	 */
	private boolean closed = false;
	
	public int doStartTag() throws JspException{
		setCss(PANEL_CONTAINER_CSS);
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<div");
		appendBaseProStr(sb);
		appendProperties(sb, "title", title);
		appendProperties(sb, "iconCls", iconCls);
		appendProperties(sb, "width", width);
		appendProperties(sb, "height", height);
		appendProperties(sb, "cls", cls);
		appendProperties(sb, "bodyCls", bodyCls);
		appendProperties(sb, "fit", fit, false);
		appendProperties(sb, "border", border, true);
		appendProperties(sb, "doSize", doSize, true);
		appendProperties(sb, "noheader", noheader, false);
		appendProperties(sb, "content", content, false);
		appendProperties(sb, "collapsible", collapsible, false);
		appendProperties(sb, "minimizable", minimizable, false);
		appendProperties(sb, "maximizable", maximizable, false);
		appendProperties(sb, "closable", closable, false);
		appendProperties(sb, "tools", tools);
		appendProperties(sb, "collapsed", collapsed, maximizable);
		appendProperties(sb, "minimized", minimized, maximizable);
		appendProperties(sb, "maximized", maximized, maximizable);
		appendProperties(sb, "closed", closed, maximizable);
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
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

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getBodyCls() {
		return bodyCls;
	}

	public void setBodyCls(String bodyCls) {
		this.bodyCls = bodyCls;
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

	public boolean isDoSize() {
		return doSize;
	}

	public void setDoSize(boolean doSize) {
		this.doSize = doSize;
	}

	public boolean isNoheader() {
		return noheader;
	}

	public void setNoheader(boolean noheader) {
		this.noheader = noheader;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public boolean isMinimizable() {
		return minimizable;
	}

	public void setMinimizable(boolean minimizable) {
		this.minimizable = minimizable;
	}

	public boolean isMaximizable() {
		return maximizable;
	}

	public void setMaximizable(boolean maximizable) {
		this.maximizable = maximizable;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public String getTools() {
		return tools;
	}

	public void setTools(String tools) {
		this.tools = tools;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public boolean isMinimized() {
		return minimized;
	}

	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
}
