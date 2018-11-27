package com.annkee.base.exercise.threadpool;

/**
 * @Author: wangan
 * @Date: 18-8-5
 */
public class App {

    public static void main(String[] args) {

        ThreadPoolTest threadPoolTest = new ThreadPoolTest();

        try {
            threadPoolTest.testPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
