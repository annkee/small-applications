package com.annkee.base.exercise.volatilethread;

/**
 * main线程一直处于while死循环，设置的false不管用，到不了
 *
 * @author wangan
 * @date 2018/7/27
 */
public class App {
    public static void main(String[] args) {
        PrintString printString = new PrintString();
        printString.printStringMethod();
        System.out.println("shutdown stopThread=" + Thread.currentThread().getName());
        printString.setContinuePrint(false);
    }
}
