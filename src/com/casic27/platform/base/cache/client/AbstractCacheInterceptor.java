/**
 * @(#)com.casic27.platform.base.cache.client.AbstractCacheInterceptor.java
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
package com.casic27.platform.base.cache.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casic27.platform.base.cache.entity.CacheObject;


/**
 * 抽象的缓存拦截器
 * 
 * @author 林斌树 (linbinshu@casic27.com)
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
