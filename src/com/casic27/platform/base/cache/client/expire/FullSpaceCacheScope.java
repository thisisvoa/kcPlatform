/**
 * @(#)com.casic27.platform.base.cache.client.expire.FullSpaceCacheScope.java
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

/**
 *  包含整个缓存空间的缓存区域
 *  功能说明：
 */
public class FullSpaceCacheScope extends  AbstractCacheScope{

	private static final long serialVersionUID = 1958193421959662105L;

	public FullSpaceCacheScope(String spaceName, String scopeName) {
        super(spaceName, scopeName);
    }
}
