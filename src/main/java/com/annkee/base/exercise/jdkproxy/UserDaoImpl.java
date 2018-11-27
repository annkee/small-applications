package com.annkee.base.exercise.jdkproxy;


/**
 * @Author: wangan
 * @Date: 18-8-5
 */
public class UserDaoImpl implements UserDao {

    @Override
    public int add() {
        System.out.println("add()");
        return 0;
    }

    @Override
    public int select() {
        System.out.println("select()");
        return 0;
    }
}
