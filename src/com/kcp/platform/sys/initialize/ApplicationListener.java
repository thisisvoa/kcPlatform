package com.kcp.platform.sys.initialize;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kcp.platform.sys.context.ApplicationContext;

public class ApplicationListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(ApplicationListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("application Initialize!");
		ServletContext context = event.getServletContext();
		String webRoot = context.getRealPath("/");
		ApplicationContext applicationContext = ApplicationContext.getInstance();
		applicationContext.setWebRoot(webRoot.replaceAll("\\\\", "/"));
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("application destory!");
	}

}
