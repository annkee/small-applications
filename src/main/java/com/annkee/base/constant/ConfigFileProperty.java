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
    
    @Value("${aliyun.access_key_id}")
    private String accessKeyId;
    
    @Value("${aliyun.access_key_secret}")
    private String accessKeySecret;
    
    @Value("${aliyun.salt}")
    private String salt;
    
    public ConfigFileProperty() {
    }
}
