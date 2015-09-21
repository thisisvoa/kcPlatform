/**
 * @(#)IViewer.constants.sfddsf.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-7-4
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * 类描述：
 * 
 * @Author： 宗斌(zongbin@casic27.com)
 * @Version：1.0
 */
public class DBUtil {
	private static final Logger log = Logger
			.getLogger(com.casic27.platform.util.jdbc.DBUtil.class);

	Connection dbcon;
	
	DBSrc dbSrc;
	
	Statement stmt;

	long tmpTime = 0L;

	protected ResultSet rs;
	
	public DBUtil() {
		
	}
	public DBUtil(DBSrc dbSrc) {
		this.dbSrc = dbSrc;
	}

	public boolean exeSql(String strSql) {
		boolean exeSuccess = false;
		log.info("Sql:" + strSql);
		tmpTime = System.currentTimeMillis();
		if (dbcon == null)
			newConnection();
		try {
			stmt = dbcon.createStatement();
			stmt.executeUpdate(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime) + "ms");
			exeSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Error SQL:" + strSql, e);
		} finally {
			close();
		}
		return exeSuccess;
	}

	public boolean exeBatchSql(String strSql[]) {
		boolean exeSuccess = false;
		tmpTime = System.currentTimeMillis();
		if (dbcon == null)
			newConnection();
		try {
			dbcon.setAutoCommit(false);
			stmt = dbcon.createStatement();
			for (int i = 0; i < strSql.length; i++) {
				stmt.addBatch(strSql[i]);
			}
			log.info("SQL executeBatch and size is :" + strSql.length);
			stmt.executeBatch();
			dbcon.commit();
			log.info("Time:" + (System.currentTimeMillis() - tmpTime) + "ms");
			exeSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Error SQL:" + strSql, e);
		} finally {
			close();
		}
		return exeSuccess;
	}

	public ResultSet exeSqlQuery(String strSql) {
		log.info("Sql:" + strSql);
		try {
			tmpTime = System.currentTimeMillis();
			if (dbcon == null)
				newConnection();
			stmt = dbcon.createStatement(1004, 1008);
			rs = stmt.executeQuery(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime) + "ms");
		} catch (Exception e) {
			rs = null;
			e.printStackTrace();
			log.error("Error SQL:" + strSql, e);
		}
		return rs;
	}
	
	public List<Map<String, Object>> resultSetToList(ResultSet resultSet) throws SQLException{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		while(resultSet.next()){				
			ResultSetMetaData meta = resultSet.getMetaData();			
			Map<String, Object> columns =new HashMap<String, Object>(meta.getColumnCount());
									
			for(int i=1; i<=meta.getColumnCount(); i++){
				columns.put(meta.getColumnName(i),resultSet.getObject(i));					
			}				
			resultList.add(columns);				
		}	
		return resultList;
	}

	public boolean hasData(String strSql) {
		boolean hasData = true;
		log.info("Sql:" + strSql);
		try {
			tmpTime = System.currentTimeMillis();
			if (dbcon == null)
				newConnection();
			stmt = dbcon.createStatement();
			rs = stmt.executeQuery(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime) + "ms");
			if (!rs.next())
				hasData = false;			
		} catch (Exception ex) {
			log.error("Error SQL:" + strSql, ex);
			hasData = false;
		}finally {
			close();
		}
		return hasData;
	}

	public int getInt(String strSql) {
		int i = 0;
		log.info("Sql:" + strSql);
		tmpTime = System.currentTimeMillis();
		try {
			if (dbcon == null)
				newConnection();
			stmt = dbcon.createStatement();
			rs = stmt.executeQuery(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime)+ "ms");
			if (!rs.next()) {
				i = 0;
			}
			i = rs.getInt(1);			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Error SQL:" + strSql, e);
		} finally {
			close();
		}
		return i;
	}

	public String getString(String strSql) {
		String rsStr = "";
		log.info("Sql:" + strSql);
		tmpTime = System.currentTimeMillis();
		if (dbcon == null)
			newConnection();
		try {
			stmt = dbcon.createStatement();
			rs = stmt.executeQuery(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime)+ "ms");
			if (rs.next()) {
				rsStr = rs.getString(1);
			}
		} catch (SQLException e) {
			log.error("Error SQL:" + strSql, e);
			e.printStackTrace();
		}finally {
			close();
		}
		return "";
	}

	public int getCountQuery(String strSql) {
		int count = 0;
		log.info("Sql:" + strSql);
		try {
			tmpTime = System.currentTimeMillis();
			if (dbcon == null)
				newConnection();
			stmt = dbcon.createStatement(1004, 1008);
			rs = stmt.executeQuery(strSql);
			log.info("Time:" + (System.currentTimeMillis() - tmpTime) + "ms");
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			log.error("Error SQL:" + strSql, e);
			e.printStackTrace();
		} finally {
			close();
		}
		return count;
	}

	public void close() {
		try {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
			if (dbcon != null)
				dbcon.close();
			dbcon = null;
		} catch (Exception e) {
			log.error("Close the databse Connection fail:", e);
		}
	}

	private void newConnection() {
		String driver = dbSrc.getDriver();
		String url = dbSrc.getUrl();
		String user = dbSrc.getUname();
		String password = dbSrc.getPassword();
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			log.error("load the dataBase Driver fail", e);
		}
		try {
			dbcon = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			log.error("database Connection fail", e);
		}
	}

}