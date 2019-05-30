package com.annkee.applications.controller;

import com.annkee.applications.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @author wangan
 */
@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncRequestController {
    
    @GetMapping("/getPrincipal")
    public Callable<UserModel> doAsync() {
        
        return () -> {
            Thread.sleep(1000);
            return (UserModel) SecurityUtils.getSubject().getPrincipal();
        };
    }
    
    
}
