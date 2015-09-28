package com.kcp.platform.base.cache.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.kcp.platform.base.cache.interceptor.MethodCacheDefinition;

/**
 * 方法缓存注解解析器，将注解配置信息解析为<link>com.kcp.platform.base.cache.interceptor.MethodCacheDefinition</link>对象
 * 
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
