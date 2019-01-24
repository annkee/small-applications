package com.annkee.base.task.dynatask;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wangan
 * @date 2019/1/24
 */
@Slf4j
@Component
public class ToDoJob implements Job {
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.warn("时间：{}，线程：{}", new Date(), Thread.currentThread().getName());
    }
    
}
