package com.kcp.platform.base.cache.client.expire;

import java.util.regex.Pattern;
/**
 * 正则规则的缓存区域，缓存空间中缓存键符合该正则规则的缓存数据都为该缓存区域的值
 */
public class RegexpFilterCacheScope extends AbstractCacheScope{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String regexp;
    public RegexpFilterCacheScope(String spaceName, String scopeName, String regexp) {
        super(spaceName, scopeName);
        this.regexp = regexp;
    }

    public boolean contains(String cacheKey) {
        return Pattern.matches(this.regexp,cacheKey);
    }
}
