package com.annkee.common.exercise.arithmetics;

/**
 * @author wangan
 * @date 2018/8/8
 */
public class App {

    public static void main(String[] args) {

        //二分查找，必须有顺序
        int[] arr = {6, 12, 33, 87, 90, 97, 108, 561};
        int i = Arithmetic.binarySearch(arr, 87, 0, arr.length - 1);
        System.out.println(i);

        //冒泡
        Arithmetic.bubble(arr, 8);
    }
}
