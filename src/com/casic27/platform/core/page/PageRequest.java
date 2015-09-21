package com.casic27.platform.core.page;

import java.io.Serializable;

public class PageRequest implements Serializable {
	private static final long serialVersionUID = 429319287171290580L;
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 页数 
	 */
	private int page=1;
	
	/** 
	 * 分页大小 
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	public PageRequest(){
		
	}
	/**
	 * 构造函数
	 * @param page
	 * @param pageSize
	 */
	public PageRequest(int page, int pageSize){
		this.page = page;
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
