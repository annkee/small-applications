package com.annkee.applications.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author wangan
 * @date 2018/11/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void generateJwtToken() {
        String token = userService.generateJwtToken("admin");
        log.warn(token);
    }
}