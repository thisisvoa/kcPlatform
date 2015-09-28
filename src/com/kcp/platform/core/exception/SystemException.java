package com.kcp.platform.core.exception;

/**
 *
 *类描述：系统级的异常
 * 
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
