
package com.kcp.platform.base.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加入缓存注解，标注上该注解的方法，返回值将加入到缓存中
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
	/**
	 * 缓存Key值，支持spel表达式
	 * @return
	 */
	String key();
	
	/**
	 * 缓存项管理器Bean名称
	 * @return
	 */
	String cacheItemManager();
}
