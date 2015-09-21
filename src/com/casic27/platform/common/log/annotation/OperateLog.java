package com.casic27.platform.common.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
	/**
	 * 日志操作类型 <br>
	 * 1:查询 2:新增 3:删除 4:修改
	 */
	OperateLogType type() default OperateLogType.QUERY;
	
	/**
	 * 模块名称
	 * @return
	 */
	String moduleName() default "";
	
	/**
	 * 操作描述
	 * @return
	 */
	String operateDesc() default "";
	
	boolean isRecord() default true;
}
