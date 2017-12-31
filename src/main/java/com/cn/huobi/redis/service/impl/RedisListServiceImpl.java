package com.cn.huobi.redis.service.impl;

import com.cn.huobi.redis.service.RedisListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:43
 */
@Service
public class RedisListServiceImpl implements RedisListService {


    private RedisTemplate template;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.template = redisTemplate;
    }

    /********************** List 操作  ****************************/
    @Override
    public Long listSize(String key) {
        return template.opsForList().size(key);
    }

    @Override
    public void listSet(String key, Object obj) {
        template.opsForList().leftPush(key, obj);
    }
    /**
     * @param var1:  key
     * @param var2 : 删除的位置
     * @param var4 ：删除的值
     */
    @Override
    public Long listRemove(String var1, long var2, Object var4) {
        return template.opsForList().remove(var1,var2,var4);
    }

    @Override
    public Object listIndex(String var1, long var2) {
        return template.opsForList().index(var1,var2);
    }

    @Override
    public List<Object> listrange(String var1, long var2, long var4) {
        return template.opsForList().range(var1,var2,var4);
    }

    @Override
    public List<String> range(String k) {
        List<String> lis = template.opsForList().
                range(k,0,template.opsForList().size(k));
        return lis;
    }
}
