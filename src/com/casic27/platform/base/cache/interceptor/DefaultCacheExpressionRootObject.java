/**
 * @(#)com.casic27.platform.base.cache.interceptor.DefaultCacheExpressionRootObject.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.interceptor;

import org.springframework.util.Assert;

/**
 * 
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class DefaultCacheExpressionRootObject implements CacheExpressionRootObject {

	private final String methodName;

	public DefaultCacheExpressionRootObject(String methodName) {
		Assert.hasText(methodName, "method name is required");
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}
}
