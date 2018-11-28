package com.annkee.base.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangan
 * @date 2018/10/10
 */
@Data
@Component
@ConfigurationProperties(prefix = "*")
public class ConfigFileProperty {
    
    @Value("${spring.redis.host}")
    private String redisHost;
    
    @Value("${spring.redis.port}")
    private String port;
    
//    @Value("${spring.redis.password}")
//    private String redisPassword;
    
    public ConfigFileProperty() {
    }
}
