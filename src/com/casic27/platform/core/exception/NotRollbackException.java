/**
 * @(#)com.casic27.platform.core.exception.NotRollbackException.java
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
 *类描述：若某个业务操作出异常时候，不回滚事务，则抛出该类异常
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class NotRollbackException extends BaseException {
	
	private static final long serialVersionUID = 7418441730022443475L;
	
	public NotRollbackException(){
		super();
	}
	
	public NotRollbackException(String errKey){
		super(errKey);
	}
	
	public NotRollbackException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public NotRollbackException(Throwable cause){
		super(cause);
	}
	
	public NotRollbackException(String errKey, Throwable cause){
		super(errKey, cause);
	}
	
	public NotRollbackException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
