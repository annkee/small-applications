package com.annkee.common.exercise.deadlock;

/**
 * @author wangan
 * @date 2018/7/25
 */
public class DealThread implements Runnable {

    public String username;

    public Object lock1 = new Object();
    public Object lock2 = new Object();

    public void setFlag(String username) {
        this.username = username;
    }

    @Override
    public void run() {

        if (username.equals("a")) {
            //同步代码块不结束不会释放lock1锁
            synchronized (lock1) {
                System.out.println(username);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock2) {
                    System.out.println("lock1-->lock2");
                }
            }
        }

        if (username.equals("b")) {
            //同步代码块不结束，不会释放lock2锁
            synchronized (lock2) {
                System.out.println(username);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock1) {
                    System.out.println("lock2-->lock1");
                }
            }
        }
    }
}
