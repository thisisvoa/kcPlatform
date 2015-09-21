package com.casic27.platform.sys.context;

public class ApplicationContext {
	private static ApplicationContext applicationContext = new ApplicationContext();
	
	private ApplicationContext(){
		
	}
	
	public static ApplicationContext getInstance(){
		return applicationContext;
	}
	/**
	 * 项目的绝对路径
	 */
	private String webRoot;

	public String getWebRoot() {
		return webRoot;
	}
	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}
	
}
