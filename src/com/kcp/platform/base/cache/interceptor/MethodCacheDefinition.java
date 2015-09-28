package com.kcp.platform.base.cache.interceptor;

/**
 * 被Aop拦截的方法的定义信息(注解中配置的信息)
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
