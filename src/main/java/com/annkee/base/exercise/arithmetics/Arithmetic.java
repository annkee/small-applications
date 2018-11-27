package com.annkee.base.exercise.arithmetics;

/**
 * 算法
 *
 * @author wangan
 * @date 2018/8/8
 */
public class Arithmetic {

    /**
     * 二分查找算法，也叫折半查找，必须有顺序存储
     *
     * @param dataList
     * @param data
     * @param beginIndex
     * @param endIndex
     */
    public static int binarySearch(int[] dataList, int data, int beginIndex, int endIndex) {

        int midIndex = (beginIndex + endIndex) / 2;

        if (data < dataList[beginIndex] || data > dataList[endIndex] || beginIndex > endIndex) {
            return -1;
        }

        if (data < dataList[midIndex]) {
            return binarySearch(dataList, data, beginIndex, midIndex - 1);
        } else if (data > dataList[midIndex]) {
            return binarySearch(dataList, data, midIndex + 1, endIndex);
        } else {

            return midIndex;
        }


    }

    /**
     * 冒泡算法，比较相邻数据大小，进行交换处理
     *
     * @param arr
     * @param n
     */
    public static void bubble(int arr[], int n) {

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

    }
}
