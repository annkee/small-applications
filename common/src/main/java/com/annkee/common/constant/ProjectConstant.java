package com.annkee.common.constant;

/**
 * 常量类
 *
 * @author wangan
 * @date 2018/3/21
 */
public class ProjectConstant {
    
    /**
     * 请求 fasthdfs 接口,SECRET_KEY和IDENTITY_ID都是需要设置的header
     */
    public static final String FINAL_URL = "http://localhost:8000/zuul/fdfs-micro/file/unlimited/uploads";
    public static final String SECRET_KEY = "my_fastdfs_key";
    public static final String IDENTITY_ID = "my_id";
    
    /**
     * json或者xml post请求
     */
    public static final int JSON = 1;
    public static final int XML = 2;
    
  
    /**
     * 超时时间
     */
    public static final int TIME_OUT = 5 * 1000;
    
    /**
     * 请求重试次数
     */
    public static final int RETRY_NUM = 3;
    
    /**
     * 最大连接数
     */
    public static final int MAX_CONNECTIONS = 200;
    
    /**
     * 每个路由最大连接数
     */
    public static final int MAX_ROUTE_CONNECTIONS = 100;
    
    /**
     * 请求头
     */
    public static final String AUTHORIZATION = "X-Token";
    
    /**
     * jwt salt
     */
    public static final String JWT_ENCRYPT_SALT = "DTrt$57^&*R.tyu123";
}
