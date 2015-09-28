package com.kcp.platform.base.cache.annotation;

import java.lang.reflect.AnnotatedElement;

import com.kcp.platform.base.cache.interceptor.MethodCacheDefinition;
public interface CacheAnnotationParser {
	/**
	 * 用于将注解信息转化为MethodCacheDefinition类型，便于进行解析
	 * @param ae
	 * @return
	 */
	MethodCacheDefinition parseCacheAnnotation(AnnotatedElement ae);
}
