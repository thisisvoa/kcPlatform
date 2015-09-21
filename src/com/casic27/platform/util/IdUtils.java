/**
 * @(#)com.casic27.platform.util.IdUtils.java
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

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @author calvin
 */
public abstract class IdUtils {

	private static SecureRandom random = new SecureRandom();

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成,中间有-分割
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return random.nextLong();
	}

	/**
	 * 基于Base62编码的随机生成Long.
	 */
	public static String randomBase62() {
		return EncodeUtils.encodeBase62(random.nextLong());
	}
}
