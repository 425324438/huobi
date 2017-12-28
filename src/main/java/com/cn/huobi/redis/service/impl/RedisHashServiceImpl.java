package com.cn.huobi.redis.service.impl;

import com.cn.huobi.redis.service.RedisHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:35
 */
@Service
public class RedisHashServiceImpl implements RedisHashService {

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
    public Map<Object, Object> getHash(String key) {
        HashOperations hashOperations = template.opsForHash();
        Map map = hashOperations.entries(key);
        return map;
    }

    @Override
    public void setHash(String key,Map map) {
        HashOperations hashOperations = template.opsForHash();
        hashOperations.putAll(key,map);
    }

    @Override
    public Object getHashObject(String key, Object mapKey) {
        HashOperations hashOperations = template.opsForHash();
        return hashOperations.get(key,mapKey);
    }

    @Override
    public Long HashDelet(String key, Object object) {
        HashOperations hashOperations = template.opsForHash();
        return hashOperations.delete(key,object);
    }

    @Override
    public Boolean hasKey(String var1, Object var2) {
        HashOperations hashOperations = template.opsForHash();
        return hashOperations.hasKey(var1,var2);
    }
}
