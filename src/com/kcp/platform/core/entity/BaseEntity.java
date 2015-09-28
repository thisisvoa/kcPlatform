package com.kcp.platform.core.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 *类描述：所有领域对象的基类
 * 
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
