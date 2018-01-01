package com.cn.huobi.redis;

import com.cn.huobi.redis.service.RedisListService;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 开发者 liaoliping
 * date：2017/12/29
 * time：21:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisListTest {

    @Autowired
    private RedisListService redisListService;
    @Autowired
    private StringRedisTemplate template;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Test
    public void Test(){
        JSONObject huobi = new JSONObject();
        huobi.put("xrpusdt","65765");
        huobi.put("dataTime",dateFormat.format(new Date()));

        template.opsForList().leftPush("monitor_30", String.valueOf(huobi));
    }

    @Test
    public void getTest(){
        List<String> lis = template.opsForList().range("currencyList",0,template.opsForList().size("currencyList"));
        for (int i=0;i < lis.size();i++){
            System.out.print(lis.get(i)+"\n");
        }
    }

    @Test
    public void removeTest(){

        JSONObject userJson = new JSONObject();
        userJson.put("user","asdawd");
        userJson.put("currency","dqwdqw");
        userJson.put("email","qwdqw");

        List<String> lis = template.opsForList().range("userEmail",0,template.opsForList().size("userEmail"));
        for(int i=0;i< lis.size();i++){
            String str = lis.get(i);
            JSONObject strJsonData = JSONObject.fromObject(str);
            if(strJsonData.has("user")){
                String userEmail = strJsonData.getString("user");
                if(userEmail.equals("asdawd")){
                    template.opsForList().remove("userEmail",i+1,String.valueOf(userJson));
                }
            }
        }
    }
}
