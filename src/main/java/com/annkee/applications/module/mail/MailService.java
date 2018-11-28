package com.annkee.applications.module.mail;

import com.annkee.base.enums.MailEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 邮件发送
 *
 * @author wangan
 * @date 2018/1/10
 **/
@Slf4j
public class MailService {
    
    private static final String HOST = MailEnum.MAILHOST.getValue();
    private static final Integer PORT = Integer.valueOf(MailEnum.MAILPORT.getValue());
    private static final String USERNAME = MailEnum.MAILUSERNAME.getValue();
    private static final String PASSWORD = MailEnum.MAILPASSWORD.getValue();
    private static final String EMAIL_FORM = "test@qq.com";
    private static JavaMailSenderImpl mailSender = createMailSender();
    
    /**
     * 设置邮件属性
     *
     * @return JavaMailSenderImpl
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "50000");
        p.setProperty("mail.smtp.auth", "true");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(p);
        log.info("---create sender---");
        return sender;
    }
    
    /**
     * 发送邮件
     *
     * @param to      接受人
     * @param subject 主题
     * @param html    发送内容
     * @throws MessagingException           异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendHtmlMail(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, "test@qq.com");
        //抄送给地址
        messageHelper.setCc(new InternetAddress("test@163.com"));
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        log.info("---now send mail---");
        mailSender.send(mimeMessage);
    }
    
    /**
     * 发送邮件
     *
     * @param email
     * @param emailMsg
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sendMail(String email, String emailMsg) throws MessagingException {
        
        // 创建一个程序与邮件服务器会话对象 Session
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.smtp.host", "smtp.163.com");
        props.setProperty("mail.smtp.port", "25");
        // 指定验证为true
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.timeout", "100000");
        // 验证账号及密码，密码需要是第三方授权码
        
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("test@163.com", "test");
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        
        // 创建一个Message，它相当于是邮件内容
        Message message = new MimeMessage(session);
        // 设置发送者
        message.setFrom(new InternetAddress("test@163.com"));
        // 设置发送方式与接收者
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
        // 设置主题
        message.setSubject("天气预报");
        // 设置内容
        message.setText(emailMsg);
        message.saveChanges();
        // 3.创建 Transport用于将邮件发送
        Transport.send(message);
    }
    
}
