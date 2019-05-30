package com.annkee.common.exercise.deadlock;

/**
 * 多线程死锁问题，互相持有对方的锁，不释放，导致互相等待
 *
 * @author wangan
 * @date 2018/7/19
 */
public class App {

    public static void main(String[] args) {
        DealThread thread1 = new DealThread();

        thread1.setFlag("a");
        Thread threadA = new Thread(thread1);
        threadA.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread threadB = new Thread(thread1);
        thread1.setFlag("b");
        threadB.start();
    }
}
