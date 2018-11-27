package com.annkee.base.enums;

import lombok.Getter;


/**
 * 返回格式代码
 *
 * @author wangan
 * @date 2018/1/4
 */
@Getter
public enum ResultCodeEnum {
    
    SUCCESS(200, "成功"),
    SQLException(5001, "数据库异常"),
    PARAM_ERROR(504025, "参数错误"),
    AES_ENCRYPT_ERROR(504026, "AES加密错误"),
    AES_DECRYPT_ERROR(504027, "AES解密错误"),
    SYSTEM_ERROR(504028, "系统异常"),
    APP_PARSE_ERROR(504029, "app解析异常"),
    USER_LOGIN_ERROR(504030, "登陆异常"),
    
    BEIJING(54511, "北京"),
    ;
    private Integer code;
    private String message;
    
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
