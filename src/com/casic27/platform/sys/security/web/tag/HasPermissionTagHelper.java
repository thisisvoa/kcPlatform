/**
 * @(#)com.casic27.platform.sys.security.web.tag.HasPermissionTagHelper.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-16
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.sys.security.web.tag;

import com.casic27.platform.sys.security.context.SecurityContext;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class HasPermissionTagHelper {
	
	/**
	 * 判断当前用户是否有某权限
	 */
	public static boolean hasPermission(String permCode){
		return SecurityContext.getSubject().isPermitted(permCode);
	}
}
