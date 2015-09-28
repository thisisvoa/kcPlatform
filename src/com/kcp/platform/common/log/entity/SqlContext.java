package com.kcp.platform.common.log.entity;

public class SqlContext {
	private String sql = "";
	/**
	 * 起始切入点
	 */
	private String joinPoint;
	
	public SqlContext(){
		
	}
	
	public SqlContext(String sql){
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	
	public String getJoinPoint() {
		return joinPoint;
	}

	public void setJoinPoint(String joinPoint) {
		this.joinPoint = joinPoint;
	}
}
