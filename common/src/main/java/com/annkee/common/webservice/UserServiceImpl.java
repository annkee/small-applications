package com.annkee.common.webservice;

import com.annkee.common.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 项目启动执行此方法
 *
 * @author wangan
 * @date 2018/11/28
 */
@WebService(serviceName = "UserService", targetNamespace = "http://service.demo.example.com", endpointInterface = "com.annkee.common.webservice.UserService")
@Component
@Slf4j
public class UserServiceImpl implements UserService {
    
    private Map<String, UserModel> userMap = new HashMap<>();
    
    public UserServiceImpl() {
        log.warn("发布实体对象内容，向实体类插入数据");
        UserModel user = new UserModel();
        user.setSalt(UUID.randomUUID().toString().replace("-", ""));
        user.setUsername("test1");
        userMap.put(user.getSalt(), user);
        
        user = new UserModel();
        user.setSalt(UUID.randomUUID().toString().replace("-", ""));
        user.setUsername("test2");
        userMap.put(user.getSalt(), user);
        
    }
    
    @Override
    public String getUserName(String userId) {
        return "userId为：" + userId;
    }
    
    @Override
    public UserModel getUser(String userId) {
        log.warn("userMap是:{}", userMap);
        return userMap.get(userId);
    }
    
}
