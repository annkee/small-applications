package com.annkee.base.enums;

/**
 * @author wangan
 * @date 2018/1/9
 */
public enum MailEnum {
    
    MAILUSERNAME("mailUsername", "test@qq.com"),
    MAILPASSWORD("mailPassword", "gdzkzvntydjcd"),
    MAILTIMEOUT("mailTimeout", "10000"),
    MAILHOST("mailHost", "smtp.qq.com"),
    MAILFROM("mailFrom", "test@qq.com"),
    PERSONAL("personal", "annkee"),
    MAILPORT("mailPort", "465"),;

    private String key;
    private String value;

    MailEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
