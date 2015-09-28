package com.kcp.platform.core.mybatis.plugin;



import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.kcp.platform.core.jdbc.dialect.Dialect;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.core.page.PageRequest;
import com.kcp.platform.util.PropertiesHelper;


/**
 * 为ibatis3提供基于方言(Dialect)的分页查询的插件
 * 
 * 将拦截Executor.query()方法实现分页方言的插入.
 * 
 * 配置文件内容:
 * <pre>
 * 	&lt;plugins>
 *		&lt;plugin interceptor="cn.org.rapid_framework.ibatis3.plugin.OffsetLimitInterceptor">
 *			&lt;property name="dialectClass" value="cn.org.rapid_framework.jdbc.dialect.MySQLDialect"/>
 *		&lt;/plugin>
 *	&lt;/plugins>
 * </pre>
 * 
 *
 */
@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffsetLimitInterceptor implements Interceptor{
	protected static Log logger = LogFactory.getLog(OffsetLimitInterceptor.class);
	
	private static int MAPPED_STATEMENT_INDEX = 0;
	private static int PARAMETER_INDEX = 1;
	
	/**
	 * SQL转化器
	 */
	private Dialect dialect;
	
	/**
	 * 注解解释器
	 */
	private PageAnnParser parser = new PageAnnParser();
	
	/**
	 * 拦截
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] queryArgs = invocation.getArgs();
		PageRequest pageRequest = PageContextHolder.getPageRequest();
		//只有ThreadLocal中存在PageRequest对象才进行分页查询
		if(pageRequest!=null){
			MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
			String sqlId = ms.getId();
			int index = sqlId.lastIndexOf(".");
			String className = sqlId.substring(0, index);
			String methodName = sqlId.substring(index+1);
			
			//方法上有标注Pageabel注解才进行分页查询
			if(parser.hasPageAnn(className, methodName)){
				Object parameter = queryArgs[PARAMETER_INDEX];
				BoundSql boundSql = ms.getBoundSql(parameter);
				String sql = boundSql.getSql().trim();
				
				//统计总记录数
				Connection connection = ((Executor)invocation.getTarget()).getTransaction().getConnection();
				int count = CalRowCountUtil.calRowCount(ms, dialect, parameter, connection);
                Page page = new Page(pageRequest.getPage(),pageRequest.getPageSize(),count);
                PageContextHolder.setPage(page);
                
                //改写执行的SQL为物理分页
				int offset = page.getOffset();
				int limit = page.getLimit();
				if (dialect.supportsLimitOffset()) {
					sql = dialect.getLimitString(sql, offset, limit);
				} else {
					sql = dialect.getLimitString(sql, 0, limit);
				}
				BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
				MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
				queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
			}
		}
			
		return invocation.proceed();
	}
	
	/**
	 * 将原来未分页的查询SQL转化为物理分页查询SQL
	 * @param ms
	 * @param boundSql
	 * @param sql
	 * @return
	 */
	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
			String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
		    String prop = mapping.getProperty();
		    if (boundSql.hasAdditionalParameter(prop)) {
		        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
		    }
		}
		return newBoundSql;
	}
	
	/**
	 * 拷贝未分页的MappedStatement为分页的MappedStatement副本
	 * @param ms
	 * @param newSqlSource
	 * @return
	 */
	private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		builder.keyProperty(ms.getKeyProperty());
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());
		return builder.build();
	}
	
	
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
    
    /**
	 * 配置的时候用于设置参数
	 */
	public void setProperties(Properties properties) {
		String dialectClass = new PropertiesHelper(properties).getRequiredProperty("dialectClass");
		try {
			dialect = (Dialect)Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot create dialect instance by dialectClass:"+dialectClass,e);
		} 
		logger.info(OffsetLimitInterceptor.class.getSimpleName()+".dialect="+dialectClass);
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
}
