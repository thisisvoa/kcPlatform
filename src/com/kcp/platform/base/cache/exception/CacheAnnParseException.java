package com.kcp.platform.base.cache.exception;

import com.kcp.platform.core.exception.SystemException;

/**
 * 日志注解解析错误异常
 * 
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
