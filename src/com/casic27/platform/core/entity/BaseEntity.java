/**
 * @(#)com.casic27.platform.core.entity.BaseEntity.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 *类描述：所有领域对象的基类
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 7948835957778146226L;

	public String toString() {
	        return (new ReflectionToStringBuilder(this) {
	            protected boolean accept(Field f) {
	                if (f.getType().isPrimitive() || f.getType() == String.class) {
	                    return true;
	                } else {
	                    return false;
	                }
	            }
	        }).toString();
	    }
}
