package com.annkee.base.task.dynatask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wangan
 * @date 2019/1/24
 */
@Component
@Slf4j
public class ToDoJobManager implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    public void init() {
        log.warn("---读取所有设置了时间的机构推送设置!");
        QuartzManager quartzManager = (QuartzManager) applicationContext.getBean("quartzManager");
        ToDoQuartz job = new ToDoQuartz();
        job.setName("testA");
        job.setExecuteDate(new Date());
        
        String jobName = job.getName();
        quartzManager.removeJob(jobName, jobName, jobName, jobName);
        quartzManager.addJob(jobName, jobName, jobName, jobName, ToDoJob.class, "0 */1 * * * ?", job);
        
    }
    
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        init();
    }
    
}