/**
 * @(#)com.casic27.platform.core.exception.InitializeException.java
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
package com.casic27.platform.core.exception;

/**
 *
 *类描述：组件生命周期异常
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class InitializeException extends SystemException {

	private static final long serialVersionUID = -6655141540249006892L;
	public InitializeException(){
		super();
	}
	
	public InitializeException(String errKey){
		super(errKey);
	}
	
	public InitializeException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public InitializeException(Throwable cause){
		super(cause);
	}
	
	public InitializeException(String errKey, Throwable cause){
		super(errKey, cause);
	}
		
	public InitializeException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
