/**
 * @(#)com.casic27.platform.ui.nav.TabTag.java
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
public class TabTag extends UiTagSupport {
	/**
	 * Tab panel 的标题文字
	 */
	private String title;
	
	/**
	 * Tab panel 的内容
	 */
	private String content;
	
	/**
	 * 显示在tab panel 标题上的图标的 CSS 类
	 */
	private String iconCls;
	
	/**
	 * Tab panel 的宽度
	 */
	private String width;
	
	/**
	 * Tab panel 的高度
	 */
	private String height;
	
	/**
	 * 当设置为 true 时，tab panel 将显示一个关闭按钮，点击它就能关闭这个tab panel
	 */
	private boolean closable = false;
	
	/**
	 * 当设置为 true 时，tab panel 将被选中
	 */
	private boolean selected = false;
	
	/**
	 * 当设置为false时， tab panel将不可用
	 */
	private boolean enable = true;
	
	/**
	 * Tab右侧工具按钮
	 */
	private String tools;
	
	/**
	 * 内置嵌套Iframe时使用
	 */
	private String url;


	public int doStartTag() throws JspException{
		//设置样式
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<div");
		appendBaseProStr(sb);
		appendProperties(sb, "title", getTitle());
		appendProperties(sb, "content", getContent());
		appendProperties(sb, "width", getWidth());
		appendProperties(sb, "height", getHeight());
		appendProperties(sb, "closable", isClosable(), false);
		appendProperties(sb, "selected", isSelected(), false);
		appendProperties(sb, "enable", isEnable(), true);
		appendProperties(sb, "iconCls", getIconCls(), true);
		appendProperties(sb, "tools", getTools());
		appendProperties(sb, "url", getUrl());
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public boolean isClosable() {
		return closable;
	}
	public void setClosable(boolean closable) {
		this.closable = closable;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public String getTools() {
		return tools;
	}

	public void setTools(String tools) {
		this.tools = tools;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
