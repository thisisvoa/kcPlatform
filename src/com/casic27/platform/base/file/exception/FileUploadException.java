package com.casic27.platform.base.file.exception;

import com.casic27.platform.core.exception.NotInterceptException;

public class FileUploadException extends NotInterceptException {
	public FileUploadException(){
		super();
	}
	
	public FileUploadException(String errKey){
		super(errKey);
	}
	
	public FileUploadException(String errKey, Object[] params){
		super(errKey, params);
	}
	
	public FileUploadException(Throwable cause){
		super(cause);
	}
	
	public FileUploadException(String errKey, Throwable cause){
		super(errKey, cause);
	}
	
	public FileUploadException(String errKey, Object[] params, Throwable cause){
		super(errKey, params, cause);
	}
}
