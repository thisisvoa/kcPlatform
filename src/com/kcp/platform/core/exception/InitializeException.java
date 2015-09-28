package com.kcp.platform.core.exception;

/**
 *
 *类描述：组件生命周期异常
 * 
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
