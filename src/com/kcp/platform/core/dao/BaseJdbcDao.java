package com.kcp.platform.core.dao;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;


/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
public class BaseJdbcDao extends JdbcDaoSupport {
	/**
	 * 执行数据库insert语句
	 * @param sql	待执行的SQL
	 * @param parameters 	SQL中的参数
	 * @return 
	 */
	public int insert(String sql,Object... parameters){
		return executeSqlForDml(sql, parameters);
	}
	
	/**
	 * execute SQL for update 
	 * @param sql	execute SQL
	 * @param parameters	execute parameters
	 * @return	effect row number
	 */
	public int update(String sql,Object... parameters){
		return executeSqlForDml(sql, parameters);
	}
	
	/**
	 * execute SQL for delete 
	 * @param sql	execute SQL
	 * @param parameters	execute parameters
	 * @return	effect row number
	 */
	public int delete(String sql,Object... parameters){
		return executeSqlForDml(sql, parameters);
	}
	
	/**
	 * execute SQL for query object
	 * @param sql	execute SQL
	 * @param parameters	execute parameters
	 * @return	query result (object)
	 */
	public Object queryForObject(String sql,Object... parameters){
		return this.getJdbcTemplate().queryForMap(sql, parameters);
	}
	
	/**
	 * execute SQL for query list
	 * @param sql	execute SQL
	 * @param parameters	execute parameters
	 * @return	query result (list)
	 */
	public List<?> queryForList(String sql,Object... parameters){
		return this.getJdbcTemplate().queryForList(sql, parameters);
	}
	
	/**
	 * 获取查询的总记录数
	 * @param sql
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getObjectTotal(String sql,Object... parameters){
		String sqlString = "SELECT COUNT(1) AS tatolRow from (" + sql +")";
		return this.getJdbcTemplate().queryForInt(sqlString, parameters);
	}
	
	/**
	 * execute SQL for DML type 
	 * @param sql	execute SQL
	 * @param parameters	execute parameters
	 * @return	effect row number
	 */
	private int executeSqlForDml(String sql,Object... parameters){
		return this.getJdbcTemplate().update(sql, parameters);
	}
	
	/**
	 * execute SQL for DDL
	 * @param sql
	 */
	public void executeSqlForDdl(String sql){
		 this.getJdbcTemplate().execute(sql);
	}
}
