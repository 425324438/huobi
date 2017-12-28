package com.cn.huobi.redis.service;

import java.util.Set;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:30
 */
public interface RedisZSetService {


    /********************** zset(sorted set：有序集合) 操作****************************/
    Boolean setAdd(String key, Object value, double var3);
    /**
     * 键为K的集合，Smin<=score<=Smax的元素个数
     */
    Long setcount(String key, double Smin, double Smax);

    Long setSize(String key);
    /**
     * 键为K的集合，value为obj的元素分数
     */
    Double setScore(String K, Object obj);

    /**
     * 元素分数增加，delta是增量
     */
    Double setIncrementScore(String k, Object obj, double delta);

    /**
     * @param start 起始位置
     * @param end 结束位置
     */
    Set<Object> setange(String k, long start, long end);
}
