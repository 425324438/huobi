package com.cn.huobi.https;

import com.cn.huobi.util.HMACUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author 425324438@qq.com
 */
@Repository
public class HttpsClientUtil {

	private static Log log = LogFactory.getLog(HttpsClientUtil.class);

    @Value("${huobi.market}")
    private String market ;
    @Value("${huobi.trade}")
    private String trade ;
    @Value("${huobi.AccessKeyId}")
    private String AccessKeyId;
    @Value("${huobi.SignatureMethod}")
    private String SignatureMethod;
    @Value("${huobi.SignatureVersion}")
    private String SignatureVersion;
    @Value("${huobi.Secret}")
    private String Secret;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") ;
    private String charset = "utf-8";


	public String doPost(String url,Map<String,String> map,String charset){
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = new SslClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept-Language", "zh-cn");
            httpPost.addHeader("user agent", "User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){  
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }
	
	public String doGet(String url,Map<String,String> map,String charset){
        HttpClient httpClient = null; 
        HttpGet httpGet = null;
        String result = null;
            //设置参数
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : map.entrySet()){
                if (StringUtils.isNotBlank(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    buf.append(key + "=" + val);
                    buf.append("&");
                }
            }
            if (!buf.equals("")){
                url += "?"+buf;
            }
        try{  
            httpClient = new SslClient();
            httpGet = new HttpGet(url);
            signature(httpGet, url);
            System.out.print("---火币网API----URL----------"+url+"\n");
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);
                }
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }
        return result;  
    }


    private void signature(HttpGet httpGet ,String url){
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Accept-Language", "zh-cn");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        String security= "";
        security += "GET\napi.huobi.pro\n"+url+"\n";
        Map<String,String> paraMap = new HashMap<String,String>();
        paraMap.put("AccessKeyId",AccessKeyId);
        paraMap.put("SignatureMethod",SignatureMethod);
        paraMap.put("SignatureVersion",SignatureVersion);
        paraMap.put("Timestamp",format.format(new Date()));
        String str = HMACUtil.formatUrlMap(paraMap,true,false);
        security = security + str;
        String mima = HMACUtil.encrytSHA256(security,"197b7634-8887df09-d41c81bb-72e5a");
        String base64 = new sun.misc.BASE64Encoder().encode(mima.getBytes());
        try {
            URLEncoder.encode(base64,"UTF-8");
        }catch(UnsupportedEncodingException e){
            e.getMessage();
        }

    }
}
