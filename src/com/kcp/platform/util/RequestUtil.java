package com.kcp.platform.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtil {

	public static String getString(HttpServletRequest request, String key,
			String defaultValue) {
		String value = request.getParameter(key);
		if (StringUtils.isEmpty(value))
			return defaultValue;
		return value.replace("'", "''").trim();
	}

	public static String getString(HttpServletRequest request, String key) {
		return getString(request, key, "");
	}

	public static String getSecureString(HttpServletRequest request,
			String key, String defaultValue) {
		String value = request.getParameter(key);
		if (StringUtils.isEmpty(value))
			return defaultValue;
		return filterInject(value);
	}

	public static String getSecureString(HttpServletRequest request, String key) {
		return getSecureString(request, key, "");
	}

	public static String filterInject(String str) {
		String injectstr = "\\||;|exec|insert|select|delete|update|count|chr|truncate|char";
		Pattern regex = Pattern.compile(injectstr, 226);
		Matcher matcher = regex.matcher(str);
		str = matcher.replaceAll("");
		str = str.replace("'", "''");
		str = str.replace("-", "—");
		str = str.replace("(", "（");
		str = str.replace(")", "）");
		str = str.replace("%", "％");
		return str;
	}

	public static String getLowercaseString(HttpServletRequest request,
			String key) {
		return getString(request, key).toLowerCase();
	}

	public static int getInt(HttpServletRequest request, String key) {
		return getInt(request, key, 0);
	}

	public static int getInt(HttpServletRequest request, String key,
			int defaultValue) {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Integer.parseInt(str);
	}
	
	public static short getShort(HttpServletRequest request, String key) {
		return getShort(request, key, (short)0);
	}
	
	public static short getShort(HttpServletRequest request, String key, short defaultValue) {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Short.parseShort(str);
	}

	public static long getLong(HttpServletRequest request, String key) {
		return getLong(request, key, 0L);
	}
	
	
	public static Long[] getLongAryByStr(HttpServletRequest request, String key) {
		String sysUserId = request.getParameter(key);
		String[] aryId = sysUserId.split(",");
		Long[] lAryId = new Long[aryId.length];
		for (int i = 0; i < aryId.length; i++) {
			lAryId[i] = Long.valueOf(Long.parseLong(aryId[i]));
		}
		return lAryId;
	}

	public static String[] getStringAryByStr(HttpServletRequest request,
			String key) {
		String sysUserId = request.getParameter(key);
		String[] aryId = sysUserId.split(",");
		String[] lAryId = new String[aryId.length];
		for (int i = 0; i < aryId.length; i++) {
			lAryId[i] = aryId[i];
		}
		return lAryId;
	}

	public static Integer[] getIntAry(HttpServletRequest request, String key) {
		String[] aryKey = request.getParameterValues(key);
		Integer[] aryInt = new Integer[aryKey.length];
		for (int i = 0; i < aryKey.length; i++) {
			aryInt[i] = Integer.valueOf(Integer.parseInt(aryKey[i]));
		}
		return aryInt;
	}

	public static long getLong(HttpServletRequest request, String key,
			long defaultValue) {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Long.parseLong(str);
	}

	public static float getFloat(HttpServletRequest request, String key) {
		return getFloat(request, key, 0.0F);
	}

	public static float getFloat(HttpServletRequest request, String key,
			float defaultValue) {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Float.parseFloat(request.getParameter(key));
	}

	public static Date getDate(HttpServletRequest request, String key)
			throws ParseException {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return null;
		SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd");
		Date _date = (Date) spdf.parseObject(str);
		return _date;
	}

	public static Date getTimeStamp(HttpServletRequest request, String key)
			throws ParseException {
		String str = request.getParameter(key);
		if (StringUtils.isEmpty(str))
			return null;

		SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date _date = (Date) spdf.parseObject(str);
		return _date;
	}

	public static String getUrl(HttpServletRequest request) {
		StringBuffer urlThisPage = new StringBuffer();
		urlThisPage.append(request.getRequestURI());
		Enumeration e = request.getParameterNames();
		String para = "";
		String values = "";
		urlThisPage.append("?");
		while (e.hasMoreElements()) {
			para = (String) e.nextElement();
			values = request.getParameter(para);
			urlThisPage.append(para);
			urlThisPage.append("=");
			urlThisPage.append(values);
			urlThisPage.append("&");
		}
		return urlThisPage.substring(0, urlThisPage.length() - 1);
	}

	public static String getPrePage(HttpServletRequest request) {
		return request.getHeader("Referer");
	}

	public static Map<String,Object> getParameterValueMap(HttpServletRequest request,
			boolean remainArray, boolean isSecure) {
		Map<String,Object> map = new HashMap<String,Object>();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement().toString();
			String[] values = request.getParameterValues(key);
			if (values != null)
				if (values.length == 1) {
					String tmpValue = values[0];
					if (tmpValue != null) {
						tmpValue = tmpValue.trim();
						if (!tmpValue.equals("")) {
							if (isSecure)
								tmpValue = filterInject(tmpValue);
							if (!tmpValue.equals(""))
								map.put(key, tmpValue);
						}
					}
				} else {
					String rtn = getByAry(values, isSecure);
					if (rtn.length() > 0) {
						if (remainArray)
							map.put(key, rtn.split(","));
						else
							map.put(key, rtn);
					}
				}
		}
		return map;
	}

	private static String getByAry(String[] aryTmp, boolean isSecure) {
		String rtn = "";
		for (int i = 0; i < aryTmp.length; i++) {
			String str = aryTmp[i].trim();
			if (!str.equals("")) {
				if (isSecure) {
					str = filterInject(str);
					if (!str.equals(""))
						rtn = new StringBuilder().append(rtn).append(str)
								.append(",").toString();
				} else {
					rtn = new StringBuilder().append(rtn).append(str)
							.append(",").toString();
				}
			}
		}
		if (rtn.length() > 0)
			rtn = rtn.substring(0, rtn.length() - 1);
		return rtn;
	}

	public static Locale getLocal(HttpServletRequest request) {
		Locale local = request.getLocale();
		if (local == null)
			local = Locale.CHINA;
		return local;
	}
}