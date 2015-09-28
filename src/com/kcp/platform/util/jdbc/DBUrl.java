package com.kcp.platform.util.jdbc;

import net.sf.json.JSONObject;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@kcp.com)
 *@Version：1.0
 */
public class DBUrl
{

    private String id;
    private String name;
    private String url;
    private String driver;
    
    public DBUrl()
    {
        id = "";
        name = "";
        url = "";
        driver = "";
    }

    public DBUrl(JSONObject urlJson)
    {
        id = urlJson.getString("id");
        name = urlJson.getString("name");
        url = urlJson.getString("url");
        driver = urlJson.getString("driver");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }
}
