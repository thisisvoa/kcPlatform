package com.kcp.platform.base.cache.client.expire;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存命名存储Map
 */
public class CacheSpaceCommandMap extends LinkedHashMap<String,LogicExpireCommand>{

	private static final long serialVersionUID = -3301829340871887574L;
	
	private int maxSize;

    public CacheSpaceCommandMap(int maxSize) {
        super(10);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, LogicExpireCommand> eldest) {
         return size() > maxSize;
    }
}
