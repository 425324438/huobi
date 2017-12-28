package com.cn.huobi.redis.service.impl;

import com.cn.huobi.redis.service.RedisStrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:32
 */
@Service
public class RedisStrServiceImpl implements RedisStrService {

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
    @Override
    public Object getKey(String key) {
        ValueOperations valueOperations = template.opsForValue();
        Object o = valueOperations.get(key);
        return o;
    }

    @Override
    public void setKey(String key, String val) {
        template.opsForSet().add(key,val);
    }
}
