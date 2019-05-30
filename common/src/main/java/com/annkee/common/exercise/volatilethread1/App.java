package com.annkee.common.exercise.volatilethread1;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class App {

    public static void main(String[] args) {
        PrintString printString = new PrintString();
        new Thread(printString).start();

        System.out.println("shutdown thread name=" +
                Thread.currentThread().getName());


        printString.setContinuePrint(false);
    }
}
