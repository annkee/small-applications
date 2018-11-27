package com.annkee.applications.controller;

import com.annkee.applications.domain.model.UserModel;
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
public class AsyncRequestController {
    
    @GetMapping("/getPrincipal")
    public Callable<UserModel> doAsync() {
        return () -> {
            Thread.sleep(5000);
            return (UserModel) SecurityUtils.getSubject().getPrincipal();
        };
    }
}
