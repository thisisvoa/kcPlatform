package com.casic27.platform.util.jdbc.dbType;

import java.sql.ResultSet;
import java.util.Map;

public class Oracle implements DataBase {

	@Override
	public DatabaseType getDatabaseType() {
		
		return DataBase.DatabaseType.ORACLE;
	}

	@Override
	public String getConcateOperator() {
		
		return "";
	}

	@Override
	public String getUrlTemplate() {
		
		return "jdbc:oracle:thin:@<server>:<dbname>";
	}

	@Override
	public String getDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public String getAccessUrl(String dbname, String ip, Integer port,Map< String, Object> argsMap) {
		String url = getUrlTemplate().replaceAll("<dbname>", dbname).replaceAll("<server>", ip + ":" + port);		
        String arrIPs[] = ip.split(",");
        String szDescript = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(LOAD_BALANCE=yes)";
        for(int ii = 0; ii < arrIPs.length; ii++)
        {
            szDescript = szDescript + "(ADDRESS=(PROTO=TCP)(HOST=" + arrIPs[ii];
            szDescript = szDescript + ")(protocol=tcp)(port=" + port + "))";
        }

        szDescript = szDescript + ")(CONNECT_DATA=(SERVICE_NAME=" + dbname + "))))";
        url = szDescript;
        if(dbname.length() > 4)
        {
            String oracleflag = dbname.substring(0, 4);
            if(oracleflag.equals("oci_"))
            {
                dbname = dbname.substring(4);
                String ociurl = "(DESCRIPTION=(ADDRESS_LIST=(LOAD_BALANCE=yes)";
                for(int ii = 0; ii < arrIPs.length; ii++)
                {
                    ociurl = ociurl + "(ADDRESS=(PROTO=TCP)(HOST=" + arrIPs[ii];
                    ociurl = ociurl + ")(protocol=tcp)(port=" + port + "))";
                }

                ociurl = ociurl + ")(CONNECT_DATA=(SERVICE_NAME=" + dbname + ")))";
                url = "jdbc:oracle:oci:@" + ociurl;
            }
        }
        return url;
    }

	@Override
	public String getTableColumnsNameSql(String tableName) {
		
		return "select COLUMN_NAME AS COLUMN_NAME,comments as COMMENTS from user_col_comments WHERE TABLE_NAME = '" + tableName + "' ";
	}

	@Override
	public String getTablesSql() {

		return " select TABLE_NAME from USER_TABLES ";
	}

}
