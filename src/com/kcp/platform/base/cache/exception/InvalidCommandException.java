
package com.kcp.platform.base.cache.exception;

import com.kcp.platform.core.exception.BusinessException;



/**
 * 类描述：
 * 发布了不合法的缓存过期命令
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
