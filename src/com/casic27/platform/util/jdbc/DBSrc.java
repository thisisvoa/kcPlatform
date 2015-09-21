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

import com.casic27.platform.util.jdbc.dbType.DataBase;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
public class DBSrc
{

	/**
	 * 数据源名称
	 */
    private String name;

	/**
	 * 数据源用户名
	 */
    private String uname;
	/**
	 * 数据源密码
	 */
    private String password;
	/**
	 * 数据源实例名
	 */
    private String dbname;
	/**
	 * 数据源IP地址
	 */
    private String ip;
	/**
	 * 数据源端口号
	 */
    private Integer port;
    /**
     * 数据源类型（oracle、mysql、sqlserver等）
     */
    private DataBase dbType;
    /**
     * 数据库连接URL
     */
    private String url;
    

	/**
     * 数据库驱动程序
     */
    private String driver;
   
    
    public DBSrc(DataBase dbType ,String name ,String uname ,String password ,String dbname ,String ip ,Integer port)
        throws JSONException
    {
    	this.dbType = dbType;
        this.name = name;
        this.uname = uname;
        this.password = password;
        this.dbname = dbname;
        this.ip = ip;
        this.port = port;
        url = dbType.getAccessUrl(dbname, ip, port,null);
        driver = dbType.getDriver();    
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUname()
    {
        return uname;
    }

    public void setUname(String uname)
    {
        this.uname = uname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDbname()
    {
        return dbname;
    }

    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }
    
	public DataBase getDbType() {
		return dbType;
	}

	public void setDbType(DataBase dbType) {
		this.dbType = dbType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}


}