package com.kcp.platform.util;

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
