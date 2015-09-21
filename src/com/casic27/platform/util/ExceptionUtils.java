/**
 * @(#)com.casic27.platform.util.ExceptionUtils.java
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

public class ExceptionUtils {

	/**
	 * 将CheckedException转换为UnCheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(e.getMessage(), e);
	}
}
