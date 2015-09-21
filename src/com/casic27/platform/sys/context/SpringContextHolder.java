/**
 * @(#)com.casic27.platform.sys.context.SpringContextHolder.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.sys.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.casic27.platform.util.AssertUtils;
import com.casic27.platform.util.StringUtils;


/**
 * 类描述：
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * 
 * @author calvin
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if(StringUtils.isEmpty(name)){
			return null;
		}
		assertContextInjected();
		T result = null;
		try {
			result = (T) applicationContext.getBean(name);
		} catch (BeansException e) {
		}
		return result;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		logger.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);

		if (SpringContextHolder.applicationContext != null) {
			logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
					+ SpringContextHolder.applicationContext);
		}

		SpringContextHolder.applicationContext = applicationContext; //NOSONAR
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
     * 从Spring容器中获取Bean，其类型为clazz。Spring容器中必须至少包含一个该类型的Bean，
     * 否则返回null
     * @param clazz Bean的类型
     * @return Bean实例
     */
    @SuppressWarnings("unchecked")
	public static Object[] getBeansByType(Class clazz){
    	assertContextInjected();
        Map map = applicationContext.getBeansOfType(clazz);
        if (map.size() >= 1) {
            return map.values().toArray();
        }else {
            new IllegalAccessException("在Spring容器中没有类型为"+clazz.getName()+"的Bean");
        }
        return null;
    }
    
    
    @SuppressWarnings("unchecked")
	public static List getBeanListByType(Class clazz){
    	assertContextInjected();
        Map map = applicationContext.getBeansOfType(clazz);
        if (map.size() >= 1) {
        	List result = new ArrayList();
        	result.addAll(map.values());
        	return result;
        }else {
            new IllegalAccessException("在Spring容器中没有类型为"+clazz.getName()+"的Bean");
        }
        return null;
    }
	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		AssertUtils.state(applicationContext != null,
				"applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}
	
	
}
