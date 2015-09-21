/**
 * @(#)com.casic27.platform.base.cache.itemmanager.CacheItemAccessor.java
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
 * 缓存项数据访问接口
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Dec 6, 2011
 */
public interface CacheItemAccessor {
	/**
     * 从缓存中获取一个对象
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 将一个对象添加到缓存中
     * @param key 键名
     * @param obj 缓存对象
     */
    void put(String key, Object obj);
    
    /**
     * 删除多个对象
     * @param keys
     */
    void remove(Set<String> keys);
}
