/**
 * @(#)com.casic27.platform.base.cache.client.expire.CacheLogicExpireInterceptor.java
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
package com.casic27.platform.base.cache.client.expire;


import java.util.List;
import java.util.Set;

import com.casic27.platform.base.cache.client.AbstractCacheInterceptor;
import com.casic27.platform.base.cache.entity.CacheObject;

/**
 * 缓存逻辑过期拦截器，用于拦截缓存数据的获取
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
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
