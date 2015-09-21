/**
 * @(#)com.casic27.platform.base.cache.itemmanager.DefaultCacheItemManager.java
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
package com.casic27.platform.base.cache.itemmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.cache.client.CacheClient;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 * 默认缓存项目管理器
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
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
		return "com.casic27.platform";
	}
	
	@Autowired
	@Qualifier("defaultCacheClient")
	public void setCacheClient(CacheClient cacheClient){
		this.cacheClient = cacheClient;
	}
}
