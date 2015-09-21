/**
 * @(#)com.casic27.platform.base.cache.itemmanager.LoadByKeyCacheItemManager.java
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

import com.casic27.platform.base.cache.entity.CacheObject;

@SuppressWarnings("unchecked")
public abstract class LoadByKeyCacheItemManager extends AbstractCacheItemManager {
	
	protected abstract Object loadByKey(String key);
	
	/**
	 * 重写get方法，当在缓存中找不到键值为key的缓存数据时候，调用LoadByKeyCacheItemManager#loadByKey方法，根据该方法定义的逻辑获取缓存数据
	 */
	@Override
	public Object get(String key) {
		if(key==null){
			return null;
		}
		key = key.toUpperCase();
		CacheObject cacheObject = cacheClient.get(getKey(key));
		if(cacheObject==null){
			Object obj = null;
			//避免重复加载
			synchronized (this.getName()) {
				cacheObject = cacheClient.get(getKey(key));
				if(cacheObject==null){
					obj =  loadByKey(key);
					if(obj!=null){
						put(key, obj);
					}
				}
			}
			return obj;
		}else{
			return cacheObject.getValue();
		}
	}
}
