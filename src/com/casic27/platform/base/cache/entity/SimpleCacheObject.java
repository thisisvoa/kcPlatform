/**
 * @(#)com.casic27.platform.base.cache.entity.SimpleCacheObject.java
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
package com.casic27.platform.base.cache.entity;

/**
 * <pre>
 *   一个{@link CacheObject}接口的简单实现类
 * </pre>
 * @author 林斌树
 * @version 1.0
 */
public class SimpleCacheObject<T> implements CacheObject<T>{
    
	private static final long serialVersionUID = 565376779991029581L;
	
	private T value;
    
    private int version = 0;
    
    private long cacheUpdateTime;
    
    private int cacheTimetoLive = CacheObject.DEFAUL_CACHE_TIME_TO_LIVE ;

    public SimpleCacheObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
    }


    public long getCacheUpdateTime() {
        return cacheUpdateTime;
    }

    public void setCacheUpdateTime(long cacheUpdateTime) {
        this.cacheUpdateTime = cacheUpdateTime;
    }

	public int getCacheTimetoLive() {
		return cacheTimetoLive;
	}

	public void setCacheTimetoLive(int cacheTimetoLive) {
		this.cacheTimetoLive = cacheTimetoLive;
	}
}
