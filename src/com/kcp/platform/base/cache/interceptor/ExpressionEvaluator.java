package com.kcp.platform.base.cache.interceptor;

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

import com.kcp.platform.base.cache.exception.CacheAnnParseException;


/**
 * 注解key表达式解析器
 * 
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
		DefaultCacheExpressionRootObject rootObject = new DefaultCacheExpressionRootObject(method.getName());
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
	String key(String keyExpression, Method method, EvaluationContext evalContext) {
		Expression keyExp = keyCache.get(method);
		if (keyExp == null) {
			try {
				keyExp = parser.parseExpression(keyExpression);
			} catch (ParseException e) {
				throw new CacheAnnParseException("缓存方法注解Key格式错误，请使用正确的SPEL格式!",e);
			} 
			keyCache.put(method, keyExp);
		}
		return keyExp.getValue(evalContext).toString();
	}
}