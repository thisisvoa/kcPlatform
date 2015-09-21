/**
 * @(#)com.casic27.platform.base.cache.client.expire.SimpleCacheLogicExpireManager.java
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

import org.springframework.util.Assert;

import com.casic27.platform.base.cache.client.CacheClient;
import com.casic27.platform.base.cache.entity.CacheObject;
import com.casic27.platform.base.cache.exception.InvalidCommandException;
import com.casic27.platform.base.cache.util.CacheObjectUtils;


import java.util.*;


/**
 * 类描述：
 * 基于缓存存储缓存逻辑过期命令的缓存过期管理器
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
@SuppressWarnings("unchecked")
public class SimpleCacheLogicExpireManager implements CacheLogicExpireManager {

    /**
     * key为缓存空间名，值为缓存空间对象
     */
    private Map<String,CacheSpace> cacheSpaceMap = new HashMap<String,CacheSpace>();

    private CacheClient cacheClient;
    
    /**
     * 发布逻辑过期命令
     * @param command 逻辑过期命令
     */
    public void issueCommand(LogicExpireCommand command) {
        assertValidCommand(command);
        Map<String, LogicExpireCommand> commandMap = getCommandMap(command.getCacheScope().getSpaceName());
        CacheSpace commandCacheSpace = getCacheSpace(command.getCacheScope().getSpaceName());
        if (commandMap == null) {
            commandMap = new CacheSpaceCommandMap(commandCacheSpace.getMaxScopeAmount());
            Collections.synchronizedMap(commandMap);
        }
        //已经包含有存在的相同命令移除
        if(commandMap.containsKey(command.getCacheScope().getScopeName())){
             commandMap.remove(command.getCacheScope().getScopeName());
        }
        commandMap.put(command.getCacheScope().getScopeName(), command);

        putCommandToCache(command.getCacheScope().getSpaceName(), commandMap);
    }

    private CacheSpace getCacheSpace(String spaceName) {
        return cacheSpaceMap.get(spaceName);
    }

    private void assertValidCommand(LogicExpireCommand command) {
        if(!cacheSpaceMap.containsKey(command.getCacheScope().getSpaceName())){
            throw new InvalidCommandException("缓存空间"+command.getCacheScope().getSpaceName()+"不存在");
        }
    }

    private void putCommandToCache(String spaceName, Map<String, LogicExpireCommand> scopeCommandMap) {
        cacheClient.put(getSpaceCommandKey(spaceName),
                                   CacheObjectUtils.asCacheObject(scopeCommandMap));
    }

    public Set<CacheSpace> ofCacheSpaces(String cacheKey) {
        Set<CacheSpace> tempCacheSpaces = new HashSet<CacheSpace>();
        for (CacheSpace cacheSpace : cacheSpaceMap.values()) {
            if (cacheSpace.containCache(cacheKey)) {
                tempCacheSpaces.add(cacheSpace);
            }
        }
        return tempCacheSpaces;
    }

    public void removeCommand(String spaceName) {
        CacheSpace cacheSpace = cacheSpaceMap.get(spaceName);
        if(cacheSpace != null){
            getCacheClient().remove(getSpaceCommandKey(spaceName));
        }
    }

    private Map<String,LogicExpireCommand> getCommandMap(String spaceName){
        CacheObject cacheable = null;
        cacheable = getCacheClient().get(getSpaceCommandKey(spaceName));
        if(cacheable != null){
            return (Map<String,LogicExpireCommand>)cacheable.getValue();
        }else{
            return null;
        }
    }

    public List<LogicExpireCommand> getCommands(String spaceName) {
        Map<String, LogicExpireCommand> commandMap = getCommandMap(spaceName);
        if(commandMap != null){
           List commands =  new ArrayList<LogicExpireCommand>();
           commands.addAll(commandMap.values());
           return commands;
        }else{
            return null;
        }
    }

    public void removeCommand(String spaceName, String scopeName) {
        Map<String, LogicExpireCommand> commandMap = getCommandMap(spaceName);
        if(commandMap != null){
            commandMap.remove(scopeName);
            putCommandToCache(spaceName, commandMap);
        }
    }

    /**
     * 获取
     * @param spaceName
     * @return
     */
    private String getSpaceCommandKey(String spaceName){
        return new StringBuffer(LOGIC_EXPIRE_COMMAND_OF_SPACE_KEY_PREFIX).append(spaceName).toString();
    }

    /**
     *   设置该管理器所支持的缓存空间
     * @param cacheSpaces
     */
    public void setCacheSpaces(List<CacheSpace> cacheSpaces) {
        Set<CacheSpace> cacheSpaceSet = new HashSet<CacheSpace>();
        cacheSpaceSet.addAll(cacheSpaces);
        if(cacheSpaceSet.size() != cacheSpaces.size()){
            throw new IllegalArgumentException("命令组重复，命令组名必须唯一。");
        }else{
            for (CacheSpace cacheSpace : cacheSpaceSet) {
                 this.cacheSpaceMap.put(cacheSpace.getSpaceName(), cacheSpace);
            }
        }
    }

    /**
     * 通过一个Map设置缓存空间
     * @param cacheSpaceList
     */
    public void setCacheSpaceList(List<String> cacheSpaceList){
        List<CacheSpace> tempCacheSpaces = new ArrayList<CacheSpace>();
        for (String itemStr : cacheSpaceList) {
            tempCacheSpaces.add(CacheSpace.valueOf(itemStr));
        }
        setCacheSpaces(tempCacheSpaces);
    }
    
    /**
     * 为该逻辑过滤管理器，添加一个缓存空间
     * @param cacheSpace
     */
    public void addCacheSpace(CacheSpace cacheSpace){
    	cacheSpaceMap.put(cacheSpace.getSpaceName(), cacheSpace);
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }


    public CacheClient getCacheClient() {
        Assert.notNull(cacheClient,
                "cacheClient未初始化，请先调用setCacheClient(CacheClient cacheClient)初始化。");
        return cacheClient;
    }

}
