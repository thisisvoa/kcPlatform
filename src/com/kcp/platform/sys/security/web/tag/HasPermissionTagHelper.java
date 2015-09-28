package com.kcp.platform.sys.security.web.tag;

import com.kcp.platform.sys.security.context.SecurityContext;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class HasPermissionTagHelper {
	
	/**
	 * 判断当前用户是否有某权限
	 */
	public static boolean hasPermission(String permCode){
		return SecurityContext.getSubject().isPermitted(permCode);
	}
}
