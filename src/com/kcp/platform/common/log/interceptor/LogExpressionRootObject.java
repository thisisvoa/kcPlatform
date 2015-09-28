package com.kcp.platform.common.log.interceptor;


/**
 * 缓存注解表达式解析对象
 * 
 */
interface LogExpressionRootObject {

	/**
	 * 缓存方法名称
	 * 
	 * @return name of the cached method.
	 */
	String getMethodName();
}
