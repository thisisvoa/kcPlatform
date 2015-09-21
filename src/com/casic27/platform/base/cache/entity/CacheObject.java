/**
 * @(#)com.casic27.platform.base.cache.entity.CacheObject.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.entity;

import java.io.Serializable;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	缓存对象必须实现的接口
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public interface CacheObject<T> extends Serializable{

    /**
     * 默认缓存项的存活时间,单位为秒 （一个月）
     */
    int DEFAUL_CACHE_TIME_TO_LIVE = 30*24*3600;
    /**
     * 获取缓存的对象
     * @return
     */
    T getValue();

    /**
     * 设置缓存的对象
     * @param value
     */
    void setValue(T value);


    /**
     * 设置版本号
     * @return
     */
    void setVersion(int version);

    /**
     * 获取版本号
     * @return
     */
    int getVersion();

    /**
     * 在远程缓存中的存活时间，单位为秒，负数或0表示永不过期
     * @return
     */
    int getCacheTimetoLive();

    /**
     * 获取缓存更改时间，单位为毫秒
     * @return
     */
    long getCacheUpdateTime();

    /**
     * 设置缓存更新时间
     * @param  cacheUpdateTime   缓存更改时间，单位为毫秒
     */
    void setCacheUpdateTime(long cacheUpdateTime);
}