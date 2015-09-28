package com.kcp.platform.base.cache.itemmanager;

import com.kcp.platform.base.cache.entity.CacheObject;

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
