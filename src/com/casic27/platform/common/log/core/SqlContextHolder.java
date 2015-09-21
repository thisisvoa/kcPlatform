package com.casic27.platform.common.log.core;

import com.casic27.platform.common.log.entity.SqlContext;


public class SqlContextHolder {
	private static ThreadLocal<SqlContext> sqlHolder = new ThreadLocal<SqlContext>();
	
	public static void setSqlContext(SqlContext sqlContext){
		sqlHolder.set(sqlContext);
	}
	
	public static SqlContext getSqlContext(){
		return sqlHolder.get();
	}
	
	public static void clear(){
		sqlHolder.set(null);
	}
	
	public static void clearJoinPoint(){
		SqlContext sqlContext = getSqlContext();
		if(sqlContext!=null){
			sqlContext.setJoinPoint("");
		}
	}
	
	public static void setSql(String sql){
		SqlContext sqlContext = getSqlContext();
		if(sqlContext==null){
			sqlContext = new SqlContext();
		}
		if(sql!=null && sql.length()>4000){
			sql = sql.substring(0, 3999);
		}
		sqlContext.setSql(sql);
		setSqlContext(sqlContext);
	}
	
	public static String getSql(){
		SqlContext sqlContext = getSqlContext();
		if(sqlContext == null){
			return "";
		}else{
			return sqlContext.getSql();
		}
	}
	
	public static void setJoinPoint(String joinPoint){
		SqlContext sqlContext = getSqlContext();
		if(sqlContext==null){
			sqlContext = new SqlContext();
		}
		sqlContext.setJoinPoint(joinPoint);
		setSqlContext(sqlContext);
	}
	
	public static String getJoinPoint(){
		SqlContext sqlContext = getSqlContext();
		if(sqlContext==null){
			return "";
		}
		return sqlContext.getJoinPoint();
	}
}
