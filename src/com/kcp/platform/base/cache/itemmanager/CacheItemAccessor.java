package com.kcp.platform.base.cache.itemmanager;

import java.util.Set;

/**
 * 缓存项数据访问接口
 * 
 */
public interface CacheItemAccessor {
	/**
     * 从缓存中获取一个对象
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 将一个对象添加到缓存中
     * @param key 键名
     * @param obj 缓存对象
     */
    void put(String key, Object obj);
    
    /**
     * 删除多个对象
     * @param keys
     */
    void remove(Set<String> keys);
}
