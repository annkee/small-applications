package com.annkee.base.exercise.stringandsyn;

/**
 * @author wangan
 * @date 2018/7/17
 */
public class Service {

    public void print(String param) {

        synchronized (param) {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
