package com.kcp.platform.common.log.interceptor;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.SimpleLogingAnnotationParser;
import com.kcp.platform.common.log.annotation.SysLogAnnotationParser;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.util.StringUtils;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	系统日志拦截器
 *</pre> 
 */
@Aspect
@Component("sysLogInterceptor")
public class SysLogInterceptor {
	protected final Log logger = LogFactory.getLog(SysLogInterceptor.class);
	/**
	 * 注解解析器
	 */
	private SysLogAnnotationParser parser = new SimpleLogingAnnotationParser();
	
	private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
	
	@Before(value="execution(* com.kcp*..*Service.*(..))")
	public void clearBeforeExcute(JoinPoint pjp){
		if(StringUtils.isEmpty(SqlContextHolder.getJoinPoint())){
			//清空sql上下文信息
			SqlContextHolder.clear();
			SqlContextHolder.setJoinPoint(pjp.toLongString());
		}
	}
	
	@AfterReturning(value="@annotation(com.kcp.platform.common.log.annotation.Log)",returning="returnVal")
	public void logAfterReturning(JoinPoint pjp, Object returnVal) throws Throwable {
		try {
			if(pjp.toLongString().equals(SqlContextHolder.getJoinPoint())){
				MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
				Method method = joinPointObject.getMethod();
				MethodSysLogDefinition definition = parser.parseLogAnnotation(method);
				
				
				String logType = definition.getType();
				String moduleName = definition.getModuleName();
				String operateDesc = definition.getOperateDesc(); 
				String operateContent = SqlContextHolder.getSql();
				if(definition.isUseSpel()){
					/**对日志描述和日志备注加入SPEL支持*/
					LogOperationContext context = getOperationContext(definition, method, pjp.getArgs(), pjp.getClass());
					operateDesc = context.desc();
				}
				Logger.getInstance().addSysLog(logType, operateContent, moduleName, operateDesc);
				SqlContextHolder.clear();
			}
		} catch (Exception e) {
			logger.error("记录系统操作日志失败",e);
		}
	}
	@AfterReturning(value="execution(* com.kcp*..*Service.*(..))")
	public void clearAfterReturning(JoinPoint pjp)throws Throwable{
		if(pjp.toLongString().equals(SqlContextHolder.getJoinPoint())){
			SqlContextHolder.clearJoinPoint();
		}
	}
	
	@AfterThrowing(value="execution(* com.kcp*..*Service.*(..))",throwing="ex")
	public void logAfterThrowing(JoinPoint pjp, Exception ex){
		SqlContextHolder.clear();
	}
	
	protected LogOperationContext getOperationContext(MethodSysLogDefinition definition, Method method, Object[] args,
			Class<?> targetClass){
		return new LogOperationContext(definition, method, args, targetClass);
	}
	
	/**
	 * 方法日志注解上下文对象，用于解析缓存注解中定义的信息，生成缓存KEY
	 * 
	 */
	protected class LogOperationContext {
		
		private MethodSysLogDefinition definition;
		
		private final Method method;
		
		private final EvaluationContext evalContext;
		
		public LogOperationContext(MethodSysLogDefinition definition,
				Method method, Object[] args, Class<?> targetClass){
			this.definition = definition;
			this.method = method;
			
			this.evalContext = evaluator.createEvaluationContext(method,
					args, targetClass);
		}
		
		public String parselog(String logExpression){
			return evaluator.parse(logExpression, method, evalContext);
		}
		
		public String desc(){
			return evaluator.parse(definition.getOperateDesc(), method, evalContext);
		}
	}
}
