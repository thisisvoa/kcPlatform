/**
 * @(#)com.casic27.platform.base.cache.exception.InvalidCommandException.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.exception;

import com.casic27.platform.core.exception.BusinessException;



/**
 * 类描述：
 * 发布了不合法的缓存过期命令
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Apr 16, 2012
 */
public class InvalidCommandException extends BusinessException{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCommandException(Throwable cause) {
        super(cause);
    }
}
