package com.casic27.platform.common.log.util;

import java.util.List;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

public class MyBatisSqlUtils {
	/**
	 * 获取一个Mybatis运行时调用的SQL和参数
	 * @param ms
	 * @param parameterObject
	 * @return
	 */
	public static MyBatisSql getMyBatisSql(MappedStatement ms,
			Object parameterObject) {
		MyBatisSql ibatisSql = new MyBatisSql();
		BoundSql boundSql = ms.getBoundSql(parameterObject);
		ibatisSql.setSql(boundSql.getSql());
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		if (parameterMappings != null) {
			Object[] parameterArray = new Object[parameterMappings.size()];
			MetaObject metaObject = parameterObject == null ? null : MetaObject
					.forObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++)
			{
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (ms.getConfiguration().getTypeHandlerRegistry()
							.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = MetaObject.forObject(value).getValue(
									propertyName.substring(prop.getName()
											.length()));
						}
					} else {
						value = metaObject == null ? null : metaObject
								.getValue(propertyName);
					}
					parameterArray[i] = value;
				}
			}
			ibatisSql.setParameters(parameterArray);
		}
		return ibatisSql;
	}
}
