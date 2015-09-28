package com.kcp.platform.core.exception;

/**
 *
 *类描述：若某个业务操作出异常时候，不回滚事务，则抛出该类异常
 * 
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
