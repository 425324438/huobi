package com.cn.huobi.util;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.net.URLEncoder;
import java.security.Security;
import java.util.*;


/**
 * @author 425324438@qq.com
 * @date 2017/12/20 13:48
 */
public  class HMACUtil {

    public static String encrytSHA256(String content, String secret) {

        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF8"),
                    "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] digest = mac.doFinal(content.getBytes("UTF-8"));
            return new HexBinaryAdapter().marshal(digest);
        } catch (Exception e) {
            throw new RuntimeCryptoException("加密异常");
        }

    }


    /**
     *
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URLENCODE
     * @param keyToLower    是否需要将Key转换为全小写
     *            true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
    {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>(){
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2){
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                if (StringUtils.isNotBlank(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }
}
