package com.kcp.platform.base.cache.client.expire;

import java.util.List;
import java.util.Set;

import com.kcp.platform.base.cache.client.CacheClient;

/**
 * 负责进行缓存逻辑过期处理的管理器
 * 
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public interface CacheLogicExpireManager {
	
	static final String LOGIC_EXPIRE_COMMAND_OF_SPACE_KEY_PREFIX = "@@SPACE_COMMAND/";
    /**
     *  发布一个缓存过期的命令，以便批量过期缓存，会覆盖 命令组名+命令名相同时的命令
     * @param command
     */
     void issueCommand(LogicExpireCommand command);

    /**
     * 获取缓存空间的命令列表
     * @param spaceName
     * @return
     */
     List<LogicExpireCommand> getCommands(String spaceName);

    /**
     * 删除指定缓存空间的所有逻辑过期命令
     * @param spaceName
     */
     void removeCommand(String spaceName);

    /**
     * 删除指定缓存区域的所有逻辑过期命令
     * @param spaceName
     * @param scopeName
     */
     void removeCommand(String spaceName, String scopeName);

    /**
     *   获取对应cacheKey的缓存空间
     * @param cacheKey
     * @return
     */
     Set<CacheSpace> ofCacheSpaces(String cacheKey);
     
     /**
      * 为该逻辑过滤管理器，添加一个缓存空间
      * @param cacheSpace
      */
     public void addCacheSpace(CacheSpace cacheSpace);
     
    /**
     * 获取缓存客户端
     * @return
     */
     CacheClient getCacheClient();

    /**
     * 设置缓存客户端对象
     * @param cacheClient
     */
     void setCacheClient(CacheClient cacheClient);
}
