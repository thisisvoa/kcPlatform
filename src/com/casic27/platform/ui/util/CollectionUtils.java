/**
 * @(#)com.casic27.platform.ui.util.CollectionUtils.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-6-21
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.util;

import java.util.List;

/**
 * <pre>
 *  类描述：
 * </pre>
 * 
 * <pre>
 * </pre>
 * 
 * @author 林斌树(linbinshu@casic27.com)
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
