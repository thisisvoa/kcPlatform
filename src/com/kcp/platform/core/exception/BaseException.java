package com.kcp.platform.core.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *类描述：所有异常的基类
 * 
 *@Version：1.0
 */
public class BaseException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 446583692535675941L;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 异常代码
	 */
	private String errKey = null;
	
	/**
	 * 异常消息参数值
	 */
	private Object[] params = null;
	
	public BaseException(){
		super();
	}
	
	public BaseException(String errKey){
		super(errKey);
		this.errKey = errKey;
	}
	
	public BaseException(String errKey, Object[] params){
		super(errKey);
		this.errKey = errKey;
		this.params = params;
	}
	
	public BaseException(Throwable cause){
		super(cause);
	}
	
	public BaseException(String errKey, Throwable cause){
		super(errKey, cause);
		this.errKey = errKey;
	}
	
	public BaseException(String errKey, Object[] params, Throwable cause){
		super(errKey, cause);
		this.errKey = errKey;
		this.params = params;
	}
	
	/**
	 * 获取本级的异常信息
	 */
	public String getMessage(){
		//根据异常代码从异常配置信息读取异常，若读取不到，则默认异常代码即为异常信息
		String message = ExpMsgHolder.getMessage(errKey, errKey);
		if(params!=null){
			message = MessageFormat.format(message, params);
		}
		
		return message;
	}
	
	/**
	 * 获取堆栈异常信息
	 * @return
	 */
	public String getStackMessage(){
		String stackMessage = null;
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			printStackTrace(pw);
			
			stackMessage = sw.getBuffer().toString();
			sw.flush();
			pw.flush();
			sw.close();
			pw.close();
		} catch (IOException e) {
			logger.error(e);
		}
		return stackMessage;
	}
	
	/**
	 * 获取该异常的上一级异常类
	 * @return
	 */
	public String getCauseClass(){
		StackTraceElement[] trace = getStackTrace();
		String causeClass = null;
		causeClass = ((StackTraceElement)trace[0]).getClassName();
		return causeClass;
	}
}
