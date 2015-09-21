/**
 * @(#)com.casic27.platform.base.cache.client.expire.LogicExpireCommand.java
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 
 * 对保存在{@link CacheScope}中的缓存，如果其{@link com.casic27.platform.base.cache.entity.CacheObject#getCacheUpdateTime()}<={#getBeforeTime()}，
 * 都标识为逻辑过期.
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class LogicExpireCommand implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CacheScope cacheScope;
    private long beforeTime;

    /**
     *
     * @param cacheScope 缓存区域
     * @param beforeTime
     */
    private LogicExpireCommand(CacheScope cacheScope, long beforeTime) {
        this.cacheScope = cacheScope;
        this.beforeTime = beforeTime;
    }

    /**
     *   该命令使<code>cacheScope</code>中的缓存，其缓存更新时间在<code>beforeTime</code>时间点之前的缓存都逻辑过期
     * @param cacheScope
     * @param beforeTime
     * @return
     */
    public static final LogicExpireCommand expireBefore(CacheScope cacheScope, long beforeTime){
        return new LogicExpireCommand(cacheScope, beforeTime);
    }

    /**
     *  该命令使<code>cacheScope</code>中的缓存，其缓存更新时间在当前时间点之前的缓存都逻辑过期
     * @param cacheScope
     * @return
     */
    public static final LogicExpireCommand expireBeforeCurr(CacheScope cacheScope){
        return new LogicExpireCommand(cacheScope,System.currentTimeMillis());
    }

    public CacheScope getCacheScope() {
        return cacheScope;
    }

    public long getBeforeTime() {
        return beforeTime;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.getCacheScope())
                .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof LogicExpireCommand) {
            LogicExpireCommand other = (LogicExpireCommand) obj;
            equals = new EqualsBuilder()
                    .append(this.getCacheScope(), other.getCacheScope())
                    .isEquals();
        }
        return equals;
    }

}
