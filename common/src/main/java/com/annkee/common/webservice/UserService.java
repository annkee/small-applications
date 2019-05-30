package com.annkee.common.webservice;


import com.annkee.common.model.UserModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author wangan
 * @date 2018/11/28
 */
@WebService(targetNamespace = "http://service.demo.example.com")
public interface UserService {
    /**
     * 标注该方法为webservice暴露的方法,用于向外公布，它修饰的方法是webservice方法，去掉也没影响的，类似一个注释信息。
     *
     * @param userId
     * @return
     */
    @WebMethod
    UserModel getUser(@WebParam(name = "userId") String userId);
    
    /**
     * 获取用户名
     *
     * @param userId
     * @return
     */
    @WebMethod
    @WebResult(name = "String", targetNamespace = "")
    String getUserName(@WebParam(name = "userId") String userId);
}
