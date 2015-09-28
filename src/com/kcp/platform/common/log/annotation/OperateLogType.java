package com.kcp.platform.common.log.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kcp.platform.util.JsonParser;

/**
 * 操作类型枚举
 * @author Administrator
 *
 */
public enum OperateLogType {
	/**
	 * 查询
	 */
	QUERY("1","查询"),
	/**
	 * 新增
	 */
	INSERT("2","新增"),
	
	/**
	 * 删除
	 */
	DELETE("3","删除"),
	
	/**
	 * 修改
	 */
	UPDATE("4","修改"),
	
	/**
	 * 授权
	 */
	AUTH("5","授权"),
	
	/**
	 * 导出/下载
	 */
	EXPORT("6","导出/下载");
	
	private final String value;
	
	private final String desc;

	OperateLogType(String value,String desc) { 
		this.value = value; 
		this.desc = desc;
	}
	
	public String value() { return this.value; }
	
	public String desc() { return this.desc; }
	
	public static String desc(String value){
		OperateLogType[] logTypes = OperateLogType.values();
		for(OperateLogType logType:logTypes)
			if(logType.value().equals(value))
				return logType.desc();
		return "";
	}
	
	public static Map<String, String> toMap(){
        Map<String, String> result = new HashMap<String, String>();
        for(OperateLogType sysLogType : OperateLogType.values()){
            result.put(sysLogType.value(), sysLogType.desc());
        }
        return result;
    }
    
    public static List<Map<String, String>> toList(String valueKey, String labelKey){
        List<Map<String, String>> result = new ArrayList<Map<String,String>>();
        for(OperateLogType sysLogType : OperateLogType.values()){
            Map<String, String> map = new HashMap<String, String>();
            map.put(valueKey, sysLogType.value());
            map.put(labelKey, sysLogType.desc());
            result.add(map);
        }
        return result;
    }
    
    public static String toMapJson(){
        return JsonParser.convertObjectToJson(OperateLogType.toMap());
    }
    
    public static String toListJson(String valueKey, String labelKey){
        return JsonParser.convertArrayObjectToJson(OperateLogType.toList(valueKey, labelKey));
    }
}
