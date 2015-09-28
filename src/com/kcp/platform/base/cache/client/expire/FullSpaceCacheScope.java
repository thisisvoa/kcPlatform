package com.kcp.platform.base.cache.client.expire;

/**
 *  包含整个缓存空间的缓存区域
 *  功能说明：
 */
public class FullSpaceCacheScope extends  AbstractCacheScope{

	private static final long serialVersionUID = 1958193421959662105L;

	public FullSpaceCacheScope(String spaceName, String scopeName) {
        super(spaceName, scopeName);
    }
}
