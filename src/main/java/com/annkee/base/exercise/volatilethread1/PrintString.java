package com.annkee.base.exercise.volatilethread1;

/**
 * @author wangan
 * @date 2018/7/27
 */
public class PrintString implements Runnable {

    private boolean isContinuePrint = true;

    public boolean isContinuePrint() {
        return isContinuePrint;
    }

    public void setContinuePrint(boolean continuePrint) {
        isContinuePrint = continuePrint;
    }

    public void printStringMethod() {
        while (isContinuePrint == true) {
            System.out.println("run printStringMethod thread name= " +
                    Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        printStringMethod();
    }
}
