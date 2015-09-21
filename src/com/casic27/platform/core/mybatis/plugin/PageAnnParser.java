/**
 * @(#)com.casic27.platform.core.mybatis.plugin.PageAnnParser.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-7
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.mybatis.plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.casic27.platform.core.mybatis.annotation.Pageable;


public class PageAnnParser {
	/**
	 * 方法缓存
	 */
	private Map<String, Method> methodCache = new ConcurrentHashMap<String,Method>();;
	
	/**
	 * 判断某个方法上是否存在有Pageable注解
	 * @param className
	 * @param methodName
	 * @return
	 */
	public boolean hasPageAnn(String className, String methodName){
		String key = className + "." + methodName;
		Method method = methodCache.get(key);
		if(method==null){
			try {
				Class cls = Class.forName(className);
				Method[] methodArray = cls.getMethods();
				method = findMethod(methodName, methodArray);
				if(method!=null){
					methodCache.put(key, method);//对反射的方法做缓存
				}else{
					return false;
				}
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
		Pageable pageable= method.getAnnotation(Pageable.class);
		if(null==pageable){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 从方法数组中查找某个方法，注：因Mybatis Mapper中不能有多态，故不考虑多态的情况；
	 * @param methodName
	 * @param methodArray
	 * @return
	 */
	private Method findMethod(String methodName,Method[] methodArray){
		Method method = null;
		for(int i=0;i<methodArray.length;i++){
			Method mtd = methodArray[i];
			if(mtd.getName().equals(methodName)){
				method = mtd;
				
				break;
			}
		}
		
		return method;
	}
}
