package com.annkee.base.exercise.jdkproxy;

import java.lang.reflect.Proxy;

/**
 * @Author: wangan
 * @Date: 18-8-5
 */
public class App {

    public static void main(String[] args) {

        UserDaoImpl userDao = new UserDaoImpl();
        userDao.add();
        userDao.select();

        //下面使用代理

        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(userDao);
        UserDao userDaoProxy = (UserDao) Proxy.newProxyInstance(userDao.getClass().getClassLoader(),
                userDao.getClass().getInterfaces(), myInvocationHandler);

        //动态代理得到权限进行操作
        userDaoProxy.add();
        userDaoProxy.select();
    }
}
