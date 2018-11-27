package com.annkee.base.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author wangan
 */
@WebListener
@Slf4j
public class ContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.warn("ServletContext销毁");
    }
    
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log.warn("ServletContext初始化");
    }
}