package com.cn.huobi.job;

import com.cn.huobi.email.EmailSend;
import com.cn.huobi.https.HttpsClientUtil;
import com.cn.huobi.redis.service.RedisHashService;
import com.cn.huobi.redis.service.RedisListService;
import com.cn.huobi.redis.service.RedisStrService;
import com.cn.huobi.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 425324438@qq.com
 * @date 2018/1/5 13:29
 */
@Component
@Configuration
@EnableScheduling
public class Mins30 {
    private static final Logger log = LoggerFactory.getLogger(Mins30.class);

    @Value("${huobi.market}")
    private String market ;
    @Value("${huobi.trade}")
    private String trade ;
    private String charset = "utf-8";
    @Autowired
    private HttpsClientUtil httpsClientUtil;
    @Autowired
    private RedisStrService redisStrService;
    @Autowired
    private RedisHashService redisHashService;
    @Autowired
    private EmailSend emailSend;

    /**
     *  5分钟 执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 2 )
    public void job(){
        String currencySize = redisStrService.getKey("currencyList").toString();
        List<String> currencyList = Arrays.asList(currencySize.split(","));
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
                        mins30(dataJson,currency);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * @param dataJson 接口返回的最新价格
     * @param currency 币种
     */
    private void mins30(JSONObject dataJson,String currency){
        DecimalFormat df = new DecimalFormat("######0.000");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //收盘价：当前价格
        String close = dataJson.getString("close");
        //取出上次预留价格
        String upClose = (String)redisHashService.getHashObject("monitor_30"+currency,currency);
        String minTime = (String)redisHashService.getHashObject("monitor_30"+currency,"dataTime");
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
        Date dateTime1 = new Date();
        //相差的时间
        JSONObject dateTime = DateUtil.dateDiffer(dateFormat.format(dateTime1),minTime);
        Long min = dateTime.getLong("min");
        if(min != null){
            if(rose >= 5.0 || rose <= -5.0 ){
                String subject = currency+" ："+dateTime+"分钟内波动较大，"+"波动比例="+msg+"："+strRose+"%"+
                        " --当前价为 "+close+"，之前价为:"+dupClose;
                emailSend.sendMailByUser(currency,subject,subject);
                JSONObject huobi = new JSONObject();
                huobi.put(currency,close);
                huobi.put("dataTime",dateFormat.format(new Date()));
                redisHashService.setHash("monitor_30"+currency,huobi);
            }else{
                JSONObject huobi = new JSONObject();
                huobi.put(currency,close);
                huobi.put("dataTime",dateFormat.format(new Date()));
                redisHashService.setHash("monitor_30"+currency,huobi);
            }
            //1小时 定时发邮件
        }else if(min >= 60){
            String subject = "1小时推送："+currency+" ："+dateTime+"分钟内波动较大，"+"波动比例="+msg+"："+strRose+"%"+
                    " --当前价为 "+close+"，之前价为:"+dupClose;
            emailSend.sendMailByUser(currency,subject,subject);
            JSONObject huobi = new JSONObject();
            huobi.put(currency,close);
            huobi.put("dataTime",dateFormat.format(new Date()));
            redisHashService.setHash("monitor_30"+currency,huobi);
        }
    }
}
