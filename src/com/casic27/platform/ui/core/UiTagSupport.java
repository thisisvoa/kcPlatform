package com.casic27.platform.ui.core;

import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casic27.platform.ui.util.StringBufferUtil;
import com.casic27.platform.ui.util.StringBuilderUtil;
import com.casic27.platform.util.StringUtils;

public abstract class UiTagSupport extends BodyTagSupport {
	protected Log logger = LogFactory.getLog(getClass());
	/**
	 * id
	 */
	private String id;
	
	/**
	 * 样式
	 */
	private String style;
	
	/**
	 * 样式类
	 */
	private String css;
	

	protected void appendBaseProStr(StringBuilderUtil sb){
		appendIdStr(sb);
		appendCssStr(sb);
		appendStyleStr(sb);
	}
	protected void appendIdStr(StringBuilderUtil sb){
		appendProperties(sb,"id", getId());
	}
	
	protected void appendStyleStr(StringBuilderUtil sb){
		appendProperties(sb,"style", getStyle());
	}
	
	protected void appendCssStr(StringBuilderUtil sb){
		appendProperties(sb,"class", getCss());
	}
	
	/**
	 * 添加某个属性字符串
	 * @param sb
	 * @param name
	 * @param val
	 */
	protected void appendProperties(StringBuilderUtil sb, String name, Object val){
		if(null!=val && StringUtils.isNotEmpty(val.toString())){
			sb.append(" ")
			  .append(name)
			  .append("=")
			  .append("\"")
			  .append(val)
			  .append("\"");
		}
	}
	
	/**
	 * 若val==defaultVal追加属性值
	 * @param sb
	 * @param name
	 * @param val
	 * @param defaultVal
	 */
	protected void appendProperties(StringBuilderUtil sb, String name, Object val, Object defaultVal){
		if(null!=val && StringUtils.isNotEmpty(val.toString())){
			if(defaultVal.equals(val)){
				return;
			}else{
				sb.append(" ")
				  .append(name)
				  .append("=")
				  .append("\"")
				  .append(val)
				  .append("\"");
			}
		}
	}
	
	/**
	 * 若val==defaultVal追加属性值
	 * @param sb
	 * @param name
	 * @param val
	 * @param defaultVal
	 */
	protected void appendProperties(StringBuilderUtil sb, String name, boolean val, boolean defaultVal){
		if(val==defaultVal){
			return;
		}else{
			sb.append(" ")
			  .append(name)
			  .append("=")
			  .append("\"")
			  .append(val)
			  .append("\"");
		}
	}
	
	/**
	 * 若val==defaultVal追加属性值
	 * @param sb
	 * @param name
	 * @param val
	 * @param defaultVal
	 */
	protected void appendProperties(StringBuilderUtil sb, String name, int val, int defaultVal){
		if(val==defaultVal){
			return;
		}else{
			sb.append(" ")
			  .append(name)
			  .append("=")
			  .append("\"")
			  .append(val)
			  .append("\"");
		}
	}
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

}
