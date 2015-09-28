
package com.kcp.platform.core.exception;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class ResponseError {
	/**
	 * 错误编号
	 */
	private String errorCode;
	
	/**
	 * 错误信息
	 */
	private String errorMessage;
	
	/**
	 * 请求的URI
	 */
	private String requestURI;
	
	public ResponseError(String errorMessage){
		this.errorMessage = errorMessage;
	}
	
	public ResponseError(String errorCode, String errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ResponseError(String errorCode, String errorMessage, String requestURI){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.requestURI = requestURI;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
}
