package com.kcp.platform.base.file.entity;

import java.io.Serializable;

public class UploadSuccessResponse implements UploadResponse,Serializable {
	private boolean isSuccess = true;
	/**
	 * 成功时候的返回结果
	 */
	private Object result;

	public UploadSuccessResponse(){
		
	}
	
	public UploadSuccessResponse(Object result){
		this.result = result;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
