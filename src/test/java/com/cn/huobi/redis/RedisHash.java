package com.cn.huobi.redis;

import com.cn.huobi.redis.service.RedisHashService;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * 开发者 liaoliping
 * date：2018/1/5
 * time：21:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisHash {

    @Autowired
    private RedisHashService redisHashService;

    @Test
    public void Test(){
        Object obj = redisHashService.getHashObject("monitor_30eosusdt","dataTime");
//        Object minTime = redisHashService.getHashObject("monitor_30"+currency,"dataTime");
        System.out.print(obj);
    }
}
