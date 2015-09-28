package com.kcp.platform.common.log.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.kcp.platform.common.log.interceptor.MethodSysLogDefinition;


/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
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
