package com.annkee.base.exercise.threadpool;

import java.util.concurrent.*;

/**
 * @Author: wangan
 * @Date: 18-8-5
 */
public class ThreadPoolTest {

    /**
     * 核心线程池数量，表示能够同时执行的任务数量
     */
    private int corePoolSize;

    /**
     * 最大线程池数量，其实是包含了核心线程池数量在内的
     */
    private int maximumPoolSize;

    /**
     * 存活时间,表示最大线程池中等待任务的存活时间
     */
    private long keepAliveTime = 1;

    private TimeUnit unit = TimeUnit.HOURS;

    /**
     * 1.使用Executors创建线程池
     *
     * @throws Exception
     */
    public void testPool() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<String> result = executorService.submit(() -> "hello world");


        System.out.println(result.get());
    }


    /**
     * 2.ThreadPoolExecutor创建线程池
     *
     * @throws Exception
     */
    public void testPool2() throws Exception {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

    }


}
