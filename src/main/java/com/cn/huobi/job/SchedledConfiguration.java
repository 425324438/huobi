package com.cn.huobi.job;

import com.cn.huobi.email.EmailSend;
import com.cn.huobi.https.HttpsClientUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  liaoliping
 * date：2017/12/27
 * time：22:34
 *
 * 我给你个阈值，5分钟2%，半小时10%   一天30%
 */
@Component
@Configuration
@EnableScheduling // 启用定时任务
public class SchedledConfiguration  {
    private static final Logger log = LoggerFactory.getLogger(SchedledConfiguration.class);

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
    private RedisListService redisListService;
    @Autowired
    private EmailSend emailSend;

    /**
          "data": [
             {
                 "id": K线id,
                 "amount": 成交量,
                 "count": 成交笔数,
                 "open": 开盘价,
                 "close": 收盘价,当K线为最晚的一根时，是最新成交价
                 "low": 最低价,
                 "high": 最高价,
                 "vol": 成交额, 即 sum(每一笔成交价 * 该笔的成交量)
             }
          ]
     */
//    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Scheduled(fixedRate = 1000 * 5 )
    public void job(){
        Map<String,String> createMap = new HashMap<String,String>(){
            {
                put("symbol","xrpusdt");
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
                    mins5(dataJson);
                    break;
                }
            }
        }
    }

    /**
     *  我给你个阈值，5分钟2%，半小时10%   一天30%
     *
     *  5分钟 --》 放入某个redis ，
     *      30分钟的循环每次从这里取，如果监控到涨幅超过 10%， 放入一天  （redis key）--》
     *          1天一次的定时器对其监控
     */
    private void mins5(JSONObject dataJson){
        DecimalFormat df = new DecimalFormat("######0.00");
        //收盘价：当前价格
        String close = dataJson.getString("close");
        log.info("当前价格：xrpusdt = "+close);
        //取出上次预留价格
        JSONObject redis =  JSONObject.fromObject(redisStrService.getKey("xrpusdt"));
        JSONObject dateJson = DateUtil.dateDiffer
                (dateFormat.format(new Date()),redis.getString("dataTime"));
        if(dateJson!= null && dateJson.has("min")){
            Long min = dateJson.getLong("min");
            if(min >= 5){
                String upClose = redis.getString("xrpusdt");
                //涨幅 = （之前价格 -  当前价格 ） /  当前价格 ，
                Double dClose =  Double.parseDouble(close);
                Double dupClose =  Double.parseDouble(upClose);
                Double rose  = ( dClose - dupClose) / dClose;
                String strRose = df.format(rose);
                String msg = "";
                    if(dupClose < dClose){
                        msg = "上涨";
                    }else{
                        msg = "下跌";
                    }
                if(rose > 2 || rose < -2){
                    String subject = "xrpusdt ：5分钟内波动较大，"+"波动比例 = "+msg+"："+strRose+"%"+
                            " -- 当前价格为 "+close+"，之前价格为:"+dupClose;
                    emailSend.sendMail("2037520355@qq.com",subject,subject);
                    /**
                     *  30分钟的Obj监控
                     *  [
                            {
                             "dataTime": "2017-12-29 04:35:32",
                             "xrpusdt": "1.1277"
                             }
                        ]
                     */
                    JSONObject huobi = new JSONObject();
                        huobi.put("xrpusdt",close);
                        huobi.put("dataTime",dateFormat.format(new Date()));
                    redisListService.listSet("monitor_30",String.valueOf(huobi));
                }
                JSONObject redisJson = new JSONObject();
                redisJson.put("xrpusdt",close);
                redisJson.put("dataTime",dateFormat.format(new Date()));
                redisStrService.setKey("xrpusdt",String.valueOf(redisJson));
            }
        }
    }



    /**
     * 根据cron表达式格式触发定时任务
     *  cron表达式格式:
     *      1.Seconds Minutes Hours DayofMonth Month DayofWeek Year
     *      2.Seconds Minutes Hours DayofMonth Month DayofWeek
     *  顺序:
     *      秒（0~59）
     *      分钟（0~59）
     *      小时（0~23）
     *      天（月）（0~31，但是你需要考虑你月的天数）
     *      月（0~11）
     *      天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     *      年份（1970－2099）
     *
     *  注:其中每个元素可以是一个值(如6),一个连续区间(9-12),一个间隔时间(8-18/4)(/表示每隔4小时),一个列表(1,3,5),通配符。
     *  由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置?.
     */
//    @Scheduled(cron="5 * * * * ?")
//    public void cronScheduled() {
//        log.info("定时任务执行了 : The time is now {}", dateFormat.format(new Date()));
//    }
}
