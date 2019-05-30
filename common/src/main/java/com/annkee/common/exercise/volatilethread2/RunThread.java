package com.annkee.common.exercise.volatilethread2;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class RunThread extends Thread {

    volatile private boolean isRunning = true;

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        System.out.println("run()...");
        while (isRunning == true) {

        }
        System.out.println("shutdown thread");
    }
}
