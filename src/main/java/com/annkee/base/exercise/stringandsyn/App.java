package com.annkee.base.exercise.stringandsyn;

/**
 * string常量的监视器对象
 * 线程A和B使用同样的字符串常量锁对象，造成B线程不能执行
 *
 * @author wangan
 * @date 2018/7/19
 */
public class App {

    public static void main(String[] args) {
        Service service = new Service();
        ThreadA threadA = new ThreadA(service);
        threadA.setName("A");

        ThreadB threadB = new ThreadB(service);
        threadB.setName("B");

        threadA.start();
        threadB.start();

    }
}
