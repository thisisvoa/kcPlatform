/**
 * @(#)com.casic27.platform.base.log.interceptor.MethodSysLogDefinition.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.log.interceptor;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class MethodSysLogDefinition {
	/**
	 * 日志级别
	 */
	private String moduleName;
	
	/**
	 * 日志操作类型
	 */
	private String type;
	
	/**
	 * 日志操作描述
	 */
	private String operateDesc;
	
	/**
	 * 是否使用SPEL注解解析日志描述信息
	 */
	private boolean useSpel;
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getOperateDesc() {
		return operateDesc;
	}
	
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}
	
	public boolean isUseSpel() {
		return useSpel;
	}
	
	public void setUseSpel(boolean useSpel) {
		this.useSpel = useSpel;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
