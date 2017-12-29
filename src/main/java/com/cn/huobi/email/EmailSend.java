package com.cn.huobi.email;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 425324438@qq.com
 * @date 2017/12/29 13:09
 */
@Repository
public class EmailSend {

    @Resource
    public  JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    public  String Sender;

    /**
     * @param to 收件人
     * @param Subject 主题
     * @param text  邮件内容
     */
    public  void sendMail(String to,String Subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Sender);
        message.setTo(to);
        message.setSubject(Subject);
        message.setText(text);
        mailSender.send(message);
    }


}
