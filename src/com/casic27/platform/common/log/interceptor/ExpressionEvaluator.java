/**
 * @(#)com.casic27.platform.base.log.interceptor.ExpressionEvaluator.java
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
package com.casic27.platform.common.log.interceptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.casic27.platform.core.exception.SystemException;




/**
 * 注解key表达式解析器
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Nov 24, 2011
 */
class ExpressionEvaluator {

	private SpelExpressionParser parser = new SpelExpressionParser();
	
	private ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	private Map<Method, Expression> keyCache = new ConcurrentHashMap<Method, Expression>();
	private Map<Method, Method> targetMethodCache = new ConcurrentHashMap<Method, Method>();
	/**
	 * 创建注解解析上下文
	 * @param method
	 * @param args
	 * @param targetClass
	 * @return
	 */
	EvaluationContext createEvaluationContext(Method method, Object[] args, Class<?> targetClass) {
		DefaultLogExpressionRootObject rootObject = new DefaultLogExpressionRootObject(method.getName());
		StandardEvaluationContext evaluationContext = new LazyParamAwareEvaluationContext(rootObject,
				paramNameDiscoverer, method, args, targetClass, targetMethodCache);

		return evaluationContext;
	}
	
	
	/**
	 * 生成Key
	 * @param keyExpression
	 * @param method
	 * @param evalContext
	 * @return
	 */
	String parse(String logExpression, Method method, EvaluationContext evalContext) {
		Expression keyExp = keyCache.get(method);
		if (keyExp == null) {
			try {
				keyExp = parser.parseExpression(logExpression);
			} catch (ParseException e) {
				throw new SystemException("缓存方法注解Key格式错误，请使用正确的SPEL格式!",e);
			} 
			keyCache.put(method, keyExp);
		}
		return keyExp.getValue(evalContext).toString();
	}
}