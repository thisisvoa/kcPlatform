package com.kcp.platform.util.jdbc.dbType;

import java.sql.ResultSet;
import java.util.Map;

public interface DataBase {
	
	
	
	enum FieldType{
		VARCHAR,NUMBER,CLOB,BLOB,CHAR;
	}
	
	enum DatabaseType{
		ORACLE,MYSQL,SQLSERVER;
	}
	
	/**
	 * 获得数据库类型
	 * @return
	 */
	DatabaseType getDatabaseType();
	
	/**
	 * 生成特定数据库的连接符
	 * @return
	 */
	String getConcateOperator();
	
	/**
	 * 获取数据库连接URL模板
	 * ORACLE:  jdbc:oracle:thin:@<server>:<dbname>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	String getUrlTemplate();
	
	/**
	 * 获取数据库连接(添加多连接处理，字符集等参数)
	 * @return
	 */
	String getAccessUrl(String dbName ,String ip ,Integer port,Map< String, Object> argsMap);
	
	/**
	 * 获得驱动程序
	 * @return
	 */
	String getDriver();
	
	/**
	 * 查询表的字段名称的SQL语句
	 * @return
	 */
	String getTableColumnsNameSql(String tableName);

	/**
	 * 查询数据源的表信息和视图信息语句
	 * @return
	 */
	String getTablesSql();
}
