package com.kcp.platform.sys.constants;

import com.kcp.platform.util.jdbc.dbType.DataBase;
import com.kcp.platform.util.jdbc.dbType.Oracle;

/**
 *
 *类描述：基础的常量定义信息
 * 
 *@Version：1.0
 */
public class CommonConst {
	/**
     * 登陆用户信息保存在session中的key
     */
    public static final String SESSION_USER_KEY = "$kcp_user$";
    
    /**
     * 登陆用户部门信息保存在session中的key
     */
    public static final String SESSION_ORG_KEY = "$kcp_org$";
    
    /**
     * 登录客户端IP
     */
    public static final String SESSION_REMOTE_IP_KEY = "$kcp_clientIp$";
    
    /**
     * 登录客户端IP
     */
    public static final String SESSION_ID = "$kcp_sessionId$";
    
    /**
     * 当前登录用户角色
     */
    public static final String SESSION_ROLE_LIST_KEY = "$kcp_roleList$";
    
    public static final String AJAX_REQUEST_FLAG = "_ajax_request_flag_";
    
    public static final String AJAX_UNLOGIN_FLAG = "_ajax_unlogin_flag_";
    
    /**
     * 返回引用页标识
     */
    public static final String LOGIN_RRETURN_URL_KEY = "return.url.key";
    
    /**
     * 超级管理员登陆用户名
     */
    public static final String SUPERADMIN_LOGINID = "admin";
    
    /**
     * 启用标示代码
     */
    public static final String ENABLE_FLAG = "1";
    
    /**
     * 禁用标示代码 
     */
    public static final String DISABLE_FLAG = "0";
    /**
     * 是标示
     */
    public static final String YES_FLAG = "1";
   /**
    * 否标示
    */
    public static final String NO_FLAG = "0";
    /**
     * 默认树根节点ID为“0”
     */
    
    public static final String TREE_ROOT_ID = "0";
    
    /**
     * 新增模式
     */
    public static final String ADD_OPERMODEL = "ADD";
    
    /**
     * 修改模式
     */
    public static final String UPDATE_OPERMODEL = "UPDATE";
    
    /**
     * 查看模式
     */
    public static final String VIEW_OPERMODEL = "VIEW";
    
    /**
     * 初始化密码
     */
    public static final String INIT_PASSWORD = "123456";
    
    /**
     * 操作成功
     */
    public static final String OPER_SUCCESS = "SUCCESS";
    
    /**
     * 操作失败
     */
    public static final String OPER_FAIL = "FAIL";
    /**
     * EXCEL一个工作页最大导出记录数为65536
     */
    public static final int EXCEL_MAX_EXPORT_NUM = 65535;
    
    /**
     * 系统使用数据库类型
     */
    public static final DataBase.DatabaseType DATABASE_TYPE = DataBase.DatabaseType.ORACLE;
    
    /**
     * 否
     */
    public static final String NO = "0";
    
    /**
     * 是
     */
    public static final String YES = "1";
}
