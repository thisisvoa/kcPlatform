package com.kcp.platform.bpm.util;

import com.kcp.platform.core.exception.BaseException;

public class ExceptionUtil {
	/**
	 * 获取流程错误信息
	 * @param e
	 * @return
	 */
	public static String getExceptionMsg(Exception e){
		if(e instanceof BaseException){
			return e.getMessage();
		}else{
			int i=0;
			Throwable t = e.getCause();
			while(!(t instanceof BaseException) && i<20){
				t = t.getCause();
				i++;
			}
			if(t instanceof BaseException){
				return t.getMessage();
			}else{
				return "系统未知异常！";
			}
		}
	}
}
