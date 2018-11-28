package com.annkee.applications.service;

import com.annkee.applications.domain.model.UserModel;
import com.annkee.base.auth.JwtUtil;
import com.annkee.base.constant.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息
 *
 * @author wangan
 */
@Service
@Slf4j
public class UserService {
    
    @Resource
    private RedisTemplate redisTemplate;
    
    /**
     * 解决redis key 出现乱码 \xac\xed\x00\x05t\x00\x04username
     *
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 保存user登录信息，返回token
     *
     * @param username
     */
    public String generateJwtToken(String username) {
        Object o = redisTemplate.opsForValue().get("token:" + username);
        String value = String.valueOf(o);
        if (o == null) {
            
            String salt = JwtUtil.generateSalt();
            log.warn("salt:{}", salt);
            // 将salt保存到数据库或者缓存中
            redisTemplate.opsForValue().set("token:" + username, salt, 3600, TimeUnit.SECONDS);
            // 生成jwt token，设置过期时间为1小时
            String sign = JwtUtil.sign(username, salt, 3600);
            return sign;
        }
        return value;
    }
    
    /**
     * 获取上次token生成时的salt值和登录用户信息
     *
     * @param username
     * @return
     */
    public UserModel getJwtTokenInfo(String username) {
        // 从数据库或者缓存中取出jwt token生成时用的salt
        Object salt = redisTemplate.opsForValue().get("token:" + username);
        UserModel user = getUserInfo(username);
        user.setSalt(salt.toString());
        return user;
    }
    
    /**
     * 清除token信息
     *
     * @param username 登录用户名
     */
    public void deleteLoginInfo(String username) {
        // 删除数据库或者缓存中保存的salt
        redisTemplate.delete("token:" + username);
        
    }
    
    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     *
     * @param userName
     * @return
     */
    public UserModel getUserInfo(String userName) {
        UserModel user = new UserModel();
        user.setUserId(1L);
        user.setUsername("admin");
        user.setEncryptPwd(new Sha256Hash("123456", ProjectConstant.JWT_ENCRYPT_SALT).toHex());
        return user;
    }
    
    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     *
     * @param userId
     * @return
     */
    public List<String> getUserRoles(Long userId) {
        return Arrays.asList("admin");
    }
    
}
