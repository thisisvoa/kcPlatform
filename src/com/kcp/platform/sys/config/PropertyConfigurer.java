package com.kcp.platform.sys.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 类描述：读取系统配置文件，并提供相应的配置项读取接口
 * 
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
	private static PropertyConfigurer instance = new PropertyConfigurer();
	private static Properties properties;

	private PropertyConfigurer() {
		super();
	}
	
	/**
	 * 单例的PropertyConfigurer实现
	 * @return PropertyConfigurer实例
	 */
	public static PropertyConfigurer getInstance() {
		return instance;
	}

	/**
	 * 获取配置项目的值
	 * 
	 * @param key 配置项名称
	 * @return 配置项值
	 */
	public static String getValue(String key) {
		String value = null;
		try {
			if (properties == null) {
				properties = instance.mergeProperties();
			}
			value = properties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取配置项的值,配置项不存在时返回默认值
	 * 
	 * @param key 配置项名称
	 * @param defaultValue 默认值
	 * @return 配置项值
	 */
	public static String getValue(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 获取整形的配置数据
	 * 
	 * @param key 配置项名称
	 * @return 转化为int类型的配置项值
	 */
	public static int getIntValue(String key) {
		String value = getValue(key);
		int iV = 0;
		if (value != null && value.matches("^\\d+$")) {
			iV = Integer.parseInt(value);
		}
		return iV;
	}

	/**
	 * 获取整形的配置数据
	 * 
	 * @param key 配置项名称
	 * @param defaultValue 默认值
	 * @return 转化为int类型的配置项值
	 */
	public static int getIntValue(String key, int defaultValue) {
		String value = getValue(key);
		int iV = 0;
		if (value != null && value.matches("^\\d+$")) {
			iV = Integer.parseInt(value);
		} else {
			iV = defaultValue;
		}
		return iV;
	}

	/**
	 * 返回布尔型的配置项
	 * 
	 * @param key 配置项名称
	 * @return 转化为布尔型的配置项值
	 */
	public static boolean getBooleanValue(String key) {
		String value = getValue(key);
		boolean bV = false;
		if (value != null && value.matches("(?i)^(true|false)$")) {
			bV = Boolean.parseBoolean(value);
		}
		return bV;
	}

	/**
	 * 返回布尔型的配置项
	 * 
	 * @param key 配置项名称
	 * @param defaultValue 默认值
	 * @return 转化为布尔型的配置项值
	 */
	public static boolean getBooleanValue(String key, boolean defaultValue) {
		String value = getValue(key);
		boolean bV = false;
		if (value != null && value.matches("(?i)^(true|false)$")) {
			bV = Boolean.parseBoolean(value);
		} else {
			bV = defaultValue;
		}
		return bV;
	}

	/**
	 * 重加载配置信息
	 */
	public static void reload() {
		try {
			properties = instance.mergeProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
