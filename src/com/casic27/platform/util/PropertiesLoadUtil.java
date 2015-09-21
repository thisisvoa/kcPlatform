/**
 * @(#)com.casic27.platform.util.PropertiesLoadUtil.java
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
package com.casic27.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoadUtil {

	public static String filePath="/conf/application.properties";
	private static  Properties properties=new Properties();
	public static String  getProValue(String key){
			return properties.getProperty(key);
		
	}
	static {
		//src目录下把'.'去掉，'.'表示当前类路径
		InputStream is =PropertiesLoadUtil.class.getResourceAsStream(filePath);
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		System.out.println(getProValue("wsdlLocation"));
	}

}
