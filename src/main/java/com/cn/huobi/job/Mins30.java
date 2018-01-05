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

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
    private RedisListService redisListService;
    @Autowired
    private EmailSend emailSend;

    /**
     *  30分钟 执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 30 )
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

    private void mins30(JSONObject dataJson,String currency){
        DecimalFormat df = new DecimalFormat("######0.000");
        //收盘价：当前价格
        String close = dataJson.getString("close");
        //取出上次预留价格
        Object obj = redisStrService.getKey(currency);
        if(obj == null){
            JSONObject redisJson = new JSONObject();
            redisJson.put(currency,close);
            redisJson.put("dataTime",dateFormat.format(new Date()));
            redisStrService.setKey(currency,String.valueOf(redisJson));
            obj = redisJson;
        }
        JSONObject redis =  JSONObject.fromObject(obj);
        JSONObject dateJson = DateUtil.dateDiffer
                (dateFormat.format(new Date()),redis.getString("dataTime"));
        Long min = dateJson.getLong("min");
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
        log.info("当前趋势："+currency+" = "+msg+"："+rose+"%");

        if(dateJson!= null && dateJson.has("min")){
            if(rose >= 5.0 || rose <= -5.0 ){
                String subject = currency+" ：30分钟内波动较大，"+"波动比例 = "+msg+"："+strRose+"%"+
                        " -- 当前价为 "+close+"，之前价为:"+dupClose;
                emailSend.sendMailByUser(currency,subject,subject);
                /**
                 *  30分钟的Obj监控
                 *  {
                 "dataTime": "2017-12-29 04:35:32",
                 "xrpusdt": "1.1277"
                 }
                 */
                JSONObject huobi = new JSONObject();
                huobi.put(currency,close);
                huobi.put("dataTime",dateFormat.format(new Date()));
                redisHashService.setHash("monitor_30"+currency,huobi);
                //5分钟更新一次（或者价格波动较大） redis 数据, 每次比较与5分钟之前的 价格比较
                JSONObject redisJson = new JSONObject();
                redisJson.put(currency,close);
                redisJson.put("dataTime",dateFormat.format(new Date()));
                redisStrService.setKey(currency,String.valueOf(redisJson));
            }
            if( min >= 30 ){
                //5分钟更新一次（或者价格波动较大） redis 数据, 每次比较与5分钟之前的 价格比较
                JSONObject redisJson = new JSONObject();
                redisJson.put(currency,close);
                redisJson.put("dataTime",dateFormat.format(new Date()));
                redisStrService.setKey(currency,String.valueOf(redisJson));
            }
        }
    }
}
