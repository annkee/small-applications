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
    
    SUCCESS(9999, "成功"),
    
    SQLException(50001, "数据库异常"),
    
    PARAM_ERROR(50025, "参数错误"),
    
    AES_ENCRYPT_ERROR(50026, "AES加密错误"),
    
    AES_DECRYPT_ERROR(50027, "AES解密错误"),
    
    SYSTEM_ERROR(50028, "系统异常"),
    
    APP_PARSE_ERROR(50029, "app解析异常"),
    USER_LOGIN_ERROR(50030, "登陆异常"),
    UPLOAD_FILE_ERROR(50031, "上传文件失败"),
    
    BEIJING(54511, "北京"),
    ;
    private Integer code;
    private String message;
    
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
