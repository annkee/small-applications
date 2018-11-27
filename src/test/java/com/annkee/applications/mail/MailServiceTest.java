package com.annkee.applications.mail;

import org.junit.Test;

/**
 * @author wangan
 * @date 2018/1/10
 */
public class MailServiceTest {

    @Test
    public void sendHtmlMail() throws Exception {

        MailService.sendHtmlMail("2210212867@qq.com", "春节放假通知", "<p>关于春节放假的通知如下：</p>");
    }


}