package com.kcp.platform.base.file.entity;

import java.io.Serializable;

public class UploadFailResponse implements UploadResponse,Serializable {
	/**
	 * 是否上传成功
	 */
	private boolean isSuccess = false;
	/**
	 * 错误代码代码
	 */
	private String errorCode;
	
	/**
	 * 错误信息
	 */
	private String errorMsg;
	
	public UploadFailResponse(){
		
	}
	
	public UploadFailResponse(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public UploadFailResponse(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	

}
