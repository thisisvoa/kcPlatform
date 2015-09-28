package com.kcp.platform.sys.security.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.kcp.platform.sys.constants.CommonConst;

public class AuthcFilter extends FormAuthenticationFilter {
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				return executeLogin(request, response);
			} else {
				// allow them to see the login page ;)
				return true;
			}
		} else {
			if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
				response.setCharacterEncoding("UTF-8");
	            PrintWriter out = response.getWriter();
	            out.print(CommonConst.AJAX_UNLOGIN_FLAG);
	            out.flush();
	            out.close();
			}else{
				saveRequestAndRedirectToLogin(request, response);
			}
			return false;
		}
	}
}
