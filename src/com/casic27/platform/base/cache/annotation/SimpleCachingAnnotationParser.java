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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.casic27.platform.base.cache.interceptor.MethodCacheDefinition;

/**
 * 方法缓存注解解析器，将注解配置信息解析为<link>com.casic27.platform.base.cache.interceptor.MethodCacheDefinition</link>对象
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class SimpleCachingAnnotationParser implements CacheAnnotationParser,
		Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 转化方法的注解
	 */
	public MethodCacheDefinition parseCacheAnnotation(AnnotatedElement ae) {
		Cacheable update = findAnnotation(ae, Cacheable.class);
		if (update != null) {
			return parseCacheableAnnotation(ae,update);
		}
		CacheEvict invalidate = findAnnotation(ae, CacheEvict.class);
		if(invalidate!=null){
			return parseInvalidateAnnotation(ae,invalidate);
		}
		return null;
	}
	
	/**
	 * 根据注解类型，查找注解信息
	 * @param <T>
	 * @param ae
	 * @param annotationType
	 * @return
	 */
	private <T extends Annotation> T findAnnotation(AnnotatedElement ae, Class<T> annotationType) {
		T ann = ae.getAnnotation(annotationType);
		if (ann == null) {
			for (Annotation metaAnn : ae.getAnnotations()) {
				ann = metaAnn.annotationType().getAnnotation(annotationType);
				if (ann != null) {
					break;
				}
			}
		}
		return ann;
	}
	
	/**
	 * 转化cacheable注解
	 * @param target
	 * @param ann
	 * @return
	 */
	MethodCacheDefinition parseCacheableAnnotation(AnnotatedElement target, Cacheable ann) {
		MethodCacheDefinition mcd = new MethodCacheDefinition();
		mcd.setKey(ann.key());
		mcd.setType(MethodCacheDefinition.TYPE_CACHEABLE);
		mcd.setCacheItemManager(ann.cacheItemManager());
		return mcd;
	}
	
	/**
	 * 转化CacheEvict注解
	 * @param target
	 * @param ann
	 * @return
	 */
	MethodCacheDefinition parseInvalidateAnnotation(AnnotatedElement target, CacheEvict ann) {
		MethodCacheDefinition mcd = new MethodCacheDefinition();
		mcd.setKey(ann.key());
		mcd.setType(MethodCacheDefinition.TYPE_CACHEEVICT);
		mcd.setCacheItemManager(ann.cacheItemManager());
		mcd.setEvitAll(ann.evitAll());
		mcd.setRegexpScope(ann.regexpScope());
		return mcd;
	}
	
}
