/**
 * 2011-1-9 下午05:02:14
 */
package com.cntinker.util;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author bin_liu
 */
public class MailSend{

    private Log log = LogFactory.getLog("msg");

    /**
     * 发送简单文字邮件
     * 
     * @param host
     * @param user
     * @param password
     * @param formname
     * @param fromaddress
     * @param to
     * @param subject
     * @param content
     */
    public void sendSampleMail(String localhost,String host,String user,String password,
            String formname,String fromaddress,String[] to,String subject,
            String content){
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server
        senderImpl.setHost(host);

        // 建立邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo(to);
        // mailMessage.setFrom("刘斌<liubin0821@126.com>");
        mailMessage.setFrom(formatAddress(formname,fromaddress));
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        senderImpl.setUsername(user); // 根据自己的情况,设置username
        senderImpl.setPassword(password); // 根据自己的情况, 设置password

        //
        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout","25000");
        prop.put("mail.smtp.localhost", localhost);
        prop.put("mail.smtp.host", host);

        Session s = senderImpl.getSession();
        if(s == null)
            s = Session.getInstance(prop);
        s.setDebug(true);
        senderImpl.setSession(s);
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);

    }

    /**
     * 发送HTML的邮件
     * 
     * @param host
     * @param user
     * @param password
     * @param formname
     * @param fromaddress
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String host,String user,String password,
            String formname,String fromaddress,String[] to,String subject,
            String content) throws MessagingException{
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        // 设定mail server
        senderImpl.setHost(host);

        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                "GBK");

        // 设置收件人，寄件人
        messageHelper.setTo(to);
        messageHelper.setFrom(formatAddress(formname,fromaddress));
        messageHelper.setSubject(subject);
        // true 表示启动HTML格式的邮件
        messageHelper.setText(content,true);

        senderImpl.setUsername(user);
        senderImpl.setPassword(password);
        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout","25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);
    }

    /**
     * 发件人转换函数
     * 
     * @param name
     * @param email
     * @return String
     */
    public static String formatAddress(String name,String email){
        try{
            return String.format("%1$s <%2$s>",
                    MimeUtility.encodeText(name,"UTF-8","B"),email);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return email;
    }

    // public static void main(String args[])throws Exception{
    // String[] to = new String[]{"376068867@qq.com"}; //"284523574@qq.com"
    //
    // String link = "http://211.94.187.165:8090/taobao/ad_1.jsp?m=";
    //
    // MailSend ms = new MailSend();
    // // ms.sendSampleMail("smtp.126.com","liubin0821","gamebb","管理员",
    // // "liubin0821@126.com","binliu0821@gmail.com","测试标题","测试内容");
    //
    // String html
    // ="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body>如果邮件无法正常显示，<a href="+link+StringHelper.toHexString("376068867@qq.com")+">请点击这里显示<br/><a href="+link+StringHelper.toHexString("376068867@qq.com")+"><img src=http://cartoon.dm3721.com/ad_1.jpg></img></a></body></html>";
    // ms.sendHtmlMail("smtp.126.com","liubin0821","gamebb","时尚包包",
    // "liubin0821@126.com",to,"2011最新时尚包包",html);
    //
    // System.out.println(" 邮件发送成功.. ");
    // }
}
