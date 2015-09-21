/**
 * @(#)com.casic27.platform.base.cache.interceptor.MethodCacheInterceptor.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.cache.annotation.CacheAnnotationParser;
import com.casic27.platform.base.cache.annotation.SimpleCachingAnnotationParser;
import com.casic27.platform.base.cache.itemmanager.CacheItemManager;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.util.AssertUtils;
import com.casic27.platform.util.StringUtils;


/**
 * 使用Spring aop 对执行的方法进行拦截，
 */
@Aspect
@Component("methodCacheInterceptor")
public class MethodCacheInterceptor {
	
	private CacheAnnotationParser annParser = new SimpleCachingAnnotationParser();
	
	private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
	
	private Map<String, CacheItemManager> itemManagerCache = new ConcurrentHashMap<String, CacheItemManager>();
	
	/**
	 * 拦截带有@Cacheable 注解的方法，若能够在缓存中找到值则直接返回,不执行方法逻辑；否则，继续执行方法逻辑，并在方法执行结束时候将返回结果存入缓存；
	 */
	@Around(value="@annotation(com.casic27.platform.base.cache.annotation.Cacheable)")
	public Object cacheableProfiling(ProceedingJoinPoint pjp) throws Throwable {
		
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
        Method method = joinPointObject.getMethod();
		MethodCacheDefinition definition = annParser.parseCacheAnnotation(method);
		CacheItemManager cacheItemManager = getCacheItemManager(definition.getCacheItemManager());
		AssertUtils.notNull(cacheItemManager,"bean名称为:["+definition.getCacheItemManager()+"]的缓存项管理不存在");
		
		Object retVal = null;
		
		//生成KEY
		CacheOperationContext context = getOperationContext(definition, method, pjp.getArgs(), pjp.getClass());
		String key = context.generateKey();
		Object val = cacheItemManager.get(key);
		if (val!=null) {
			retVal = val;
		} else {
			retVal = pjp.proceed();
			if(retVal!=null){
				cacheItemManager.put(key, retVal);
			}
		}
		
		return retVal;
	}
	
	/**
	 * 拦截带有@CacheEvit注解的方法,用于更新缓存
	 */
	@Around(value="@annotation(com.casic27.platform.base.cache.annotation.CacheEvict)")
	public void cacheEvitProfiling(ProceedingJoinPoint pjp)throws Throwable{
		//执行拦截方法
		pjp.proceed();
		
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
        Method method = joinPointObject.getMethod();
		MethodCacheDefinition definition = annParser.parseCacheAnnotation(method);
		
		CacheItemManager cacheItemManager = getCacheItemManager(definition.getCacheItemManager());
		AssertUtils.notNull(cacheItemManager,"bean名称为:["+definition.getCacheItemManager()+"]的缓存项管理不存在");
		
		if(definition.isEvitAll()){
			//发布命令，将该组下的缓存项逻辑过期
			cacheItemManager.refreshAll();
		}else{
			String regexpScope = definition.getRegexpScope();
			CacheOperationContext context = getOperationContext(definition, method, pjp.getArgs(), pjp.getClass());
			//生成KEY
			String key = context.generateKey();
			
			if(StringUtils.isNotEmpty(regexpScope)){
				cacheItemManager.refreshByRegexp(key, context.parseRegexpScope());
			}else{
				cacheItemManager.refresh(key);
			}
		}
	}
	 
	protected CacheOperationContext getOperationContext(MethodCacheDefinition definition, Method method, Object[] args,
			Class<?> targetClass) {
		return new CacheOperationContext(definition, method, args, targetClass);
	}
	
	
	public void setAnnParser(CacheAnnotationParser annParser) {
		this.annParser = annParser;
	}
	
	
	/**
	 * 获取缓存项管理器，对缓存项管理器做了缓存，避免在Spring上下文中重复查找；
	 * @param beanName
	 * @return
	 */
	private CacheItemManager getCacheItemManager(String beanName){
		CacheItemManager manager = null;
		manager = itemManagerCache.get(beanName);
		if(manager==null){
			manager = (CacheItemManager)SpringContextHolder.getBean(beanName);
			itemManagerCache.put(beanName, manager);
		}
		return manager;
	}
	/**
	 * 方法缓存上下文对象，用于解析缓存注解中定义的信息，生成缓存KEY
	 * 
	 * @author 林斌树 (linbinshu@casic27.com)
	 * @version Revision: 1.00 Date: Nov 23, 2011
	 */
	protected class CacheOperationContext {
		private MethodCacheDefinition definition;
		private final Method method;
		private final Object[] args;
		
		private final EvaluationContext evalContext;
		
		public CacheOperationContext(MethodCacheDefinition definition,
				Method method, Object[] args, Class<?> targetClass) {
			this.definition = definition;
			this.method = method;
			this.args = args;
			
			this.evalContext = evaluator.createEvaluationContext(method,
					args, targetClass);
			
		}
		
		/**
		 * 生成Key
		 * @return
		 */
		protected String generateKey() {
			
			if (!StringUtils.isEmpty(definition.getKey())) {
				String key = evaluator.key(definition.getKey(), method, evalContext);
				return key;
			}else{
				int hashCode = 17;

				for (Object object : args) {
					hashCode = 31 * hashCode + object.hashCode();
				}

				return Integer.valueOf(hashCode).toString();
			}
		}
		
		protected String parseRegexpScope(){
			String regex = evaluator.key(definition.getRegexpScope(), method, evalContext);
			return regex;
		}
		public MethodCacheDefinition getDefinition() {
			return definition;
		}
		public void setDefinition(MethodCacheDefinition definition) {
			this.definition = definition;
		}
		public Method getMethod() {
			return method;
		}
		public Object[] getArgs() {
			return args;
		}
	}
}
