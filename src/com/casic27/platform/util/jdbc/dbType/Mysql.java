package com.casic27.platform.util.jdbc.dbType;

import java.sql.ResultSet;
import java.util.Map;

import com.casic27.platform.util.StringUtils;

public class Mysql implements DataBase {

	@Override
	public DatabaseType getDatabaseType() {
		
		return DataBase.DatabaseType.MYSQL;
	}

	@Override
	public String getConcateOperator() {		
		return "";
	}

	@Override
	public String getUrlTemplate() {
		return null;
	}

	@Override
	public String getAccessUrl(String dbname, String ip,Integer port,Map< String, Object> argsMap) {
		String url = getUrlTemplate().replaceAll("<dbname>", dbname).replaceAll("<server>", ip + ":" + port);
		String charset = StringUtils.getNullBlankStr(argsMap.get("charset"));
		 if(!charset.equals(""))
	        {
	            if(url.indexOf("?") >= 0)
	                url = url.lastIndexOf("?") < 0 ? url + "&" : url;
	            else
	                url = url + "?";
	            url = url + "useUnicode=true&characterEncoding=" + charset;
	        }
		
		return url;
	}

	@Override
	public String getDriver() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public String getTableColumnsNameSql(String tableName) {
		return " SHOW COLUMNS  from " + tableName;
	}

	@Override
	public String getTablesSql() {
		// TODO Auto-generated method stub
		return " SHOW TABLES ";
	}

}
