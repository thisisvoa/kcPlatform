
package com.kcp.platform.base.cache.client.expire;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class AbstractCacheScope implements CacheScope{

    private String spaceName;
    private String scopeName;

    public AbstractCacheScope(String spaceName, String scopeName) {
        this.spaceName = spaceName;
        this.scopeName = scopeName;
    }

    public String getSpaceName() {
        return this.spaceName;
    }

    public String getScopeName() {
        return this.scopeName;
    }

    public boolean contains(String cacheKey) {
        return true;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.getSpaceName())
                .append(this.getScopeName())
                .hashCode();
    }

    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof CacheScope) {
            CacheScope other = (CacheScope) obj;
            equals = new EqualsBuilder()
                    .append(this.getSpaceName(),other.getSpaceName())
                    .append(this.scopeName, other.getScopeName())
                    .isEquals();
        }
        return equals;
    }

}
