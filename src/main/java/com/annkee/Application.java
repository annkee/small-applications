package com.annkee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author wangan
 * @date 2018/01/04
 */
@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer {
    
    /**
     * jar启动
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        
        SpringApplication.run(Application.class, args);
    }
    
    /**
     * tomcat war启动
     *
     * @param application 应用构建
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        
        return application.sources(Application.class);
    }
}
