/**
 * @(#)com.casic27.platform.base.cache.client.expire.AbstractCacheScope.java
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
package com.casic27.platform.base.cache.client.expire;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
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
