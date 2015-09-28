package com.kcp.platform.base.event;

import java.util.List;

/**
 *
 *类描述：
 *	事件处理接口，实现此并且getEventClasses返回结果条数大于0，方可处理对应的事件；
 * 
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
