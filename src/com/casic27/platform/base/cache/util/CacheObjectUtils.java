/**
 * @(#)com.casic27.platform.base.cache.util.CacheObjectUtils.java
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
package com.casic27.platform.base.cache.util;

import org.springframework.util.Assert;

import com.casic27.platform.base.cache.entity.CacheObject;
import com.casic27.platform.base.cache.entity.SimpleCacheObject;

import java.io.Serializable;

/**
 * <pre>
 *     将一般类转换为Cacheable的工具类
 * </pre>
 *
 */
public class CacheObjectUtils {

    /**
     * 将value转换为{@link CacheObject}的对象
     *
     * @param value
     * @return
     */
    public static final <T> CacheObject<T> asCacheObject(T value) {
        return asCacheObject(value, CacheObject.DEFAUL_CACHE_TIME_TO_LIVE);
    }

    /**

    /**
     * 将value转换为{@link CacheObject}的对象
     *
     * @param value
     * @param cacheTimeToLive  缓存存活时间，单位为秒，0或负数表示永不过期
     * @return
     */
    public static final <T> CacheObject<T> asCacheObject(T value, int cacheTimetoLive) {
        Assert.isInstanceOf(Serializable.class,value,"必须继承Serializable接口");
        SimpleCacheObject<T> cacheable = new SimpleCacheObject<T>(value);
        cacheable.setCacheTimetoLive(cacheTimetoLive);
        return cacheable;
    }
}
