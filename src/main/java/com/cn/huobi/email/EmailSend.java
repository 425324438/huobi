package com.cn.huobi.email;


import com.cn.huobi.https.HttpsClientUtil;
import com.cn.huobi.job.SchedledConfiguration;
import com.cn.huobi.redis.service.RedisStrService;
import com.cn.huobi.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 425324438@qq.com
 * @date 2017/12/29 13:09
 */
@Repository
public class EmailSend {
    private static final Logger log = LoggerFactory.getLogger(EmailSend.class);
    @Resource
    public  JavaMailSender mailSender;    @Autowired
    private RedisStrService redisStrService;
    @Value("${spring.mail.username}")
    public  String Sender;
    @Autowired
    private StringRedisTemplate template;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Value("${huobi.market}")
    private String market ;
    @Value("${huobi.trade}")
    private String trade ;
    private String charset = "utf-8";
    @Autowired
    private HttpsClientUtil httpsClientUtil;


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

    /**
     * 为用户发送测试邮件
     */
    public String testSendEmailToUser(List<String> currencyList,String userEmail){
        try{
            for(String currency : currencyList){
                Map<String,String> createMap = new HashMap<String,String>(){
                    {
                        put("symbol",currency);
                        put("period","1min");
                        put("size","10");
                    }
                };
                String httpOrgCreateTestRtn = httpsClientUtil.doGet("https://api.huobi.pro/market/history/kline",createMap,charset);
                JSONObject json =  JSONObject.fromObject(httpOrgCreateTestRtn);
                if(json.has("status")){
                    String status = json.getString("status");
                    if(StringUtils.equals(status,"ok")){
                        JSONArray data = JSONArray.fromObject(json.get("data"));
                        for(int i=0;i < data.size();i++){
                            JSONObject dataJson = data.getJSONObject(i);
                            DecimalFormat df = new DecimalFormat("######0.000");
                            //收盘价：当前价格
                            String close = dataJson.getString("close");
                            //取出上次预留价格
                            Object obj = redisStrService.getKey(currency);
                            JSONObject redis =  JSONObject.fromObject(obj);
                            String upClose = redis.getString(currency);
                            log.info("当前价格："+currency+" = "+close+"，之前价格"+upClose);
                            //涨幅 = （（现在价格  - 之前价格） / 现在价格） * 100
                            Double dClose =  Double.parseDouble(close);//当前价格
                            Double dupClose =  Double.parseDouble(upClose);//之前价格
                            Double rose  = ( dClose - dupClose) / dClose * 100;
                            String strRose = df.format(rose);
                            String msg = "";
                            rose = Double.parseDouble(strRose);
                            if(dupClose < dClose){
                                msg = "上涨";
                            }else{
                                msg = "下跌";
                            }
                            String subject = "测试邮件："+currency+" ：5分钟内波动较大，"+"波动比例 = "+msg+"："+strRose+"%"+
                                    " -- 当前价格为 "+close+"，之前价格为:"+dupClose;
                            sendMail(userEmail,subject,subject);
                            break;
                        }
                    }
                }
            }
            return "success";
        }catch (Exception e){
            return "error";
        }
    }
}
