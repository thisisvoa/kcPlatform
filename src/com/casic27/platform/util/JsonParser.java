/**
 * @(#)com.casic27.platform.util.JsonParser.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	专门处理json格式的解析
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class JsonParser {
	/**
     * JSON处理含有嵌套关系对象，避免出现异常：net.sf.json.JSONException: There is a cycle in the hierarchy的方法
     * 注意：这样获得到的字符串中，引起嵌套循环的属性会置为null
     * 
     * @param obj
     * @return
     */    
    public static JSONObject getJsonObject(Object obj){
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        return JSONObject.fromObject(obj, jsonConfig);
    }

    public static JSONObject getJsonObject(Object obj,boolean ignoreNull) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setIgnoreTransientFields(true);
        if (ignoreNull) {
            jsonConfig.setJsonPropertyFilter(new PropertyFilter() {

                public boolean apply(Object o, String key, Object value) {
                    if (value == null) {
                        return true;
                    }
                    if (value instanceof Integer) {
                        if ((Integer)value ==0) {
                            return true;
                        }
                    }
                    if (value instanceof Double) {
                        if ((Double)value == 0) {
                            return true;
                        }
                    }
                    if (value instanceof String) {
                        if (((String) value).trim().length() == 0) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        return JSONObject.fromObject(obj, jsonConfig);
    }
    
    /**
     * JSON处理含有嵌套关系对象，避免出现异常：net.sf.json.JSONException: There is a cycle in the hierarchy的方法
     * 注意：这样获得到的字符串中，引起嵌套循环的属性会置为null
     * 
     * @param obj
     * @return
     */
    public static JSONArray getJsonArray(Object obj){
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        
        return JSONArray.fromObject(obj, jsonConfig);
    }
    
    /**
     * 解析JSON字符串成一个MAP
     * 
     * @param jsonStr json字符串，格式如： {dictTable:"BM_XB",groupValue:"分组值"}
     * @return
     */
    public static Map<String, String> parseJsonStr(String jsonStr){

        if(StringUtils.isEmpty(jsonStr))
            return null;

        Map<String, String> result = new LinkedHashMap<String, String>();

        JSONObject jsonObj = JsonParser.getJsonObject(jsonStr);

        for(Object key : jsonObj.keySet()){
            result.put((String)key, jsonObj.get(key).toString());
        }
        return result;
    }

    /**
     * 解析JSONArray或JSONObject,将嵌套对象转成map或list
     * @param obj
     * @return
     */
    public static Object parseJsonObject(Object obj){
        if(obj == null)
            return null;
        if(obj instanceof JSONObject){
            JSONObject jsonObject = (JSONObject)obj;
            Map<String,Object> map = new LinkedHashMap<String, Object>();
            for(Object key : jsonObject.keySet()){
                map.put((String) key, parseJsonObject(jsonObject.get(key)));
            }
            return map;
        }
        if( obj instanceof JSONArray){
            JSONArray jsonArray = (JSONArray)obj;
            List<Object> list = new ArrayList<Object>();
            for(Object o : jsonArray){
                list.add(parseJsonObject(o));
            }
            return list;
        }
        return obj;
    }

    /**
     * 将JSON串转为一个对象的集合
     * @param jsonStr  JSON字符串
     * @param objClass  目标类
     * @param <T>
     * @return
     * add by chenxh 2009-07-14
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T>  toBeans(String jsonStr,Class<T> objClass) {
        if (jsonStr == null) {
            return null;
        }
        JSONArray jsonArray = JsonParser.getJsonArray(jsonStr);
        return (List<T>) JSONArray.toCollection(jsonArray, objClass);
    }

    /**
     * 解析Json字符串，自动转换成一个对象的集合
     * 
     * @param jsonStr  如：[{tabId:'tab_tdjxjh',display:'true',index:'1'},{tabId:'tab_tdjxjh',display:'true',index:'1'},{tabId:'tab_tdjxjh',display:'true',index:'1'}]
     * @param objClass  DdrzTabConf.class
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Collection parseJsonStr(String jsonStr, Class objClass){
        
        if(StringUtils.isEmpty(jsonStr))
            return null;
        
        JSONArray jsonArray = JsonParser.getJsonArray(jsonStr);
        
        return JSONArray.toCollection(jsonArray,objClass);
    }
    /**
     * 解析Json字符串，自动转换成一个对象
     *
     * @param jsonStr  如：{tabId:'tab_tdjxjh',display:'true',index:'1'}
     * @param objClass  目标对象的类型
     * @return
     * add by chenxh  2009-07-14
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String jsonStr, Class<T> objClass){
        if(StringUtils.isEmpty(jsonStr)){
             return null;
        }else{
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            return (T)JSONObject.toBean(jsonObject,objClass);
        }
    }

    public static <T> T toBean(String jsonStr, Class<T> objClass,Map map){
        if(StringUtils.isEmpty(jsonStr)){
             return null;
        }else{
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            return (T)JSONObject.toBean(jsonObject,objClass,map);
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static List<JSONObject> parseJsonArray(String jsonArrayStr){
        
        if(StringUtils.isEmpty(jsonArrayStr))
            return null;
        
        List<Object> list = (List<Object>)JSONArray.toCollection(getJsonArray(jsonArrayStr));
        List<JSONObject> result = new ArrayList<JSONObject>();
        
        for(Object obj : list){
            JSONObject jsonObj = getJsonObject(obj);
            result.add(jsonObj);
        }
        
        return result;
    }

     public static List parseJsonArray(String jsonArrayStr,Class clazz){

        if(StringUtils.isEmpty(jsonArrayStr))
            return null;

        List list = (List)JSONArray.toCollection(getJsonArray(jsonArrayStr),clazz);

        return list;
    }

    /**
     * 把对象转换成为Json字符串
     *
     * @param obj
     * @return
     */
    public static String convertObjectToJson(Object obj){
        if(obj == null){
            throw new IllegalArgumentException("对象参数不能为空。");
        }
        JSONObject jsonObject = JsonParser.getJsonObject(obj);
        return jsonObject.toString();
    }

    /**
     * 把对象转换成为Json字符串
     *
     * @param obj
     * @param ignoredNull
     * @return
     */
    public static String convertObjectToJson(Object obj,boolean ignoredNull){
        if(obj == null){
            throw new IllegalArgumentException("对象参数不能为空。");
        }
        JSONObject jsonObject = JsonParser.getJsonObject(obj,ignoredNull);
        return jsonObject.toString();
    }
    
    /**
     * 把对象转换成为Json字符串
     *
     * @param obj
     * @return
     */
    public static String convertArrayObjectToJson(Object obj){
        if(obj == null){
            throw new IllegalArgumentException("对象参数不能为空。");
        }
        JSONArray jsonArray = JsonParser.getJsonArray(obj);
        return jsonArray.toString();
    }
    
    public static void main(String[] args){
    	String s = "[{tabId:\"tab_tdjxjh\",display:'true',index:'1'},{tabId:\"tab_tdjxjh\",display:\"true\",index:'1'}]";
    	List<JSONObject> list =  parseJsonArray(s);
    	System.out.println(list.size());
//    	JSONObject jso = new JSONObject();
//    	jso.put("", "2");
//    	System.out.println(convertObjectToJson(jso, true));
    	
    }
}
