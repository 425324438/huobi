package com.cn.huobi.email;


import com.cn.huobi.job.SchedledConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 425324438@qq.com
 * @date 2017/12/29 13:09
 */
@Repository
public class EmailSend {
    private static final Logger log = LoggerFactory.getLogger(EmailSend.class);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        log.info("邮件发送成功"+dateFormat.format(new Date()));
    }


}
