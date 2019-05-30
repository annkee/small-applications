package com.annkee.common.adapter;

import com.annkee.common.interceptor.WebInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * web前端路由适配器
 *
 * @author wangan
 */
@Configuration
public class WebMvcConfigAdapter extends WebMvcConfigurerAdapter {
    
    @Bean
    WebInterceptor webInterceptor() {
        return new WebInterceptor();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("html").addResourceLocations("classpath:/html");
        super.addResourceHandlers(registry);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        super.addViewControllers(registry);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
/**  registry.addInterceptor(webInterceptor()).addPathPatterns("/**").excludePathPatterns("toLogin", "swagger-ui.html","login");**/
        super.addInterceptors(registry);
    }
}
