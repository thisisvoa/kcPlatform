package com.kcp.platform.util;

import java.util.Collection;
import java.util.Map;

/**
 *
 *类描述：
 * 
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
