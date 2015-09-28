package com.kcp.platform.ui.layout;

import javax.servlet.jsp.JspException;

import com.kcp.platform.ui.core.UiTagSupport;
import com.kcp.platform.ui.util.ResponseUtils;
import com.kcp.platform.ui.util.StringBuilderUtil;

public class LayoutTag extends UiTagSupport {
	
	/**
	 * Layout panel 的标题文字
	 */
	private String title;
	
	/**
	 * 定义 layout panel 的位置，其值是下列之一：north、south、east、west、center。
	 */
	private String region;
	
	/**
	 * True 就显示 layout panel 的边框
	 */
	private boolean border = true;
	
	/**
	 * True 就显示拆分栏，用户可以用它改变panel 的尺寸
	 */
	private boolean split = false;
	
	/**
	 * 在panel 头部显示一个图标的CSS 类
	 */
	private String iconCls;
	
	/**
	 * 从远程站点加载数据的 URL
	 */
	private String href;
	
	/**
	 * 是否有圆角
	 */
	private boolean radius = false;

	public int doStartTag() throws JspException{
		if(region==null||region.equals("")){
			return SKIP_BODY;
		}
		StringBuilderUtil sb = new StringBuilderUtil(new StringBuilder());
		sb.append("<div");
		appendBaseProStr(sb);
		appendProperties(sb, "title", getTitle());
		appendProperties(sb, "region", getRegion());
		appendProperties(sb, "border", isBorder());
		appendProperties(sb, "split", isSplit());
		appendProperties(sb, "iconCls", getIconCls());
		appendProperties(sb, "href", getHref());
		appendProperties(sb, "radius", isRadius());
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public boolean isRadius() {
		return radius;
	}

	public void setRadius(boolean radius) {
		this.radius = radius;
	}
	
}
