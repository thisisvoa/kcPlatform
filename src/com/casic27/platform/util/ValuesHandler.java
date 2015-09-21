/**
 * @(#)com.casic27.platform.util.ValuesHandler.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

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
