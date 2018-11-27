package com.annkee.base.exercise.stringandsyn;

/**
 * @author wangan
 * @date 2018/7/19
 */
public class ThreadA extends Thread {

    private Service service;

    public ThreadA(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {

        service.print("AA");
    }
}
