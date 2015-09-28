package com.kcp.platform.common.log.interceptor;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
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
