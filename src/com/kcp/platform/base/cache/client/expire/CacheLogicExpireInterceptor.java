package com.kcp.platform.base.cache.client.expire;


import java.util.List;
import java.util.Set;

import com.kcp.platform.base.cache.client.AbstractCacheInterceptor;
import com.kcp.platform.base.cache.entity.CacheObject;

/**
 * 缓存逻辑过期拦截器，用于拦截缓存数据的获取
 * 
 */
@SuppressWarnings("unchecked")
public class CacheLogicExpireInterceptor extends AbstractCacheInterceptor{

    private CacheLogicExpireManager cacheLogicExpireManager;

    public boolean preGet(String key, CacheObject cacheable) {
        Set<CacheSpace> matchedCacheSpaces = cacheLogicExpireManager.ofCacheSpaces(key);
        if(matchedCacheSpaces != null){
            for (CacheSpace matchedCacheSpace : matchedCacheSpaces) {
                List<LogicExpireCommand> spaceCommands = cacheLogicExpireManager.getCommands(matchedCacheSpace.getSpaceName());
                if(spaceCommands != null){
                    for (LogicExpireCommand scopeCommand : spaceCommands) {
                        if(scopeCommand.getCacheScope().contains(key)){
                        	  if(cacheable==null) return true;
                              if(cacheable.getCacheUpdateTime() <= scopeCommand.getBeforeTime()){
                                  return false;
                              }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void setCacheLogicExpireManager(CacheLogicExpireManager cacheLogicExpireManager) {
        this.cacheLogicExpireManager = cacheLogicExpireManager;
    }

    public int getOrderNo() {
        return 0;
    }
}
