package com.kcp.platform.sys.initialize;

import com.kcp.platform.core.exception.InitializeException;



/**
 *
 *类描述：
 *<br>组件启动接口；
 *<br>实现此接口的组件在系统启动时候将会进行调用initialize()方法;
 * 
 */
public interface Initializer {
	/**
     * 执行初始化操作
	 * @throws InitializeException
	 */
	public void initialize() throws InitializeException;
	
	/**
     * 获取该初始化器的描述，eg:系统监控、系统缓存...
     * @return
     */
    public String getDesc();
}
