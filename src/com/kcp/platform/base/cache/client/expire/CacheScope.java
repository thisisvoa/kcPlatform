
package com.kcp.platform.base.cache.client.expire;

import java.io.Serializable;

/**
 * 
 * 类描述：
 * 	缓存区域，一个缓存空间可以包含无限个缓存区域，通过{@link #contains(String)}判断缓存键是否属于该缓存区域。
 * 
 */
public interface CacheScope extends Serializable{

    /**
     * 获取缓存空间名
     * @return 缓存空间名
     * @see CacheSpace
     */
    String getSpaceName();

    /**
     * 缓存区域名，在缓存空间中，必须保持唯一
     * @return
     */
    String getScopeName();

    /**
     * 判断缓存空间中的缓存路径是否属于该缓存区域
     * @param cacheKey
     * @return
     */
    boolean contains(String cacheKey);
}
