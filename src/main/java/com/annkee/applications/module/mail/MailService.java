package com.annkee.applications.module.mail;

import com.annkee.base.enums.MailEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
    
    /**
     * 发送邮件
     *
     * @param emailTo  收件人，用分号分隔的多个收件人
     * @param emailMsg 邮件内容
     * @param files    文件路径
     * @param title    主题
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sendMail(String emailTo, String emailMsg, List<String> files, String title) throws MessagingException {
        
        log.info("收件人为【{}】", emailTo);
        Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.port", PORT);
        
        // 发件人的账号
        props.put("mail.user", USERNAME);
        // 访问SMTP服务时需要提供的密码(邮箱密码)
        props.put("mail.password", PASSWORD);
        
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };
        
        Session session = Session.getInstance(props, authenticator);
        
        // 创建一个Message，它相当于是邮件内容
        Message message = new MimeMessage(session);
        try {
            message.setSubject(title);
            /**
             * 2. 设置发件人
             * 其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
             */
            message.setFrom(new InternetAddress(USERNAME, USERNAME, "UTF-8"));
            
            /**
             * 3，设置收件人
             * To收件人   CC 抄送  BCC密送
             */
            InternetAddress[] address = null;
            ArrayList<InternetAddress> objects = new ArrayList<InternetAddress>();
            
            for (int i = 0; i < emailTo.split(";").length; i++) {
                objects.add(new InternetAddress(emailTo.split(";")[i], "", "UTF-8"));
            }
            address = objects.toArray(new InternetAddress[objects.size()]);
            
            message.setRecipients(MimeMessage.RecipientType.TO, address);
            
            //   设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(emailMsg, "text/html;charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(contentPart);
            //添加附件
            if (files != null) {
                for (String attachment : files) {
                    BodyPart attachmentPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    attachmentPart.setDataHandler(new DataHandler(source));
                    //避免中文乱码的处理
                    String substring = attachment.substring(attachment.lastIndexOf("/") + 1);
                    String s = MimeUtility.encodeText(substring);
                    attachmentPart.setFileName(s);
                    multipart.addBodyPart(attachmentPart);
                }
            }
            //将multipart对象放到message中
            message.setContent(multipart, "text/html;charset=utf-8");
            Transport.send(message);
            log.warn("====to 【{}】邮件发送成功====", emailTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
