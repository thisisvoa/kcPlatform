/**
 * @(#)com.casic27.platform.base.event.BaseEvent.java
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


/**
 *
 *类描述：发布的事件的基类
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class BaseEvent{
	private static final long serialVersionUID = -553658586080168454L;
	
	private Object source;
	
	public BaseEvent(){
		
	}
	
	public BaseEvent(Object source){
		this.source = source;
	}
	public Object getSource(){
		return this.source;
	}
	
}
