package com.cn.huobi.redis.service;

import java.util.Set;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:29
 */
public interface RedisSetService {

    /********************** Set 操作****************************/
    Long setAdd(String kdy, Object val);
    /**
     * 返回所有
     */
    Set<Object> setMembers(String var1);

    /**
     * 删除元素
     */
    Long setRemove(String key, Object var2);

    Long setSize(String var1);
}
