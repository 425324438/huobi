package com.cn.huobi.util;

import com.cn.huobi.job.SchedledConfiguration;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 开发者 liaoliping
 * date：2017/12/28
 * time：23:47
 */
public  class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    /**
     * @param DateStr1  DateStr1 = "2017-12-28 11:32:35";
     * @param DateStr2  DateStr2 = "2017-12-28 11:32:35";
     * @return
     * @throws ParseException
     */
    public static  JSONObject dateDiffer(String DateStr1,String DateStr2)   {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date dateTime1 = dateFormat.parse(DateStr1);
            Date dateTime2 = dateFormat.parse(DateStr2);
            long between = dateTime1.getTime() - dateTime2.getTime();
            long day = between / (24 * 60 * 60 * 1000);
            long hour = (between / (60 * 60 * 1000) - day * 24);
            long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
                    + "毫秒");
            JSONObject json = new JSONObject();
            json.put("day",day);
            json.put("hour",hour);
            json.put("min",min);
            json.put("s",s);
            json.put("ms",ms);
            return json;
        }catch (ParseException e){
            log.error(e.getMessage() , e);
        }
        return null;
    }
}
