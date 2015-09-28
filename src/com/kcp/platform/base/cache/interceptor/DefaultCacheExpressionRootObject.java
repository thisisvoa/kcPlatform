package com.kcp.platform.base.cache.interceptor;

import org.springframework.util.Assert;

/**
 * 
 * 
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
