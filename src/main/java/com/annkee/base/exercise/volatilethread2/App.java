package com.annkee.base.exercise.volatilethread2;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class App {
    public static void main(String[] args) {
        RunThread runThread = new RunThread();
        runThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runThread.setRunning(false);
        System.out.println("have set false");
    }
}
