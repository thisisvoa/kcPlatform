package com.kcp.platform.ui.util;

import java.util.List;

/**
 * <pre>
 *  类描述：
 * </pre>
 * 
 * <pre>
 * </pre>
 * 
 */
public class CollectionUtils {
	/**
	 * 类似于javascript中的join
	 * @param array
	 * @param conjunction
	 * @return
	 */
	public static String join(String[] array, String conjunction) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : array) {
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append(item);
		}
		return sb.toString();
	}
	
	public static String join(List<String> array, String conjunction){
		return CollectionUtils.join((String[])array.toArray(new String[0]), conjunction);
	}
	
	public static String join2Json(String[] array, String conjunction){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (String item : array) {
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append("'").append(item).append("'");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String join2Json(List<String> array, String conjunction){
		return CollectionUtils.join2Json((String[])array.toArray(new String[0]), conjunction);
	}
}
