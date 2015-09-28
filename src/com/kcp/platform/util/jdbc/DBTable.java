package com.kcp.platform.util.jdbc;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 *类描述：
 * 
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
