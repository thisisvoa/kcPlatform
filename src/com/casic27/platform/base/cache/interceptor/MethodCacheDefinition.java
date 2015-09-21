/**
 * @(#)com.casic27.platform.base.cache.interceptor.MethodCacheDefinition.java
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
package com.casic27.platform.base.cache.interceptor;

/**
 * 被Aop拦截的方法的定义信息(注解中配置的信息)
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Nov 9, 2011
 */
public class MethodCacheDefinition {
	
	public static String TYPE_CACHEABLE = "cacheAble";
	
	public static String TYPE_CACHEEVICT = "cacheEvict";
	/**
	 * 类型
	 */
	private String type = "";
	
	/**
	 * key值
	 */
	private String key = "";
	
	/**
	 * 缓存项管理器Bean名称
	 */
	private String cacheItemManager;
	
	/**
	 * 是否清除整个分组
	 */
	private boolean evitAll;

	/**
	 * 清除缓存键符合该正则表达式的缓存数据
	 */
	private String regexpScope = "";
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public boolean isEvitAll() {
		return evitAll;
	}

	public void setEvitAll(boolean evitAll) {
		this.evitAll = evitAll;
	}

	public String getCacheItemManager() {
		return cacheItemManager;
	}

	public void setCacheItemManager(String cacheItemManager) {
		this.cacheItemManager = cacheItemManager;
	}

	public String getRegexpScope() {
		return regexpScope;
	}

	public void setRegexpScope(String regexpScope) {
		this.regexpScope = regexpScope;
	}

}
