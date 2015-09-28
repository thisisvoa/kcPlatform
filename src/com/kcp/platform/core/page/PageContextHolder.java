package com.kcp.platform.core.page;

/**
 * 分页上下文持有器
 * @author Administrator
 *
 */
public class PageContextHolder {
	/**
	 * 分页请求持有器
	 */
	private static ThreadLocal<PageRequest> pageRequestHolder = new ThreadLocal<PageRequest>();
	
	/**
	 * 分页结果持有器
	 */
	private static ThreadLocal<Page> pageHolder = new ThreadLocal<Page>();
	
	
	/**
	 * 设置当前线程该次请求的分页上下文信息
	 * @param page
	 */
	public static void setPage(Page page){
		pageHolder.set(page);
	}
	
	/**
	 * 获取当前线程该次请求的分页上下文信息
	 * @return
	 */
	public static Page getPage(){
		return pageHolder.get();
	}
	
	/**
	 * 设置分页请求
	 * @param request
	 */
	public static void setPageRequest(PageRequest request){
		pageRequestHolder.set(request);
	}
	
	/**
	 * 获取分页请求
	 * @return
	 */
	public static PageRequest getPageRequest(){
		return pageRequestHolder.get();
	}
}
