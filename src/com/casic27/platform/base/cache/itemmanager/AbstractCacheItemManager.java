/**
 * @(#)com.casic27.platform.base.cache.itemmanager.AbstractCacheItemManager.java
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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casic27.platform.base.cache.client.CacheClient;
import com.casic27.platform.base.cache.client.expire.CacheScope;
import com.casic27.platform.base.cache.client.expire.CacheSpace;
import com.casic27.platform.base.cache.client.expire.FullSpaceCacheScope;
import com.casic27.platform.base.cache.client.expire.LogicExpireCommand;
import com.casic27.platform.base.cache.client.expire.RegexpFilterCacheScope;
import com.casic27.platform.base.cache.entity.CacheObject;
import com.casic27.platform.base.cache.util.CacheObjectUtils;
import com.casic27.platform.util.AssertUtils;
import com.casic27.platform.util.StringUtils;


@SuppressWarnings("unchecked")
public abstract class AbstractCacheItemManager implements CacheItemManager {
	
	/**
	 * 日志记录
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 缓存客户端
	 */
	protected CacheClient cacheClient;
	
	
	public void initialize() {
		logger.info("正在初始化["+getDesc()+"]缓存......");
		CacheSpace cacheSpace = new CacheSpace(this.getName(),this.getSpace()+"*");
		cacheClient.addCacheSpace(cacheSpace);
		init();
		logger.info("完成["+getDesc()+"]缓存初始化!");
	}
	
	abstract protected void init();
	
	public Object get(String key) {
		if(key==null){
			return null;
		}
		CacheObject cacheObject = cacheClient.get(getKey(key));
		if(cacheObject==null){
			return null;
		}
		return cacheObject.getValue();
	}
	
	public void put(String key, Object obj) {
		AssertUtils.notNull(key,"缓存数据KEY不能为空");
		cacheClient.put(getKey(key), CacheObjectUtils.asCacheObject(obj));
	}
	
	public void remove(Set<String> keys){
		if(keys==null) return;
		for(String key: keys){
			remove(key);
		}
	}
	
	public void remove(String key) {
		cacheClient.remove(getKey(key));
	}
	
	
	public void refresh(String key) {
		remove(key);
	}

	public void refresh(Set<String> items) {
		if(items==null) return;
		for(String item:items){
			remove(item);
		}
	}

	public void refreshAll() {
		logger.info("正在刷新["+getDesc()+"]缓存......");
		//发布命令，将该组下的缓存项封闭
		CacheScope cacheScope = new FullSpaceCacheScope(this.getName(), this.getSpace());
		LogicExpireCommand command = LogicExpireCommand.expireBeforeCurr(cacheScope);
		cacheClient.issueCommand(command);
		logger.info("完成["+getDesc()+"]缓存刷新......");
	}
	/**
	 * 按传入的正则表达式刷新缓存
	 */
	public void refreshByRegexp(String scopeName, String regexp){
		StringBuffer regexpSB = new StringBuffer(StringUtils.parseRegexpChar(getSpace()))
	 								.append(CacheItemManager.KEY_SPLITER)
	 								.append(regexp);
		
		CacheScope scope = new RegexpFilterCacheScope(this.getName(),scopeName,regexpSB.toString());
		LogicExpireCommand command = LogicExpireCommand.expireBeforeCurr(scope);
		cacheClient.issueCommand(command);
	}
	
	public boolean refreshable(){
		return true;
	}
	
	public void setCacheClient(CacheClient cacheClient){
		this.cacheClient = cacheClient;
	}
	
	public CacheClient getCacheClient(){
		return this.cacheClient;
	}
	/**
	 * 默认key为缓存项(name)+"#"+key
	 * @param key
	 * @return
	 */
	protected String getKey(String key){
		key = key.toUpperCase();
		StringBuffer sb = new StringBuffer();
		return sb.append(getSpace()).append(KEY_SPLITER).append(key.toUpperCase()).toString();
	}
}
