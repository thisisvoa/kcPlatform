package com.kcp.platform.common.log.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;

public class MyBatisSql {
	/**
	 * 运行期 sql
	 */
	private String sql;

	/**
	 * 参数 数组
	 */
	private Object[] parameters;

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		if(parameters==null){
			return sql;
		}
		sql = sql.replaceAll("\\s|\t|\r|\n", " ");
		for(int i=0;i<parameters.length;i++){
			Object obj = parameters[i];
			if(obj!=null){
				if(obj instanceof String){
					sql = sql.replaceFirst("\\?", "'"+obj.toString()+"'");
				}else if(obj instanceof Integer || obj instanceof Double || obj instanceof Float){
					sql = sql.replaceFirst("\\?", obj.toString());
				}else if(obj instanceof Date){
					sql = sql.replaceFirst("\\?", "'"+DateUtils.parseDate2String((Date)obj, "yyyy-MM-dd HH:mm:ss.S")+"'");
				}else{
					sql = sql.replaceFirst("\\?", "'"+obj.toString()+"'");
				}
			}else{
				sql = sql.replaceFirst("\\?", "null");
			}
		}
		return sql;
	}

}
