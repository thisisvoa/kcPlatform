package com.kcp.platform.base.cache.client;

import java.util.List;

import com.kcp.platform.base.cache.client.expire.CacheLogicExpireManager;
import com.kcp.platform.base.cache.client.expire.CacheSpace;
import com.kcp.platform.base.cache.client.expire.LogicExpireCommand;
import com.kcp.platform.base.cache.entity.CacheObject;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	缓存客户端
 *</pre> 
 *@Version：1.0
 */
@SuppressWarnings("unchecked")
public interface CacheClient {
	/**
	 * 判断缓存中是否存在某个索引为Key的对象
	 * @param key
	 * @return
	 */
	boolean containsKey(String key);
	
	/**
     * 添加到缓存中,缓存数据默认存活时间分别由实现者决定.
     * @param key
     * @param value
     * @see CacheObjectUtils#getKey(Class, String) :获取领域类对应的缓存键值
     */
     void  put(String key,CacheObject value);

    /**
     * 从缓存中获取数据
     * @param key
     * @return
     */
    CacheObject get(String key);

    /**
     * 从缓存中获取数据
     * @param key
     * @param requiredType   返回对象的类型
     * @param <T>
     * @return
     */
     <T> CacheObject<T> get(String key,Class<T> requiredType);
     
     
     /**
      *   删除缓存中的数据
      * @param key
      */
      boolean remove(String key);

     /**
      * 添加拦截器
      * @param cacheInterceptor
      */
     void addCacheInterceptor(CacheInterceptor cacheInterceptor);


      /**
      *  发布一个缓存过期的命令，以便批量过期缓存，会覆盖 命令组名+命令名相同时的命令
      * @param cacheScope
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
      * 设置一个缓存逻辑过期管理器
      * @param cacheLogicExpireManager
      */
      void setCacheLogicExpireManager(CacheLogicExpireManager cacheLogicExpireManager);
      
      /**
       * 为缓存逻辑过期管理器添加支持的缓存空间
       */
      void addCacheSpace(CacheSpace cacheSpace);
}
