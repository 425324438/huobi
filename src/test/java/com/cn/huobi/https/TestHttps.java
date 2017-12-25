package com.cn.huobi.https;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 425324438@qq.com
 * @date 2017/12/20 13:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestHttps {

    @Value("${huobi.market}")
    private String market ;
    @Value("${huobi.trade}")
    private String trade ;
    private String charset = "utf-8";
    @Autowired
    private HttpsClientUtil httpsClientUtil;

    @Test
    public void  testPost(){
        Map<String,String> createMap = new HashMap<String,String>();
        String httpOrgCreateTestRtn = httpsClientUtil.doPost(market,createMap,charset);
        System.out.println("result:"+httpOrgCreateTestRtn);
    }

    @Test
    public void  testGet(){
        Map<String,String> createMap = new HashMap<String,String>();
        createMap.put("symbol","xrpusdt");
        createMap.put("period","1min");
        createMap.put("size","10");
        String httpOrgCreateTestRtn = httpsClientUtil.doGet("https://api.huobi.pro/market/history/kline",createMap,charset);
        System.out.println("result:"+httpOrgCreateTestRtn);
    }

    @Test
    public void  testsymbols(){
        Map<String,String> createMap = new HashMap<String,String>();
//        createMap.put("base-currency","xrp");
//        createMap.put("quote-currency","usdt");
//        createMap.put("symbol-partition","main");
        String httpOrgCreateTestRtn = httpsClientUtil.doGet("https://api.huobi.pro/v1/common/symbols",createMap,charset);
        System.out.println("result:"+httpOrgCreateTestRtn);
    }


}
