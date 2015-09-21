/**
 * @(#)com.casic27.platform.core.exception.BusinessException.java
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
 *类描述：业务异常
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
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
