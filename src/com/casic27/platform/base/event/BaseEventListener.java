/**
 * @(#)com.casic27.platform.base.event.EventListener.java
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

import java.util.List;

/**
 *
 *类描述：
 *	事件处理接口，实现此并且getEventClasses返回结果条数大于0，方可处理对应的事件；
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public interface BaseEventListener {
	/**
	 * 事件处理的方法
	 * @param event
	 */
	public void onEvent(BaseEvent event);
	
	/**
	 * 所要处理的事件类型
	 * @return
	 */
	public List<Class<? extends BaseEvent>> getEventClasses();
}
