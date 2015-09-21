package com.casic27.platform.common.log.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.casic27.platform.common.log.Logger;
import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.log.annotation.OperateLogAnnParser;
import com.casic27.platform.common.log.core.SqlContextHolder;
import com.casic27.platform.common.log.entity.SqlContext;
import com.casic27.platform.common.log.util.MyBatisSql;
import com.casic27.platform.common.log.util.MyBatisSqlUtils;
import com.casic27.platform.core.mybatis.plugin.PageAnnParser;
import com.casic27.platform.util.StringUtils;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }) })
public class SqlLogInterceptor implements Interceptor {
	/**
	 * 注解解释器
	 */
	private OperateLogAnnParser parser = new OperateLogAnnParser();
	
	private Properties properties;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] queryArgs = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];
		String sqlId = mappedStatement.getId();
		int index = sqlId.lastIndexOf(".");
		String className = sqlId.substring(0, index);
		String methodName = sqlId.substring(index+1);
		Object result = invocation.proceed();
		//方法上有标注OperateLog注解才进行日志记录
		if(parser.hasOperateLogAnn(className, methodName)){
			StringBuffer sql = new StringBuffer(SqlContextHolder.getSql());
			Object parameter = queryArgs[1];
			MyBatisSql myBatisSql = MyBatisSqlUtils.getMyBatisSql(mappedStatement, parameter);
			String operateContent = myBatisSql.toString();
			if(StringUtils.isNotEmpty(sql.toString())){
				sql.append(";");
			}
			SqlContextHolder.setSql(sql.append(operateContent).toString());
			
			OperateLog operateLogAnn = parser.getOperateLogAnn(className, methodName);
			if(operateLogAnn.isRecord()){
				String logType = operateLogAnn.type().value();
				String moduleName = operateLogAnn.moduleName();
				String operateDesc = operateLogAnn.operateDesc();
				Logger.getInstance().addSysLog(logType, operateContent, moduleName, operateDesc);
			}
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;  
	}

}
