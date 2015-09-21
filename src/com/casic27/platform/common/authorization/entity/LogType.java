/**
 * @(#)com.casic27.platform.common.authorization.entity.LogType.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-14
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.entity;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
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
