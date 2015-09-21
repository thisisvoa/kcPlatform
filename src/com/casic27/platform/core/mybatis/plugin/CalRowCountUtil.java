/**
 * @(#)com.casic27.platform.core.mybatis.plugin.CalRowCountUtil.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-8
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.casic27.platform.core.jdbc.dialect.Dialect;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class CalRowCountUtil {
	/**
	 * 统计MappedStatement执行的sql的总记录数
	 * @param ms
	 * @param connection
	 * @param countSql
	 * @param parameter
	 * @return
	 */
	public static int calRowCount(MappedStatement ms, Dialect dialect, Object parameter, Connection connection)throws Throwable{
		BoundSql boundSql = ms.getBoundSql(parameter);
		String sql = boundSql.getSql().trim();
		String countSql = dialect.getCountString(sql);
		
		BoundSql coutBoundSql = new BoundSql(ms.getConfiguration(),countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		PreparedStatement countStatement = connection.prepareStatement(countSql);
		setParameters(countStatement, ms, coutBoundSql, parameter);
		ResultSet rs = countStatement.executeQuery();
		int count = 0;
		if(rs.next()){
			count = rs.getInt(1);
		}
		rs.close();
		countStatement.close();
		return count;
	}
	
	 /** 
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler 
     * @param ps 
     * @param mappedStatement 
     * @param boundSql 
     * @param parameterObject 
     * @throws SQLException 
     */  
    private static void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {  
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());  
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();  
        if (parameterMappings != null) {  
            Configuration configuration = mappedStatement.getConfiguration();  
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();  
            MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);  
            for (int i = 0; i < parameterMappings.size(); i++) {  
                ParameterMapping parameterMapping = parameterMappings.get(i);  
                if (parameterMapping.getMode() != ParameterMode.OUT) {  
                    Object value;  
                    String propertyName = parameterMapping.getProperty();  
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);  
                    if (parameterObject == null) {  
                        value = null;  
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {  
                        value = parameterObject;  
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {  
                        value = boundSql.getAdditionalParameter(propertyName);  
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {  
                        value = boundSql.getAdditionalParameter(prop.getName());  
                        if (value != null) {  
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));  
                        }  
                    } else {  
                        value = metaObject == null ? null : metaObject.getValue(propertyName);  
                    }  
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();  
                    if (typeHandler == null) {  
                        throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());  
                    }  
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());  
                }  
            }  
        }
    }
}
