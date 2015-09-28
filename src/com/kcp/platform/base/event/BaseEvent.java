package com.kcp.platform.base.event;


/**
 *
 *类描述：发布的事件的基类
 * 
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
