package com.annkee.common.webservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * 客户端调用
 *
 * @author wangan
 * @date 2018/11/28
 */
@Slf4j
public class CxfClient {
    
    public static void main(String[] args) {
        client1();
        client2();
    }
    
    /**
     * 1.代理类工厂的方式,需要拿到对方的接口地址
     */
    public static void client1() {
        try {
            // 接口地址
            String address = "http://127.0.0.1:8000/soap/user?wsdl";
            // 代理工厂
            JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
            // 设置代理地址
            jaxWsProxyFactoryBean.setAddress(address);
            // 设置接口类型
            jaxWsProxyFactoryBean.setServiceClass(UserService.class);
            // 创建一个代理接口实现
            UserService us = (UserService) jaxWsProxyFactoryBean.create();
            // 数据准备
            String userId = "maple";
            // 调用代理接口的方法调用并返回结果
            String result = us.getUserName(userId);
            log.warn("返回结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 2：动态调用
     */
    public static void client2() {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://127.0.0.1:8000/soap/user?wsdl");
        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getUserName", "maple");
            log.warn("返回数据:" + objects[0]);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
