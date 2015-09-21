/**
 * @(#)com.casic27.platform.base.cache.interceptor.CacheExpressionRootObject.java
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
package com.casic27.platform.common.log.interceptor;


/**
 * 缓存注解表达式解析对象
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
interface LogExpressionRootObject {

	/**
	 * 缓存方法名称
	 * 
	 * @return name of the cached method.
	 */
	String getMethodName();
}
