/**
 * @(#)com.casic27.platform.core.exception.ExpMsgHolder.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.exception;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;



/**
 *
 *类描述：
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class ExpMsgHolder extends PropertyPlaceholderConfigurer {
	private static Logger logger = Logger.getLogger(ExpMsgHolder.class);
	
	private static ExpMsgHolder instance = new ExpMsgHolder();
	private static Properties properties;

	private ExpMsgHolder() {
		super();
	}
	
	/**
	 * 单例的PropertyConfigurer实现
	 * @return PropertyConfigurer实例
	 */
	public static ExpMsgHolder getInstance() {
		return instance;
	}
	
	/**
	 * 根据异常代码获取异常信息
	 * @param key 异常代码
	 * @return 异常信息
	 */
	public static String getMessage(String key){
		String msg = null;
		try {
			if (properties == null) {
				synchronized (instance) {
					if(properties==null){
						properties = instance.mergeProperties();
					}
				}
			}
			msg = properties.getProperty(key);
		} catch (IOException e) {
			logger.error(e);
		}
		return msg;
	}
	
	/**
	 * 根据异常代码获取异常信息，若异常信息不存在返回默认值
	 * @param key 异常代码
	 * @param defaultValue
	 * @return
	 */
	public static String getMessage(String key, String defaultValue){
		String msg = getMessage(key);
		return msg==null?defaultValue:msg;
	}
	
	/**
	 * 根据异常代码获取异常信息，并将异常信息根据传入的参数进行格式化
	 * @param key 异常代码
	 * @param params 参数值
	 * @return
	 */
	public static String getMessage(String key, Object[] params){
		String msg = getMessage(key);
		if(null!=msg){
			msg = MessageFormat.format(key, params);
		}
		return msg;
	}
}
