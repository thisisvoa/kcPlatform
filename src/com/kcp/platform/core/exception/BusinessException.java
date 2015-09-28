package com.kcp.platform.core.exception;

/**
 *
 *类描述：业务异常
 * 
 *@Version：1.0
 */
public class BusinessException extends BaseException {
	
	private static final long serialVersionUID = 3540618017625966130L;
	
	public BusinessException(){
		super();
	}
	
	public BusinessException(String errKey){
		super(errKey);
	}
	
	public BusinessException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public BusinessException(Throwable cause){
		super(cause);
	}
	
	public BusinessException(String errKey, Throwable cause){
		super(errKey, cause);
	}
	
	public BusinessException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
