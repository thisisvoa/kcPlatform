package com.kcp.platform.base.cache.itemmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kcp.platform.base.cache.client.CacheClient;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 * 默认缓存项目管理器
 *</pre> 
 */
@Component("defaultCacheItemManager")
public class DefaultCacheItemManager extends AbstractCacheItemManager {

	@Override
	protected void init() {
		logger.info("默认缓存段管理器启动...");
		logger.info("默认缓存段管理器结束.");
	}

	public String getDesc() {
		return "默认缓存段管理器";
	}

	public String getName() {
		return "defaultCacheItem";
	}

	public String getSpace() {
		return "com.kcp.platform";
	}
	
	@Autowired
	@Qualifier("defaultCacheClient")
	public void setCacheClient(CacheClient cacheClient){
		this.cacheClient = cacheClient;
	}
}
