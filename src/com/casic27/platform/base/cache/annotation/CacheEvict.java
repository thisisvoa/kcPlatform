/**
 * @(#)com.casic27.platform.base.cache.annotation.CacheEvict.java
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过期缓存数据注解，标注该注解的方法触发后将触发缓存数据的过期
 */
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
	/**
	 * 缓存Key值，支持spel表达式
	 * @return
	 */
	String key() default "";
	
	/**
	 * 是否更新整个分组
	 * @return
	 */
	boolean evitAll() default false;
	
	/**
	 * 使用的缓存项管理器Bean名称
	 * @return
	 */
	String cacheItemManager();
	
	/**
	 * 缓存键符合该正则表达式的缓存数据，进行过期
	 */
	String regexpScope() default "";
}