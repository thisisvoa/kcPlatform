package com.kcp.platform.base.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

/**
 * SPEL解析上下文，读取执行的方法的参数并加入到解析上下文中，对于解析过的方法将解析结果放入缓存
 */
class LazyParamAwareEvaluationContext extends StandardEvaluationContext {

	private final ParameterNameDiscoverer paramDiscoverer;
	private final Method method;
	private final Object[] args;
	private Class<?> targetClass;
	private Map<Method, Method> methodCache;

	private boolean paramLoaded = false;

	LazyParamAwareEvaluationContext(Object rootObject, ParameterNameDiscoverer paramDiscoverer, Method method,
			Object[] args, Class<?> targetClass, Map<Method, Method> methodCache) {
		super(rootObject);

		this.paramDiscoverer = paramDiscoverer;
		this.method = method;
		this.args = args;
		this.targetClass = targetClass;
		this.methodCache = methodCache;
	}

	/**
	 * 根据参数名读取参数值
	 */
	@Override
	public Object lookupVariable(String name) {
		Object variable = super.lookupVariable(name);
		if (variable != null) {
			return variable;
		}

		if (!paramLoaded) {
			paramLoaded = true;
			loadArgsAsVariables();
			variable = super.lookupVariable(name);
		}

		return variable;
	}

	private void loadArgsAsVariables() {
		if (ObjectUtils.isEmpty(args)) {
			return;
		}

		Method targetMethod = methodCache.get(method);
		if (targetMethod == null) {
			targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			if (targetMethod == null) {
				targetMethod = method;
			}
			methodCache.put(method, targetMethod);
		}

		for (int i = 0; i < args.length; i++) {
			super.setVariable("p" + i, args[i]);
		}

		String[] parameterNames = paramDiscoverer.getParameterNames(targetMethod);
		// 保存参数名
		if (parameterNames != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				super.setVariable(parameterNames[i], args[i]);
			}
		}
	}
}