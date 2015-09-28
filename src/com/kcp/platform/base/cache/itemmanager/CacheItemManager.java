package com.kcp.platform.base.cache.itemmanager;

import com.kcp.platform.base.cache.client.CacheClient;


public interface CacheItemManager extends CacheItemIntrodution,CacheItemAccessor,CacheItemRefresh {
	/**
	 * key与分组名的分隔符
	 */
	String KEY_SPLITER = "#";
	/**
	 * 初始化
	 */
	void initialize();
    /**
     * 设置使用的缓存客户端
     * @param cacheClient
     */
    void setCacheClient(CacheClient cacheClient);
    
    /**
     * 获取使用的缓存客户端
     * @param cacheClient
     */
    CacheClient getCacheClient();
}
