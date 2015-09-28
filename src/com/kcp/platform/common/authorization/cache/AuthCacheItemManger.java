package com.kcp.platform.common.authorization.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kcp.platform.base.cache.client.CacheClient;
import com.kcp.platform.base.cache.itemmanager.AbstractCacheItemManager;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Component("authCacheItemManger")
public class AuthCacheItemManger extends AbstractCacheItemManager {
	@Override
	protected void init() {

	}
	
	@Override
	public String getDesc() {
		return "用户权限缓存";
	}

	@Override
	public String getName() {
		return "$authorizationCache$";
	}

	@Override
	public String getSpace() {
		return "com.kcp.platform.common.authorization";
	}
	
	@Autowired
	@Qualifier("authCacheClient")
	public void setCacheClient(CacheClient cacheClient){
		this.cacheClient = cacheClient;
	}
}
