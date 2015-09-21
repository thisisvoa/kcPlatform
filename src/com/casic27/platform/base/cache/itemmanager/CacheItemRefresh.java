/**
 * @(#)com.casic27.platform.base.cache.itemmanager.CacheItemRefresh.java
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

/**
 * 缓存项刷新接口，所有实现该接口的Bean CacheItemManager，将会在界面上生成刷新按钮
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Dec 6, 2011
 */
public interface CacheItemRefresh {
	/**
	 * 刷新所有
	 */
	void refreshAll();
	
	/**
	 * 按缓存数据的key刷新缓存
	 * @param key
	 */
	void refresh(String key);
	
	/**
	 * 按缓存数据的keys刷新缓存
	 * @param keys
	 */
	void refresh(Set<String> items);
	
	/**
	 * 根据传入的正则表达式，刷新key匹配该表达式的缓存数据
	 * @param scopeName 用于标识该刷新命令,不同的正则该标识必须唯一否则会被后发布的命令覆盖
	 * @param regexp
	 */
	void refreshByRegexp(String scopeName, String regexp);
	
	/**
     * 是否需要刷新
     * @param refreshable
     */
    boolean refreshable();
}
