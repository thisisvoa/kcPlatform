package com.kcp.platform.util;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

/**
 * 响应AJAX请求返回的JSON字符串
 * 
 */
public class AjaxJsonResponse
{
    /**
     * JSON输出文档的根节点标识
     */
    public static final String JSON_ROOT_NODE = "kpm-response";
    
    /**
     * JSON输出文档的结果节点标识
     */
    public static final String JSON_RESULT_NODE = "result";
    
    /**
     * JSON输出文档的原因节点标识
     */
    public static final String JSON_REASON_NODE = "reason";
    
    /**
     * JSON输出文档的失败原因：登录超时
     */
    public static final String JSON_REASON_LOGON_TIMEOUT = "unlogon";
    
    /**
     * JSON输出文档的失败原因：拒绝访问
     */
    public static final String JSON_REASON_ACCESS_DENY = "access-deny";
    
    /**
     * JSON输出文档的失败原因：违反唯一约束
     */
    public static final String JSON_REASON_UNIQUE_CONTRAINT = "unique-contraint";
    
    /**
     * JSON输出文档的失败原因：违反了业务逻辑约束
     */
    public static final String JSON_REASON_BUSINESS_CONTRAINT = "business-contraint";
    
    /**
     * JSON输出文档系统的失败原因：异常
     */
    public static final String JSON_REASON_SYSTEM_EXCEPTION = "system-exception";
    
    /**
     * JSON输出文档的消息节点
     */
    public static final String JSON_MSG_NODE = "msg";
    
    /**
     * JSON输出文档的自定义节点
     */
    public static final String JSON_CUSTOM_CONTENT_NODE = "custom-content";
    
    /**
     * 操作成功的JSON输出字符串
     */
    public static final String SUCCESSFULLY_STRING;
    
    /**
     * 登录超时的JSON输出字符串
     */
    public static final String LOGON_TIMEOUT_STRING;
    
    /**
     * 拒绝访问的JSON输出字符串
     */
    public static final String ACCESS_DENY_STRING;
    
    /**
     * 违反唯一约束的JSON输出字符串
     */
    public static final String UNIQUE_CONTRAINT_STRING;
    
    /**
     * 违反了业务逻辑的JSON输出字符串
     */
    public static final String BUSINESS_CONTRAINT_STRING;
    
    /**
     * 系统异常的JSON输出字符串
     */
    public static final String SYSTEM_EXCEPTION_STRING;
    
    /**
     * 初始化一些响应AJAX请求返回的JSON字符串
     */
    static {
        SUCCESSFULLY_STRING = generateSuccessfulOperationDoc();
        LOGON_TIMEOUT_STRING = generateLogonTimeoutDoc();
        ACCESS_DENY_STRING = generateAccessDenyDoc();
        UNIQUE_CONTRAINT_STRING = generateUniqueContraintDoc();
        BUSINESS_CONTRAINT_STRING = generateBusinessContraintDoc();
        SYSTEM_EXCEPTION_STRING = generateSystemExceptionDoc();
    }
    
    /*-------------------- construtors --------------------*/
    
    /**
     * 
     */
    public AjaxJsonResponse()
    {
        // TODO Auto-generated constructor stub
    }
    
    /*----------------- public methods --------------------*/
    
    /**
     * 向客户端输出JSON字符串 
     */
    public static void outputJson(HttpServletResponse response, String json) throws Exception {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }
    
    /**
     * 生成操作成功JSON文档。
     *
     * @return
     */
    public static String generateSuccessfulOperationDoc(){        
        return generateJsonDocument(generateResultJson(true));
    }
    
    /**
     * 生成操作成功JSON文档。
     * 
     * @param customContent 自定义返回的内容
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String generateSuccessfulOperationDoc(Map customContent){
        JSONObject so = generateResultJson(true);
                   so.put(JSON_CUSTOM_CONTENT_NODE, customContent);
                   
        return generateJsonDocument(so);
    }
    
    /**
     * 生成操作成功JSON文档。
     * 
     * @param customContent 自定义返回的内容
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String generateSuccessfulOperationDoc(Map customContent,String msg){
        JSONObject so = generateResultJson(true);
        if(customContent!=null && customContent.size()>0)
            so.put(JSON_CUSTOM_CONTENT_NODE, customContent);
        so.put(JSON_MSG_NODE,msg);
                   
        return generateJsonDocument(so);
    } 
    
    /**
     * 生成登录超时JSON文档。
     *
     * @return
     */
    public static String generateLogonTimeoutDoc(){
        
        JSONObject lto = generateResultJson(false);
                   lto.put(JSON_REASON_NODE, JSON_REASON_LOGON_TIMEOUT);
        
        return generateJsonDocument(lto);
    }
    
