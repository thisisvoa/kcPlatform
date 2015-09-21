package org.apache.shiro.cas;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.casic27.platform.sys.constants.CommonConst;

public class CasRefererFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString() + "?";
		@SuppressWarnings("unchecked")
		Map<String, String[]> zzMap = request.getParameterMap();
		if (zzMap != null) {
			for (String s : zzMap.keySet()) {
				String[] value = zzMap.get(s);
				for (String val : value) {
					url = url + s + "=" + val + "&";
				}
			}
		}
		if (httpRequest.getSession().getAttribute("cas_Referer") == null
				&& httpRequest.getSession().getAttribute(
						CommonConst.SESSION_USER_KEY) == null) {
			httpRequest.getSession().setAttribute("cas_Referer", url);
		} else if (httpRequest.getSession().getAttribute(
				CommonConst.SESSION_USER_KEY) != null) {
			httpRequest.getSession().setAttribute("cas_Referer", null);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
