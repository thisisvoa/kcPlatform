/**
 * @(#)com.casic27.platform.base.event.EventDispatcher.java
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
package com.casic27.platform.base.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.casic27.platform.core.exception.InitializeException;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.sys.initialize.AbstractInitializer;
import com.casic27.platform.util.Orderable;

/**
 *
 *类描述：事件分发器
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
@Component("eventDispatcher")
public class EventDispatcher extends AbstractInitializer {
	private static Map<String, List<BaseEventListener>> listeners = new LinkedHashMap<String, List<BaseEventListener>>();
	
	private EventDispatcher(){
		
	}
	
	/**
	 * 扫描Spring上下文中的所有Bean，进行事件监听(业务类按时间类型归类),此方法在系统启动完后立即调用；
	 * 此方法大概过程如下：
	 * 1.从spring中找出所有实现了BaseEventLister的具体业务类实例
	 * 2.把这些实例归类装进Map变量listeners中，此Map变量的结构是：
	 * {
	 *  "XxxEvent1.class":[Listener1,Listener2,...],
	 *  "XxxEvent2.class":[Listener1,Listener3,...]
	 * }
	 */
	public void init()throws InitializeException{
		
		Map<String,BaseEventListener> beans = SpringContextHolder.getApplicationContext().getBeansOfType(BaseEventListener.class);
		if(beans==null||beans.size()==0){
			return;
		}
		
		Collection<BaseEventListener> services = beans.values();
		for(BaseEventListener listener : services){
			List<Class<? extends BaseEvent>> eventClasses = listener.getEventClasses();
			if(eventClasses==null || eventClasses.size()==0){
				continue;
			}
			for(int i=0;i<eventClasses.size();i++){
				String eventClsName = eventClasses.get(i).getName();
				List<BaseEventListener> list = listeners.get(eventClsName);
				if(list==null){
					list = new ArrayList<BaseEventListener>();
					listeners.put(eventClasses.get(i).getName(), list);
					list.add(listener);
				}
			}
		}
		
	}
	
	/**
	 * 发布事件
	 * @param event
	 */
	public static void publishEvent(BaseEvent event){
		List<BaseEventListener> list = listeners.get(event.getClass().getName());
		if(list!=null && list.size()>0){
			for(BaseEventListener listener : list){
				
				listener.onEvent(event);
			}
		}
	}
	
	/**
     * 获取初始化器的描述
     * 
     * @return
     */
    public String getDesc(){
    	return "事件框架";
    }
    
    /**
     * 设置启动顺序,将启动顺序提前
     */
    public int getOrderNo(){
    	return Orderable.LOWEST_PRECEDENCE+1;
    }
}
