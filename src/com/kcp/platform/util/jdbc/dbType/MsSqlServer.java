package com.kcp.platform.util.jdbc.dbType;

import java.sql.ResultSet;
import java.util.Map;

public class MsSqlServer implements DataBase {

	@Override
	public DatabaseType getDatabaseType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConcateOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrlTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccessUrl(String dbName, String ip, Integer port,
			Map<String, Object> argsMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDriver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableColumnsNameSql(String tableName) {
		// TODO Auto-generated method stub
		return "select   a.name as COLUMN_NAME  from   syscolumns   a   ,sysobjects   b   where   a.id=b.id   and   b.name='" + tableName + "'";
	}

	@Override
	public String getTablesSql() {
		// TODO Auto-generated method stub
		return " select name as TABLE_NAME from sysobjects where xtype='U' ";
	}

}
