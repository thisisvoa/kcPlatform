package com.casic27.platform.web.listener;

import java.util.Date;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.casic27.platform.common.log.Logger;
import com.casic27.platform.common.user.entity.User;


/**
 * 当前在线用户数统计功能
 * User: 
 * Date: 
 * Time: 
 */
public class OnLineListener implements HttpSessionAttributeListener {
    private static final String SESSION_NAME = "loginUser";
    
    public OnLineListener() {
        System.out.println("启动在线用户数统计功能");
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent hsbe) {
        String name = hsbe.getName();
        if (SESSION_NAME.equals(name)) {
            Object value = hsbe.getValue();
            if (value instanceof User) {
            	String sessionId = hsbe.getSession().getId();
            	OnlineStore.getInstance().login(sessionId);
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent hsbe) {
        String name = hsbe.getName();
        if (SESSION_NAME.equals(name)) {
            Object value = hsbe.getValue();
            if (value instanceof User) {
            	String sessionId = hsbe.getSession().getId();
            	OnlineStore.getInstance().logout(sessionId);
            	Logger.getInstance().updateLogoutTime(sessionId, new Date());
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

}
