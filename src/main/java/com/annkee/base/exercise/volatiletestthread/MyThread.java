package com.annkee.base.exercise.volatiletestthread;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class MyThread extends Thread {

    volatile public static int count;

   synchronized private static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println("thread name =" + Thread.currentThread().getName()
                + "  count=" + count);
    }

    @Override
    public void run() {
        addCount();
    }
}
