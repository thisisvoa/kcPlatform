package com.kcp.platform.base.cache.client.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.kcp.platform.base.cache.client.AbstractCacheClient;
import com.kcp.platform.base.cache.entity.CacheObject;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	基于EhCache的缓存客户端
 *</pre> 
 *@Version：1.0
 */
@SuppressWarnings("unchecked")
public class EhCacheClient extends AbstractCacheClient {
	private Cache cache;
	
	public EhCacheClient(){
	}
	
	public EhCacheClient(Cache cache){
		this.cache = cache;
	}
	
	
	protected CacheObject getHandler(String key) {
		Element element = cache.get(key);
        CacheObject cacheable = null;
        if (element != null && !element.isExpired()) {
            cacheable = (CacheObject) element.getValue();
        }
		return cacheable;
	}

	protected void putHandler(String key, CacheObject value) {
		Element element = new Element(key, value);
		element.setTimeToLive(value.getCacheTimetoLive());
		cache.put(element);
	}
	
	protected boolean removeHandler(String key){
		return cache.remove(key);
	}
	
	
	public boolean containsKey(String key){
		return cache.isKeyInCache(key);
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}
