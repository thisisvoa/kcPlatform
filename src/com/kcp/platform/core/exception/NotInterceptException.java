package com.kcp.platform.core.exception;

/**
*
*类描述：若某个业务操作出异常时候，在Controller不进行拦截，直接抛出异常
* 
*@Version：1.0
*/
public class NotInterceptException extends BaseException {
	
	public NotInterceptException(){
		super();
	}
	
	public NotInterceptException(String errKey){
		super(errKey);
	}
	
	public NotInterceptException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public NotInterceptException(Throwable cause){
		super(cause);
	}
	
	public NotInterceptException(String errKey, Throwable cause){
		super(errKey, cause);
	}
	
	public NotInterceptException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
