/**
 * @(#)com.casic27.platform.core.page.PageHandlerInterceptor.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-7
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <pre>
 * 类描述：Web端分页拦截器，用于拦截有分页列表的页面传递过来的分页信息，并将这类信息存储在PageContextHolder中
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class PageHandlerInterceptor extends HandlerInterceptorAdapter {
	
	private static String PAGE_NO_FLAG = "_PAGE_NO_";

	private static String PAGE_SIZE_FLAG = "_PAGE_SIZE_";
	
	@Override
	public void afterCompletion(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse, Object obj,
			Exception exception) throws Exception {
		super.afterCompletion(httpservletrequest, httpservletresponse, obj, exception);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse httpservletresponse, Object obj,
			ModelAndView modelandview) throws Exception {
		//判断是否有分页结果，如果有的话，设置PageHolder --- 一般第一次查询分页时，会返回记录总数
		if(PageContextHolder.getPage()!=null){
			request.setAttribute("_PAGEINFO_", PageContextHolder.getPage());
			PageContextHolder.setPage(null);
			PageContextHolder.setPageRequest(null);
		}
		super.postHandle(request, httpservletresponse, obj, modelandview);
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//清空PageContext
		PageContextHolder.setPage(null);
		PageContextHolder.setPageRequest(null);
		
		//判断是否有提交分页参数，如果有的话，设置PageHolder
		if(StringUtils.isNotEmpty(request.getParameter(PAGE_NO_FLAG))){
			PageRequest pageRequest = new PageRequest();
			pageRequest.setPage(Integer.parseInt(request.getParameter(PAGE_NO_FLAG)));
			if(StringUtils.isNotEmpty(request.getParameter(PAGE_SIZE_FLAG))){
				pageRequest.setPageSize(Integer.parseInt(request.getParameter(PAGE_SIZE_FLAG)));
			}
			PageContextHolder.setPageRequest(pageRequest);
		}
		return super.preHandle(request, response, handler);
	}
}
