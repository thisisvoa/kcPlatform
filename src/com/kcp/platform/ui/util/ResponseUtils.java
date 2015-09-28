package com.kcp.platform.ui.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 *  类描述：
 * </pre>
 * 
 * <pre>
 * </pre>
 * 
 */
public class ResponseUtils {

	private static Log log = LogFactory.getLog(ResponseUtils.class);

	public static String filter(String value) {
		if (value == null)
			return null;
		char[] content = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			default:
				result.append(content[i]);
			}
		}

		return result.toString();
	}

	public static void write(PageContext pageContext, String text)
			throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(text);
		} catch (IOException e) {
			log.error(e);
			throw new JspException("输入/输出错误:"+e.toString());
		}
	}

	public static void writePrevious(PageContext pageContext, String text)
			throws JspException {
		JspWriter writer = pageContext.getOut();
		if ((writer instanceof BodyContent))
			writer = ((BodyContent) writer).getEnclosingWriter();
		try {
			writer.print(text);
		} catch (IOException e) {
			log.error(e);
			throw new JspException("输入/输出错误:"+e.toString());
		}
	}
}