    /**
     * 生成拒绝访问JSON文档。
     *
     * @return
     */
    public static String generateAccessDenyDoc(){
        
        JSONObject ad = generateResultJson(false);
                   ad.put(JSON_REASON_NODE, JSON_REASON_ACCESS_DENY);
        
        return generateJsonDocument(ad);
    }
    
    /**
     * 生成违反唯一约束JSON文档。
     *
     * @return
     */
    public static String generateUniqueContraintDoc(){
        
        JSONObject uc = generateResultJson(false);
                   uc.put(JSON_REASON_NODE, JSON_REASON_UNIQUE_CONTRAINT);
            
        return generateJsonDocument(uc);
    }
    
    /**
     * 生成违反唯一约束JSON文档。
     * 
     * @param msg 提示信息
     * @return
     */
    public static String generateUniqueContraintDoc(String msg){
        
        JSONObject uc = generateResultJson(false);
                   uc.put(JSON_REASON_NODE, JSON_REASON_UNIQUE_CONTRAINT);
                   uc.put(JSON_MSG_NODE, msg);
            
        return generateJsonDocument(uc);
    }
    
    /**
     * 生成违反唯一约束JSON文档。
     * 
     * @param customContent 自定义内容
     * @return
     */
    public static String generateUniqueContraintDoc(Map customContent){
        
        JSONObject uc = generateResultJson(false);
                   uc.put(JSON_REASON_NODE, JSON_REASON_UNIQUE_CONTRAINT);
                   uc.put(JSON_CUSTOM_CONTENT_NODE, customContent);
            
        return generateJsonDocument(uc);
    }
    
    /**
     * 生成违反唯一约束JSON文档。
     * 
     * @param msg 提示信息
     * @param customContent 自定义内容
     * @return
     */
    public static String generateUniqueContraintDoc(String msg, Map customContent){
        
        JSONObject uc = generateResultJson(false);
                   uc.put(JSON_REASON_NODE, JSON_REASON_UNIQUE_CONTRAINT);
                   uc.put(JSON_MSG_NODE, msg);
                   uc.put(JSON_CUSTOM_CONTENT_NODE, customContent);
            
        return generateJsonDocument(uc);
    }
    
    /**
     * 生成违反业务逻辑约束JSON文档。
     *
     * @return
     */
    public static String generateBusinessContraintDoc(){
        
        JSONObject bc = generateResultJson(false);
                   bc.put(JSON_REASON_NODE, JSON_REASON_BUSINESS_CONTRAINT);
       
        return generateJsonDocument(bc);
    }
    
    /**
     * 生成违反业务逻辑约束JSON文档。
     * 
     * @param msg 提示信息
     * @return
     */
    public static String generateBusinessContraintDoc(String msg){
        
        JSONObject bc = generateResultJson(false);
                   bc.put(JSON_REASON_NODE, JSON_REASON_BUSINESS_CONTRAINT);
                   bc.put(JSON_MSG_NODE, msg);
       
        return generateJsonDocument(bc);
    }
    
    /**
     * 生成违反业务逻辑约束JSON文档。
     * 
     * @param customContent 自定义内容
     * @return
     */
    public static String generateBusinessContraintDoc(Map customContent){
        
        JSONObject bc = generateResultJson(false);
                   bc.put(JSON_REASON_NODE, JSON_REASON_BUSINESS_CONTRAINT);
                   bc.put(JSON_CUSTOM_CONTENT_NODE, customContent);
       
        return generateJsonDocument(bc);
    }
    
    /**
     * 生成违反业务逻辑约束JSON文档。
     * 
     * @param msg 提示信息
     * @param customContent 自定义内容
     * @return
     */
    public static String generateBusinessContraintDoc(String msg, Map customContent){
        
        JSONObject bc = generateResultJson(false);
                   bc.put(JSON_REASON_NODE, JSON_REASON_BUSINESS_CONTRAINT);
                   bc.put(JSON_MSG_NODE, msg);
                   bc.put(JSON_CUSTOM_CONTENT_NODE, customContent);
       
        return generateJsonDocument(bc);
    }
    
    /**
     * 生成系统异常XML文档。
     *
     * @return
     */
    public static String generateSystemExceptionDoc(){
        
        JSONObject se = generateResultJson(false);
                   se.put(JSON_REASON_NODE, JSON_REASON_SYSTEM_EXCEPTION);
                   
        return generateJsonDocument(se);
    }
    
    /*---------------- private methods ------------------*/
    
    /**
     * 产生最终的JSON输出文档
     */
    private static String generateJsonDocument(JSONObject json){
        Map<String, Object> map = new HashMap<String, Object>();
                            map.put(JSON_ROOT_NODE, json);
        return JsonParser.getJsonObject(map).toString();
    }
    
    /**
     * 生成操作结果的JSON对象
     * 
     * @param result
     * @return
     */
    private static JSONObject generateResultJson(boolean result){
        JSONObject json = new JSONObject();
                   json.put(JSON_RESULT_NODE, result);
                   
        return json;
    }
}
