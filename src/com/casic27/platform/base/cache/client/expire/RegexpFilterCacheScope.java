/**
 * @(#)com.casic27.platform.base.cache.client.expire.RegexpFilterCacheScope.java
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

import java.util.regex.Pattern;
/**
 * 正则规则的缓存区域，缓存空间中缓存键符合该正则规则的缓存数据都为该缓存区域的值
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class RegexpFilterCacheScope extends AbstractCacheScope{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String regexp;
    public RegexpFilterCacheScope(String spaceName, String scopeName, String regexp) {
        super(spaceName, scopeName);
        this.regexp = regexp;
    }

    public boolean contains(String cacheKey) {
        return Pattern.matches(this.regexp,cacheKey);
    }
}
