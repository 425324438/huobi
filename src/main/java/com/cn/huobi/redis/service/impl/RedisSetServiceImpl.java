package com.cn.huobi.redis.service.impl;

import com.cn.huobi.redis.service.RedisSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:38
 */
@Service
public class RedisSetServiceImpl implements RedisSetService {

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

    /********************** Set 操作     * @param   kdy    * @param val     ****************************/
    @Override
    public Long setAdd(String key, Object val) {
        return template.opsForSet().add(key,val);
    }

    /**
     * 返回所有
     */
    @Override
    public Set<Object> setMembers(String key) {
        return template.opsForSet().members(key);
    }

    /**
     * 删除元素
     */
    @Override
    public Long setRemove(String key, Object var2) {
        return template.opsForSet().remove(key,var2);
    }
    @Override
    public Long setSize(String var1) {
        return template.opsForSet().size(var1);
    }
}
