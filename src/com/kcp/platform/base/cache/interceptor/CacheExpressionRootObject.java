package com.kcp.platform.base.cache.interceptor;

/**
 * 缓存注解表达式解析对象
 * 
 */
interface CacheExpressionRootObject {

	/**
	 * 缓存方法名称
	 * 
	 * @return name of the cached method.
	 */
	String getMethodName();
}
