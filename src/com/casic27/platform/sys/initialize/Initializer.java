/**
 * @(#)com.casic27.platform.sys.initialize.Initializer.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.sys.initialize;

import com.casic27.platform.core.exception.InitializeException;



/**
 *
 *类描述：
 *<br>组件启动接口；
 *<br>实现此接口的组件在系统启动时候将会进行调用initialize()方法;
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
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
