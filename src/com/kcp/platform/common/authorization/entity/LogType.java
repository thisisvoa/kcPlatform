package com.kcp.platform.common.authorization.entity;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@kcp.com)
 */
public enum LogType {
	/**
	 * 登录日志类型
	 */
	LOGIN("1")
	
	/**
	 * 登出日志类型
	 */
	,LOGOUT("2");
	
	LogType(String value){
		this.value = value;
	}
	
	private String value;
	
	public String value() { return this.value; }
	
}
