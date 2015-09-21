/**
 * @(#)com.casic27.platform.base.cache.client.ehcache.EhCacheClient.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.client.ehcache;

import com.casic27.platform.base.cache.client.AbstractCacheClient;
import com.casic27.platform.base.cache.entity.CacheObject;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	基于EhCache的缓存客户端
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
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
