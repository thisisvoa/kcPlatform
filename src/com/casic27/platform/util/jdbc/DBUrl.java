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
