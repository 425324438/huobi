package com.cn.huobi.util;

import com.cn.huobi.job.SchedledConfiguration;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 开发者 liaoliping
 * date：2017/12/28
 * time：23:47
 */
public  class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    /**
     * @param dateStr1  DateStr1 = "2017-12-28 11:32:35";
     * @param dateStr2  DateStr2 = "2017-12-28 11:32:35";
     * @return
     * @throws ParseException
     */
    public static  JSONObject dateDiffer(String dateStr1,String dateStr2)   {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date dateTime1 = dateFormat.parse(dateStr1);
            Date dateTime2 = dateFormat.parse(dateStr2);
            long between = dateTime1.getTime() - dateTime2.getTime();
            long day = between / (24 * 60 * 60 * 1000);
            long hour = (between / (60 * 60 * 1000) - day * 24);
            long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            System.out.println("相差："+day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
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

    /**
     * 判断时间是否在时间段内
     * @param nowTime 当前时间
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param startTimeStr 格式 06:00
     * @param endTimeStr    格式 22:00
     * @param time           当前时间
     * @return 如果在区间之内 则返回 true
     */
    public static boolean isBelong(String startTimeStr,String endTimeStr,Date time){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date beginTime = null;
        Date endTime = null;
        try {
            time = df.parse(df.format(new Date()));
            beginTime = df.parse(startTimeStr);
            endTime = df.parse(endTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean flag = belongCalendar(time, beginTime, endTime);
        if (flag) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.print(DateUtil.isBelong("19:07","22:00", new Date()));
    }
}
