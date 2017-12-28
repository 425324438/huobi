package com.cn.huobi.redis.service;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:29
 */
public interface RedisStrService {
    /********************** Syting 操作****************************/
    Object getKey(String key);

    void setKey(String key, Object val);

}
