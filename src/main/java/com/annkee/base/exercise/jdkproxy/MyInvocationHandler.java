package com.annkee.base.exercise.jdkproxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: wangan
 * @Date: 18-8-5
 */
public class MyInvocationHandler implements InvocationHandler {

    /**
     * target object
     */
    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("proxy run invoke()");
        Object proxyObject = method.invoke(target, args);
        System.out.println("proxy object is: " + proxyObject);
        return proxyObject;
    }
}
