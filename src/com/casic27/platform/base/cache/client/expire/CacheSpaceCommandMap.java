/**
 * @(#)com.casic27.platform.base.cache.client.expire.CacheSpaceCommandMap.java
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存命名存储Map
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class CacheSpaceCommandMap extends LinkedHashMap<String,LogicExpireCommand>{

	private static final long serialVersionUID = -3301829340871887574L;
	
	private int maxSize;

    public CacheSpaceCommandMap(int maxSize) {
        super(10);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, LogicExpireCommand> eldest) {
         return size() > maxSize;
    }
}
