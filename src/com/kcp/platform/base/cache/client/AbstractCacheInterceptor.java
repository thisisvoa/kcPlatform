
package com.kcp.platform.base.cache.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kcp.platform.base.cache.entity.CacheObject;


/**
 * 抽象的缓存拦截器
 * 
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
@SuppressWarnings("unchecked")
public class AbstractCacheInterceptor implements CacheInterceptor{
	/*日志记录器*/
    protected Log logger = LogFactory.getLog(this.getClass());
    
	public boolean preGet(String key, CacheObject cacheable) {
        return true;
    }
	
    public void postGet(String key, CacheObject cacheable) {
    }

    public void prePut(String key, CacheObject cacheable) {
    }

    public void preRemove(String key) {
    }

    public int getOrderNo() {
        return Integer.MAX_VALUE;
    }

    public boolean isMatch(String key) {
        return true;
    }
}
