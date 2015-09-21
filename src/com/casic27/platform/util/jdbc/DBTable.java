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

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
public class DBTable
{
	/**
	 * 表id
	 */
	private String id;
	/**
	 * 名称
	 */
    private String name;
	/**
	 * 数据库表名（数据源中的真实表名）
	 */
    private String tablename;
    /**
     * 表面
     */
    private String table;
    /**
     * 数据源
     */
    private String dbsrc;
    
    
    public DBTable(JSONObject tableJson)
        throws JSONException
    {
    	id = tableJson.getString("id");
        name = tableJson.getString("name");
        tablename = tableJson.getString("tablename");
        table = tableJson.getString("table");
        dbsrc = tableJson.getString("dbsrc");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTablename()
    {
        return tablename;
    }

    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public String getDbsrc()
    {
        return dbsrc;
    }

    public void setDbsrc(String dbsrc)
    {
        this.dbsrc = dbsrc;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


}
