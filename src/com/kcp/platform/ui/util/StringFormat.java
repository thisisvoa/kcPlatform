package com.kcp.platform.ui.util;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public abstract class StringFormat {
	/**
	 * 转换为html title/Alt属性文本，主要转换&,单引号和双引号字符
	 */
	public static String encode4Title(String value) {
		return notNull(value).replaceAll("&", "&amp;").replaceAll("'", "&#39;")
				.replaceAll("\"", "&quot;");
	}

	/**
	* escape sql query characters | sql字符转换
	*/
	public static String escape4Sql(String value){
		value = notNull(value);
		if (value.equals(""))
			return "";
		StringBuffer buffer = new StringBuffer();
		if (value.length() > 15)
			value = value.substring(0, 15);
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == '[')
				buffer.append("[[]");
			else if (ch == '\'')
				buffer.append("''");
			else if (ch == '%')
				buffer.append("[%]");
			else if (ch == '_')
				buffer.append("[_]");
			else if (ch == '^')
				buffer.append("[^]");
			else
				buffer.append(ch);
		}
		return buffer.toString();
	}

	/**
	* 转换为html字符
	*/
	public static String encode4Html(String value) {
		value = notNull(value);
		if (value.equals(""))
			return "";
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == '<')
				result.append("&lt;");
			else if (ch == '&')
				result.append("&amp;");
			else if (ch == '"')
				result.append("&quot;");
			else if (ch == '\r')
				result.append("<BR>");
			else if (ch == '\n') {
				if (value.charAt(i - 1) == '\r') {
				} else
					result.append("<BR>");
			} else if (ch == '\t')
				result.append("&nbsp;&nbsp;&nbsp;&nbsp");
			else if (ch == ' ')
				result.append("&nbsp;");
			else
				result.append(ch);
		}
		return result.toString();
	}

	//转换非空对象，防止产生NullPointerException异常
	public static String notNull(Object s) {
		if (s instanceof String) {
			if (s == null || s.toString().trim().equals("")) {
				return "";
			} else {
				return s.toString().trim();
			}
		} else if (s instanceof Integer) {
			if (s == null) {
				return "";
			} else {
				return s.toString();
			}
		} else {
			return "";
		}
	}

	/**
	* encode for non-ASCII characters
	* @param a
	* @return
	*/
	public static String strChinese(String a) {
		try {
			return new String(a.getBytes("iso-8859-1"));
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}
}
