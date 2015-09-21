/**
 * @(#)com.casic27.platform.common.authorization.cache.AuthCacheItemManger.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-10
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.cache.client.CacheClient;
import com.casic27.platform.base.cache.itemmanager.AbstractCacheItemManager;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
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
		return "com.casic27.platform.common.authorization";
	}
	
	@Autowired
	@Qualifier("authCacheClient")
	public void setCacheClient(CacheClient cacheClient){
		this.cacheClient = cacheClient;
	}
}
