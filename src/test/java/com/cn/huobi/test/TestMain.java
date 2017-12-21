package com.cn.huobi.test;

import com.cn.huobi.util.HMACUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 425324438@qq.com
 * @date 2017/12/21 12:41
 */
public class TestMain {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") ;

    public static void main(String[] args) {
        String url="/market/history/kline";
        String security= "";
        security += "GET\napi.huobi.pro\n"+url+"\n";
        Map<String,String> paraMap = new HashMap<String,String>();
        paraMap.put("AccessKeyId","114e4d26-131e6d43-91fdd5c4-503f8");
        paraMap.put("SignatureMethod","HmacSHA256");
        paraMap.put("order-id","1234567890");
        paraMap.put("SignatureVersion","2");
        paraMap.put("Timestamp",format.format(new Date()));
        String str = HMACUtil.formatUrlMap(paraMap,true,false);
        security = security + str;
        System.out.print("要签名的数据：\n"+security+"\n");
        String mima = HMACUtil.encrytSHA256(security,"huobi.Secret");
        String base64 = new sun.misc.BASE64Encoder().encode(mima.getBytes());
        System.out.print("签名完成后转码为base64的数据：\n"+base64);
        try {
            URLEncoder.encode(base64,"UTF-8");
        }catch(UnsupportedEncodingException e){
            e.getMessage();
        }

    }

}
