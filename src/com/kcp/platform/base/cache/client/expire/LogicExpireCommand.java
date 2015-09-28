
package com.kcp.platform.base.cache.client.expire;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 
 * 对保存在{@link CacheScope}中的缓存，如果其{@link com.kcp.platform.base.cache.entity.CacheObject#getCacheUpdateTime()}<={#getBeforeTime()}，
 * 都标识为逻辑过期.
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
