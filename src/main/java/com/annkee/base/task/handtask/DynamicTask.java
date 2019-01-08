package com.annkee.base.task.handtask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

/**
 * @author wangan
 * @date 2018/12/26
 */
@Component
@Slf4j
public class DynamicTask {
    
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    
    private ThreadLocal<ScheduledFuture<?>> threadLocalFuture;
    
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
    
    public String startCron() {
        
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("0/5 * * * * *"));
        threadLocalFuture.set(future);
        
        log.warn("DynamicTask.startCron()");
        return "startCron";
    }
    
    public String stopCron() {
        
        if (threadLocalFuture.get() != null) {
            threadLocalFuture.get().cancel(true);
        }
        log.warn("DynamicTask.stopCron()");
        return "stopCron";
    }
    
    public String startCron10() {
        
        stopCron();// 先停止，在开启.
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("*/10 * * * * *"));
        log.warn("DynamicTask.startCron10()");
        return "changeCron10";
    }
    
    private class MyRunnable implements Runnable {
        
        @Override
        public void run() {
            try {
                String name = threadLocalFuture.get().get().getClass().getName();
                log.warn("name:{}-DynamicTask.MyRunnable.run()，date:{}", name, new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            
        }
    }
}
