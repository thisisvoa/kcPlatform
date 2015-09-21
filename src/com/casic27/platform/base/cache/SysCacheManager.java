/**
 * @(#)com.casic27.platform.base.cache.annotation.CacheAnnotationParser.java
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
package com.casic27.platform.base.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.cache.itemmanager.CacheItemManager;
import com.casic27.platform.sys.initialize.AbstractInitializer;
import com.casic27.platform.util.OrderableComparator;

/**
 * 系统缓存管理器,负责缓存项管理器的刷新。该类实现com.eic.sys.initializer.Initializer接口，
 * 在系统初始化时候init()方法就被调用
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Nov 15, 2011
 */
@Component("sysCacheManager")
public class SysCacheManager extends AbstractInitializer {
	
	
	@Autowired(required=false)
	private List<CacheItemManager> cacheItemManagerList = new ArrayList<CacheItemManager>();
	
	
	/**
	 * 缓存初始化 
	 */
	@Override
	public void init() {
		OrderableComparator.sort(cacheItemManagerList);
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager cacheItemManager = it.next();
			cacheItemManager.initialize();
		}
	}
	
	/**
	 * 获取系统中的缓存段管理器，供缓存刷新页面生成按钮用
	 * @return
	 */
	public Map<String,String> getManagersDescs(){
		Map<String,String>  itemDescs = new HashMap<String,String>();
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			itemDescs.put(manager.getName(), manager.getDesc());
		}
		return itemDescs;
	}
	
	public Map<String,String> getRefreshManagersDescs(){
		Map<String,String>  itemDescs = new HashMap<String,String>();
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.refreshable()){
				itemDescs.put(manager.getName(), manager.getDesc());
			}
		}
		return itemDescs;
	}
	/**
	 * 根据缓存项名称获取缓存
	 * @param name 缓存项管理器名称
	 * @return
	 */
	public CacheItemManager getCacheManagerByName(String name){
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.getName().equalsIgnoreCase(name)){
				return manager;
			}
		}
		return null;
	}
	
	/**
	 * 刷新所有缓存项管理器
	 */
	public void refreshAll(){
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.refreshable()){
				manager.refreshAll();
			}
			
		}
	}
	
	/**
	 * 根据缓存项管理器名称刷新缓存
	 * @param name
	 */
	public void refreshByManagerName(String name){
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.getName().equalsIgnoreCase(name)){
				manager.refreshAll();
				break;
			}
		}
	}
	
	/**
	 * 刷新<code>name</code>为<code>itemName</code>的缓存段管理器内,关键字为key的数据
	 * @param cacheItemName
	 */
	public void refreshItem(String name, String key){
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.getName().equalsIgnoreCase(name)){
				manager.refresh(key);
				break;
			}
		}
	}
	
	/**
	 * 刷新缓存项的多个缓存值
	 * @param name
	 * @param keys
	 */
	public void refreshItems(String name, Set<String> keys){
		Iterator<CacheItemManager> it = cacheItemManagerList.iterator();
		while(it.hasNext()){
			CacheItemManager manager = it.next();
			if(manager.getName().equalsIgnoreCase(name)){
				manager.refresh(keys);
				break;
			}
		}
	}
	
	public String getDesc() {
		return "系统缓存";
	}

	
	public void setCacheItemManagerList(List<CacheItemManager> cacheItemManagerList) {
		this.cacheItemManagerList = cacheItemManagerList;
	}
	
	/**
     * 排序号，越小越在前面。当初始化器需要优先加载(存在依赖)的时候，就需要重写该方法
     * 
     * @return 默认返回整数最小值加1
     */
    public int getOrderNo(){
        return Integer.MIN_VALUE + 1;
    }
}
