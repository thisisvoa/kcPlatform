/**
 * @(#)com.casic27.platform.base.log.annotation.SimpleLogingAnnotationParser.java
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

import java.io.Serializable;
import java.lang.annotation.Annotation;
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
@SuppressWarnings("serial")
public class SimpleLogingAnnotationParser implements SysLogAnnotationParser, Serializable {
	/**
	 * 用于将注解信息转化为MethodLogDefinition类型，便于进行解析
	 * @param ae
	 * @return
	 */
	public MethodSysLogDefinition parseLogAnnotation(AnnotatedElement ae){
		Log sysLog = findAnnotation(ae, Log.class);
		if (sysLog != null) {
			MethodSysLogDefinition definition = new MethodSysLogDefinition();
			definition.setModuleName(sysLog.moduleName());
			definition.setType(sysLog.type().value());
			definition.setOperateDesc(sysLog.operateDesc());
			definition.setUseSpel(sysLog.useSpel());
			return definition;
		}
		return null;
	}
	
	/**
	 * 根据注解类型，查找注解信息
	 * @param <T>
	 * @param ae
	 * @param annotationType
	 * @return
	 */
	private <T extends Annotation> T findAnnotation(AnnotatedElement ae, Class<T> annotationType) {
		T ann = ae.getAnnotation(annotationType);
		if (ann == null) {
			for (Annotation metaAnn : ae.getAnnotations()) {
				ann = metaAnn.annotationType().getAnnotation(annotationType);
				if (ann != null) {
					break;
				}
			}
		}
		return ann;
	}
}
