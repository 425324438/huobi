package com.cn.huobi.email;


import com.cn.huobi.job.SchedledConfiguration;
import com.cn.huobi.util.DateUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private StringRedisTemplate template;


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

    /**
     * 获取redis 中的用户列表，根据用户需求发送邮件
     * @param currencyType 货币类型
     */
    public void sendMailByUser(String currencyType,String Subject,String text){
        List<String> lis = template.opsForList().range("userEmail",0,template.opsForList().size("userEmail"));
        if(lis != null && lis.size() > 0){
            for(int i=0;i< lis.size();i++){
                String str = lis.get(i);
                JSONObject strJsonData = JSONObject.fromObject(str);
                if(strJsonData.has("user")){
                    String toEmail = strJsonData.getString("user");
                    String currency = strJsonData.getString("currency");
                    String isSend = strJsonData.getString("email");
                    if(currency.indexOf(currencyType) != -1){
                        if("true".equals(isSend)){
                            String start = strJsonData.getString("start");
                            String end = strJsonData.getString("end");
                            //根据用户设置的 接受邮件的时间区间 发送邮件
                            boolean isSendM = DateUtil.isBelong(start,end, new Date());
                            if(isSendM){
                                SimpleMailMessage message = new SimpleMailMessage();
                                message.setFrom(Sender);
                                message.setTo(toEmail);
                                message.setSubject(Subject);
                                message.setText(text);
                                mailSender.send(message);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                log.info(toEmail + "：邮件发送成功"+dateFormat.format(new Date()));
                            }
                        }
                    }
                }
            }


        }
    }

}
