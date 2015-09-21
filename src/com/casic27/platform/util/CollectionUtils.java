/**
 * @(#)com.casic27.platform.util.CollectionUtils.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-9
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

import java.util.Collection;
import java.util.Map;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
public class CollectionUtils {
	public static boolean isEmpty(Collection collection){
		if(collection == null || collection.isEmpty()){
			return true;
		}
		return false;
	}
	public static boolean isEmpty(Map map){
		if(map == null || map.isEmpty()){
			return true;
		}
		return false;
	}
}
