/**
 * @(#)com.casic27.platform.base.cache.annotation.CacheAnnotationParser.java
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
package com.casic27.platform.base.cache.annotation;

import java.lang.reflect.AnnotatedElement;

import com.casic27.platform.base.cache.interceptor.MethodCacheDefinition;

/**
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Nov 8, 2011
 */
public interface CacheAnnotationParser {
	/**
	 * 用于将注解信息转化为MethodCacheDefinition类型，便于进行解析
	 * @param ae
	 * @return
	 */
	MethodCacheDefinition parseCacheAnnotation(AnnotatedElement ae);
}
