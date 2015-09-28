package com.kcp.platform.common.log.annotation;

import java.lang.reflect.AnnotatedElement;

import com.kcp.platform.common.log.interceptor.MethodSysLogDefinition;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
 *@Version：1.0
 */
public interface SysLogAnnotationParser {
	/**
	 * 用于将注解信息转化为MethodLogDefinition类型，便于进行解析
	 * @param ae
	 * @return
	 */
	MethodSysLogDefinition parseLogAnnotation(AnnotatedElement ae);
}
