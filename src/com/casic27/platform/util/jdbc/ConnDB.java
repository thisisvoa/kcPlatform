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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.casic27.platform.util.jdbc.dbType.DataBase;
import com.casic27.platform.util.jdbc.dbType.Oracle;

/**
 * 
 * 类描述：
 * 
 * @Author： 宗斌(zongbin@casic27.com)
 * @Version：1.0
 */
public class ConnDB
{
    private static final Logger log = Logger.getLogger(com.casic27.platform.util.jdbc.ConnDB.class);
    public Connection cn;
    
    public Statement stmt;
    
    public ResultSet rs;
    
    public String errorMsg;

	public boolean Open(DBSrc dbSrc)
    {
        String uname = dbSrc.getUname();
        String password = dbSrc.getPassword();
        String url = dbSrc.getUrl();
        String className = dbSrc.getDriver();       
        try
        {
            Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            log.error("连接到据库失败:无法加载JDBC驱动:" + className);
            errorMsg = "连接到据库失败:无法加载JDBC驱动:" + className;
            return false;
        }
        try
        {
            log.debug(url);
            cn = DriverManager.getConnection(url, uname, password);
            stmt = cn.createStatement();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            log.error("连接到据库失败:" + url + "  user:" + uname + "  password:" + password);
            errorMsg = "连接到据库失败:" + url + "  user:" + uname + "  password:" + password;
            return false;
        }
        return true;
    }

    public void close()
    {
        try
        {
            if(rs != null)
                rs.close();
            rs = null;
            if(stmt != null)
                stmt.close();
            stmt = null;
            if(cn != null)
                cn.close();
            cn = null;
        }
        catch(Exception ex)
        {
            log.error("Close the databse Connection fail:", ex);
            errorMsg = "关闭数据库连接失败！";
        }
    }
    
    public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
     * 不带参数
     * @param queryString
     * @return
     */
    public ResultSet query(String queryString)
    {
        try
        {
            log.debug("执行的查询：" + queryString);
            rs = stmt.executeQuery(queryString);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    
//    public  ResultSet query(String queryString, Object[] params) throws SQLException{
//    	stmt = cn.prepareStatement(queryString);
//    	PreparedStatement ps = (PreparedStatement)stmt;
//		if(params != null){
//			for (int i = 0; i < params.length; i++) {
//				ps.setObject(i+1, params[i]);
//			}
//		}
//        try
//        {
//            log.debug("执行的查询：" + queryString);
//            rs = ps.executeQuery(queryString);
//        }
//        catch(SQLException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//        return rs;
//	}

    public JSONArray RStoJSONArray(JSONArray ja)
    {
    	ResultSet rs = this.rs;
        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(JSONObject jo; rs.next(); ja.add(jo))
            {
                jo = new JSONObject();
                int colCount = rsmd.getColumnCount();
                String strColumnName = "";
                String strColumnValue = "";
                for(int i = 1; i <= colCount; i++)
                    try
                    {
                        strColumnName = rsmd.getColumnName(i);
                        strColumnValue = rs.getObject(i) != null ? rs.getString(i) : strColumnValue;
                        if(strColumnValue.indexOf("\n") > 0)
                            strColumnValue = strColumnValue.substring(0, strColumnValue.indexOf("\n"));
                        jo.put(strColumnName, strColumnValue);
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return ja;
        }
        return ja;
    }

    public JSONArray getTableColumnsName(DBSrc dbSrc, String tableName)
    {
       ConnDB db = new ConnDB();
       db.Open(dbSrc);
       DataBase dbType = dbSrc.getDbType();
       JSONArray ja = new JSONArray();
        try {
			String queryString = dbType.getTableColumnsNameSql(tableName);
			this.rs = db.query(queryString);
			db.RStoJSONArray(ja);
			db.close();			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ja;
    }

    public JSONArray getTables(DBSrc dbSrc)
    {   	
         ConnDB db = new ConnDB();
         db.Open(dbSrc);
         DataBase dbType = dbSrc.getDbType();
         JSONArray ja = new JSONArray();
          try {
  			String queryString = dbType.getTablesSql();
  			this.rs = db.query(queryString);
  			db.RStoJSONArray(ja);
  			db.close();  			
  		} catch (JSONException e) {
  			e.printStackTrace();
  		}
  		return ja;
    }

    public Connection getCn()
    {
        return cn;
    }

    public void setCn(Connection cn)
    {
        this.cn = cn;
    }

    public static void main(String args[])
        throws SQLException, JSONException
    {
    	DBSrc dbSrc = new DBSrc(new Oracle(), "本地数据库", "zngjfx", "zngjfx", "orcl", "192.168.0.29", 1521);
    	ConnDB connDb = new ConnDB();
    	boolean isAccessSuccess = connDb.Open(dbSrc);    	
    	JSONArray ja = connDb.getTables(dbSrc);
    	System.out.println(ja.toString());
    }



}
