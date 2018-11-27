package com.annkee.base.exercise.volatiletestthread;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class App {
    public static void main(String[] args) {

        MyThread[] myThread = new MyThread[100];

        for (int i = 0; i < 100; i++) {
            myThread[i] = new MyThread();
        }

        for (int i = 0; i < 100; i++) {
            myThread[i].start();
        }
    }
}
