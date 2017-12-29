package com.cn.huobi.redis;

import javafx.application.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 17:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate template;

    @Test
    public void Test(){
        template.opsForValue().get("username");
    }

    public static void main(String[] args) {
        //涨幅 = （之前价格 -  当前价格 ） /  当前价格 ，
        Double up = 1.3406;  //当前
        Double clo = 1.3405; //之前
        Double rose  =    (clo - up) / clo;
        String msg  ="";
        if(clo < up){
            msg = "上涨";
        }else{
            msg = "下跌";
        }
        System.out.print(msg+"："+rose);
    }
}
