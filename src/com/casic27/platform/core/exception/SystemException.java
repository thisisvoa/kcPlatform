/**
 * @(#)com.casic27.platform.core.exception.SystemException.java
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
package com.casic27.platform.core.exception;

/**
 *
 *类描述：系统级的异常
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class SystemException extends BaseException {
	
	private static final long serialVersionUID = 4041970698515021292L;
	
	public SystemException(){
		super();
	}
	
	public SystemException(String errKey){
		super(errKey);
	}
	
	public SystemException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public SystemException(Throwable cause){
		super(cause);
	}
	
	public SystemException(String errKey, Throwable cause){
		super(errKey, cause);
	}
	
	public SystemException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
