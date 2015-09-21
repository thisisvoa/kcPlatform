/**
 * @(#)com.casic27.platform.base.cache.exception.CacheAnnParseException.java
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

import com.casic27.platform.core.exception.SystemException;

/**
 * 日志注解解析错误异常
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class CacheAnnParseException extends SystemException {
	
	private static final long serialVersionUID = 1L;

	public CacheAnnParseException(){
		super();
	}
	
	public CacheAnnParseException(String key){
		super(key);
	}
	
	public CacheAnnParseException(String key, Object[] args){
		super(key, args);
	}
	
	public CacheAnnParseException(Throwable cause){
		super(cause);
	}
	
	public CacheAnnParseException(String key, Throwable cause){
		super(key, cause);
	}
	
	public CacheAnnParseException(String key, Object[] args, Throwable cause){
		super(key, args, cause);
	}
}
