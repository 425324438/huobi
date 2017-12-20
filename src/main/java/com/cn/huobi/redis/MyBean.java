package com.cn.huobi.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 17:27
 */
@Component
public class MyBean {
    @Autowired
    private StringRedisTemplate template;

    public MyBean(StringRedisTemplate template) {
        this.template = template;
    }
}
