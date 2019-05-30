package com.annkee.common.exercise.list;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 测试ArrayLst 删除元素的时候导致数组下标位移，导致无法删除与上一个相同的数据
 *
 * @author wangan
 * @date 2018/8/8
 */
public class ArrayListTest {

    /**
     * 问题
     */
    public static void testRemove() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("b");
        arrayList.add("c");

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).endsWith("a")) {
                arrayList.remove("a");
            }
        }

        System.out.println(arrayList);
    }

    /**
     * 解决，使用iterator
     */
    public static void iterator() {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("b");
        arrayList.add("c");

        Iterator<String> iterator = arrayList.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals("a")) {
                iterator.remove();
            }
        }

        System.out.println(arrayList);
    }
}
