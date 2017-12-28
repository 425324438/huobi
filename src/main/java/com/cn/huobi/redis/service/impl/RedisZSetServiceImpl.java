package com.cn.huobi.redis.service.impl;

import com.cn.huobi.redis.service.RedisZSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:44
 */
@Service
public class RedisZSetServiceImpl implements RedisZSetService {
    /********************** zset(sorted set：有序集合) 操作 ****************************/

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
    @ Override
    public Boolean setAdd(String key, Object value, double score) {
        return template.opsForZSet().add(key,value,score);
    }

    /**
     * 键为K的集合，Smin<=score<=Smax的元素个数
     * @param Smin Smin
     * @param Smax Smax
     */
    @Override
    public Long setcount(String key, double Smin, double Smax) {
        return template.opsForZSet().count(key,Smin,Smax);
    }

    @Override
    public Long setSize(String key) {
        return template.opsForZSet().size(key);
    }

    /**
     * 键为K的集合，value为obj的元素分数
     */
    @Override
    public Double setScore(String K, Object score) {
        return template.opsForZSet().score(K,score);
    }

    /**
     * 元素分数增加，delta是增量
     *
     */
    @Override
    public Double setIncrementScore(String k, Object obj, double delta) {
        return template.opsForZSet().incrementScore(k,obj,delta);
    }

    /**
     * @param start 起始位置
     * @param end   结束位置
     */
    @Override
    public Set<Object> setange(String k, long start, long end) {
        return template.opsForZSet().range(k,start,end);
    }
}
