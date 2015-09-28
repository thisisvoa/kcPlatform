package com.kcp.platform.sys.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

import com.kcp.platform.common.user.dao.IUserMapper;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.sys.context.SpringContextHolder;

public class CASLoginFilter implements Filter{
	
	private IUserMapper userMapper = SpringContextHolder.getBean(IUserMapper.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException{
		HttpServletRequest httpServletRequest = (HttpServletRequest)req;
		String userName = returnUsername(httpServletRequest);
		if(userName != null && !"".equals(userName)){
			httpServletRequest.setAttribute("username", userName);
			httpServletRequest.setAttribute("isCas", "1");
			User user = userMapper.queryUserByYhdlzh(userName);
			httpServletRequest.setAttribute("encodePassword", user.getYhdlmm());
			/*Session session = SecurityUtils.getSubject().getSession();
			User user = accountManager.findUserByLoginName(userName);
			session.setAttribute(UserManager.SESSION_KEY_USER, user);
			UsernamePasswordToken token = new UsernamePasswordToken(userName, user.getPassword());
			SecurityUtils.getSubject().login(token);*/
		}
		
		HttpServletResponse response = (HttpServletResponse) res;
		/*response.setHeader("Access-Control-Allow-Origin", "*");*/
		response.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		/*if (httpServletRequest.getSession().getAttribute("token") == null) {
			Long currtime = System.currentTimeMillis();
			httpServletRequest.getSession().setAttribute("token", currtime.toString());
		}*/
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 验证用户是否合法
	 * @param req
	 * @return
	 */
	public String returnUsername(HttpServletRequest req){
		Assertion assertion;
		Map userAttributes;
		String username="";
		try
		{
			//CAS api获取用户信息
			assertion = AssertionHolder.getAssertion();
			/*第一次登录，CAS验证过用户名、密码成功之后，会将用户名放入内存中，业务系统可以获得*/
			username = assertion.getPrincipal().getName();
			
			return username;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
}
