package com.cn.huobi.redis.service;

import java.util.List;

/**
 * @author 425324438@qq.com
 * @date 2017/12/19 12:29
 */
public interface RedisListService {


    /********************** List 操作****************************/

    Long listSize(String key);

    void listSet(String key, Object obj);

    Long listRemove(String var1, long var2, Object var4);

    Object listIndex(String var1, long var2);

    List<Object> listrange(String var1, long var2, long var4);

}
