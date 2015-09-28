package com.kcp.platform.sys.security.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import com.kcp.platform.core.exception.BusinessException;

/**
 * <pre>
 * 类描述：将shiro的登录异常转化为平台的内部异常
 * </pre>
 * <pre>
 * </pre>
 */
public abstract class LoginExceptionResolver {
	public static BusinessException resolveException(Exception e){
		//密码错误
		if(e instanceof IncorrectCredentialsException){
			return new BusinessException("platform.login.001");
		}
		//未知用户名
		else if(e instanceof UnknownAccountException){
			return new BusinessException("platform.login.002");
		}
		//锁定的用户
		else if(e instanceof LockedAccountException){
			return new BusinessException("platform.login.003");
		}
		//太频繁访问
		else if(e instanceof ExcessiveAttemptsException){
			return new BusinessException("platform.login.004");
		}
		//认证失败
		else if(e instanceof AuthenticationException){
			return new BusinessException("platform.login.005");
		}else {
			return new BusinessException("platform.login.005");
		}
	}
}
