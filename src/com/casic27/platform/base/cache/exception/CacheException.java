/**
 * @(#)com.casic27.platform.base.cache.exception.CacheException.java
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
package com.casic27.platform.base.cache.exception;

import com.casic27.platform.core.exception.BaseException;


public class CacheException extends BaseException {
	private static final long serialVersionUID = 1L;

	public CacheException(){
		super();
	}
	
	public CacheException(String key){
		super(key);
	}
	
	public CacheException(String key, Object[] args){
		super(key, args);
	}
	
	public CacheException(Throwable cause){
		super(cause);
	}
	
	public CacheException(String key, Throwable cause){
		super(key, cause);
	}
	
	public CacheException(String key, Object[] args, Throwable cause){
		super(key, args, cause);
	}
}
