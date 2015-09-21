/**
 * @(#)com.casic27.platform.base.log.annotation.SysLogAnnotationParser.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.log.annotation;

import java.lang.reflect.AnnotatedElement;

import com.casic27.platform.common.log.interceptor.MethodSysLogDefinition;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
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
