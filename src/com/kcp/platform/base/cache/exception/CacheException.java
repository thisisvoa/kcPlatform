package com.kcp.platform.base.cache.exception;

import com.kcp.platform.core.exception.BaseException;


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
