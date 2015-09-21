/**
 * @(#)com.casic27.platform.base.cache.itemmanager.CacheItemManager.java
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
package com.casic27.platform.base.cache.itemmanager;

import com.casic27.platform.base.cache.client.CacheClient;


public interface CacheItemManager extends CacheItemIntrodution,CacheItemAccessor,CacheItemRefresh {
	/**
	 * key与分组名的分隔符
	 */
	String KEY_SPLITER = "#";
	/**
	 * 初始化
	 */
	void initialize();
    /**
     * 设置使用的缓存客户端
     * @param cacheClient
     */
    void setCacheClient(CacheClient cacheClient);
    
    /**
     * 获取使用的缓存客户端
     * @param cacheClient
     */
    CacheClient getCacheClient();
}
