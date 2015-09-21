package com.casic27.platform.common.log.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class OperateLogAnnParser {
	private static String NON_ANN = "NON_ANN";
	/**
	 * 注解缓存
	 */
	private Map<String, Object> methodCache = new ConcurrentHashMap<String,Object>();;
	
	/**
	 * 判断某个方法上是否存在有Pageable注解
	 * @param className
	 * @param methodName
	 * @return
	 */
	public boolean hasOperateLogAnn(String className, String methodName){
		String key = className + "." + methodName;
		Object ann = methodCache.get(key);
		if(ann == null){
			try {
				Class cls = Class.forName(className);
				Method[] methodArray = cls.getMethods();
				Method method = findMethod(methodName, methodArray);
				if(method!=null){
					Method mtd = (Method)method;
					OperateLog operateLog = mtd.getAnnotation(OperateLog.class);
					if(operateLog==null){
						methodCache.put(key, NON_ANN);
						return false;
					}else{
						methodCache.put(key, operateLog);
						return true;
					}
				}else{
					methodCache.put(key, NON_ANN);
					return false;
				}
			} catch (ClassNotFoundException e) {
				methodCache.put(key, NON_ANN);
				return false;
			}
			
		}else if(NON_ANN.equals(ann)){
			return false;
		}else{
			if(ann instanceof OperateLog){
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	public OperateLog getOperateLogAnn(String className, String methodName){
		String key = className + "." + methodName;
		Object ann = methodCache.get(key);
		if(ann == null){
			try {
				Class cls = Class.forName(className);
				Method[] methodArray = cls.getMethods();
				Method method = findMethod(methodName, methodArray);
				if(method!=null){
					Method mtd = (Method)method;
					OperateLog operateLog = mtd.getAnnotation(OperateLog.class);
					if(operateLog==null){
						methodCache.put(key, NON_ANN);
						return null;
					}else{
						methodCache.put(key, operateLog);
						return operateLog;
					}
				}else{
					methodCache.put(key, NON_ANN);
					return null;
				}
			} catch (ClassNotFoundException e) {
				methodCache.put(key, NON_ANN);
				return null;
			}
			
		}else if(NON_ANN.equals(ann)){
			return null;
		}else{
			if(ann instanceof OperateLog){
				return (OperateLog)ann;
			}else{
				return null;
			}
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
