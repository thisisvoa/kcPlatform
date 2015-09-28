package com.kcp.platform.common.log.interceptor;

import org.springframework.util.Assert;

/**
 * 
 * 
 */
public class DefaultLogExpressionRootObject implements LogExpressionRootObject {

	private final String methodName;

	public DefaultLogExpressionRootObject(String methodName) {
		Assert.hasText(methodName, "method name is required");
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}
}
