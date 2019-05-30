package com.annkee.common.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author wangan
 */
@WebListener
@Slf4j
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.warn("Session 被创建");
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.warn("ServletContext初始化");
    }
}