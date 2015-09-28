package com.kcp.platform.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ValuesHandler {
	
	public static Integer parseString2Integer(String str) {
		Integer i = null;
		if (StringUtils.isNotBlank(str)) {
			i = Integer.valueOf(str);
		}
		return i;
	}
	
	public static Map<String, Object> convertFrom(Map<String, String> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> keySet = map.keySet();
		Iterator<String> keyIts = keySet.iterator();
		String key = null;
		String value = null;
		while (keyIts.hasNext()) {
			key = keyIts.next();
			value = map.get(key);
			result.put(key, value);
		}
		return result;
	}

}
