package com.cn.huobi.redis.service;

import java.util.Map;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:29
 */
public interface RedisHashService {


    /********************** Hash 操作****************************/
    Map<Object,Object> getHash(String key);

    void setHash(String key, Map map);

    Object getHashObject(String key, Object mapKey);

    Long HashDelet(String key, Object object);

    Boolean hasKey(String var1, Object var2);

}
