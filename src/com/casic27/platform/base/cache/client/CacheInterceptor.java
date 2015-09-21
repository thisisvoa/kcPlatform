/**
 * @(#)com.casic27.platform.base.cache.client.CacheInterceptor.java
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
package com.casic27.platform.base.cache.client;

import com.casic27.platform.base.cache.entity.CacheObject;
import com.casic27.platform.util.Orderable;


/**
 * 缓存存取的拦截器接口
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
@SuppressWarnings("unchecked")
public interface CacheInterceptor extends Orderable{
    /**
     * <pre>
     *     在调用{@link CacheClient#get(String)}之前调用，如果返回false,直接返回空值，
     * 不从真实缓存获取真缓存值。
     *     应用示例：如果通过管理方式将一类缓存数据过期，那么可以编写一个拦截器，先根据控制数据判断
     * 缓存是否过期，如果过期，阻止向缓存管理器获取缓存直接返回false.
     * </pre>
     * @param key
     * @param cacheable 缓存中的对象
     * @return
     */
    boolean preGet(String key, CacheObject cacheable);

    /**
     *   在调用{@link CacheClient#get(String)}之后调用，对<code>cacaheable</code>进行加工处理，处理后的
     *cacheable会返回给调用者。
     * @param key
     * @param cacheable
     * @param <T>
     */
    void postGet(String key,CacheObject cacheable);

    /**
     *    在调用{@link CacheClient#put(String, CacheObject)}之前调用
     * @param key
     * @param cacheable
     * @param <T>
     * @return
     */
    void  prePut(String key,CacheObject cacheable);

    /**
     * 删除缓存
     * @param key
     */
    void preRemove(String key);

    /**
     * 判断该拦截器针对<code>key</code>是否匹配，只有匹配，拦截器的动作才会实施
     * @param key
     * @return
     */
    boolean isMatch(String key);
}
