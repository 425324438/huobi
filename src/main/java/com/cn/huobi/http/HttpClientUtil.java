package com.cn.huobi.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * @author 425324438@qq.com
 * @date 2017/12/20 13:05
 */
@Repository
public class HttpClientUtil {
    protected static Log log = LogFactory.getLog(HttpClientUtil.class);

    private String apiKey = null;
    private String secretKey = null;
    private String uecUrl = null;
    private String baseUrl = null;

    public JSONObject apiHTTPGET(String apiUri, String params) {
        JSONObject jsonResult = null;

        // 1. 拼接请求地址
        String url = apiUri;
        // 1.1 检查是否已包含http来判断是否需要添加baseUrl
        if (!apiUri.startsWith("http")) {
            url = uecUrl + apiUri;
        }

        // 1.2 添加参数param
        if (params != null && !"".equals(params.trim())) {
            if (params.startsWith("/") || params.startsWith("?") || params.startsWith("&")) {
                url = url + params;
            } else {
                url = url + "?" + params;
            }
        }
        System.out.println("url="+url);
        long t1 = System.currentTimeMillis();
        long time = 0;
        if (log.isDebugEnabled()) {
            log.debug("start getJsonByGetData\r\nurl=" + url + "\r\n");
        }

        String result = null;
        String originResult = null;
        // 2. get方法请求
        HttpResponse response = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "text/html;charset=utf-8");
            signature(httpGet, apiUri);

            response = client.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
            }
            System.out.println("result="+result);
        } catch (Exception e) {
            result = "{rc:909," + "msg:\"" + e.getMessage() + "\"," + "value:[]}";
        } finally {
            time = System.currentTimeMillis() - t1;
            if (log.isDebugEnabled()) {
                log.debug("end...getStrByGetData,time=" + time + "ms\r\nurl=" + url + "\r\nresult=" + result + "\r\n");
            }
        }
        jsonResult = getJsonResult(result);
        traceLog(jsonResult, url, params, time);
        return jsonResult;
    }

    public JSONObject apiHTTPPOST(String apiUri ,JSONObject jsonData){
        String url = apiUri;
        JSONObject jsonResult = null;
        checkConfig();
        if (!apiUri.startsWith("http")) {
            url = uecUrl + apiUri;
        }
        jsonData = checkUserId(jsonData);
        long t1 = System.currentTimeMillis();
        long time = 0;
        String result = null;
        HttpResponse response = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            signature(httpPost, apiUri);
            StringEntity entityStr = new StringEntity(jsonData.toString(), "UTF-8");
            httpPost.setEntity(entityStr);

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
            System.out.println("<==========>"+result);
        } catch (Exception e) {
            result =  "{rc:999," + "msg:\""+e.getMessage()+"\"," + "value:[]}";
        } finally {
            time = System.currentTimeMillis() - t1;
            if (log.isDebugEnabled()) {
                log.debug("end...getStrByPutData,time=" + time + "ms\r\nurl=" + url + "\r\nresult=" + result + "\r\n");
            }
        }
        jsonResult = getJsonResult(result);
        traceLog(jsonResult, url, jsonData.toString(), time);
        return jsonResult;
    }


    /**
     *  得到HttpClient
     * @return  返回 HttpClient对象
     */
    private HttpClient getHttpClient() {
        HttpParams mHttpParams = new BasicHttpParams();
        // 设置网络链接超时
        // 即:Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(mHttpParams, 4* 1000);
        // 设置socket响应超时
        // 即:in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(mHttpParams, 20 * 1000);
        // 设置socket缓存大小
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 30 * 1024);
        // 设置是否可以重定向
        HttpClientParams.setRedirecting(mHttpParams, true);

        HttpClient httpClient = new DefaultHttpClient(mHttpParams);
        return httpClient;
    }
    protected void traceLog(JSONObject jsonResult, String url, String params,
                            long time) {
        if (jsonResult.getInteger("rc") > 0) {
            String message = null;
            if (params == null || "".equals(params.trim())) {
                message = "";
            } else {
                message = "params=" + params + "<br/>\r\n";
            }
            message += "result(" + time + "ms)=" + jsonResult.toString();

            if (log.isErrorEnabled()) {
                log.debug("ronglian-error\r\nurl=" + url + "\r\n" + message
                        + "\r\n");
            }
        } else {
            if (log.isDebugEnabled()) {
                String message = null;
                if (params == null || "".equals(params.trim())) {
                    message = "";
                } else {
                    message = "params=" + params + "<br/>\r\n";
                }
                message += "result(" + time + "ms)=" + jsonResult.toString();

                log.debug("ronglian-trace\r\nurl=" + url + "\r\n" + message
                        + "\r\n");


            }

        }

    }

    public JSONObject getJsonResult(String jsonStr) {
        JSONObject jsonResult = null;
        String msg = null;
        try {
            jsonResult = JSONObject.parseObject(jsonStr);
            if (jsonResult == null || jsonResult.isEmpty()) {
                msg = "json result formate error";
            } else {
                if (!jsonResult.containsKey("rc")) {
                    msg = "json result formate error, param rc is not exist.";
                } else if (jsonResult.containsKey("values")) {
                    try {
                        jsonResult.getJSONArray("values");
                    } catch (Exception e) {
                        msg = e.getMessage();
                    }
                } else if (jsonResult.containsKey("value")) {
                    // TODO value是否只允许为非数组
                    try {
                        jsonResult.getJSONObject("value");
                    } catch (Exception e) {
                        msg = e.getMessage();
                    }
                }else if(jsonResult.containsKey("result")){

                    try {
                        jsonResult.getJSONArray("result");
                    } catch (Exception e) {
                        msg = e.getMessage();
                    }

                }
            }
            if (msg != null) {
                JSONObject originResult = jsonResult;
                jsonResult = new JSONObject();
                jsonResult.put("rc", 907);
                jsonResult.put("msg", msg);
                jsonResult.put("originResult", originResult);
                jsonResult.put("value",  new JSONArray());
            }

            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = new JSONObject();
            jsonResult.put("rc", 908);
            msg = "not json formate," + e.getMessage();
            jsonResult.put("msg", msg);
            jsonResult.put("originResult", jsonStr);
            jsonResult.put("value", new JSONArray());


        }
        return jsonResult;
    }



    private String createSignature(TreeMap<String, String> sortMap,
                                   String secretkey) {
        String signature = null;
        try {
            StringBuffer sb = new StringBuffer();

            for (String key : sortMap.keySet()) {
                String value = sortMap.get(key);
                sb.append(key + "=" + value + "&");
                ;
            }
            sb.append(secretkey);

            String encodeString = urlEncode(sb.toString());
            if (encodeString != null) {
                encodeString = encodeString.replaceAll("\\*", "%2A");
            }
            signature = toMD5Hex(encodeString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;

    }

    private String toMD5Hex(String datas) {
        String result = null;
        try {
            result = toMD5Hex(datas, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String toMD5Hex(String datas, String charset)
            throws UnsupportedEncodingException {
        return toMD5Hex(datas.getBytes(charset));
    }

    private String toMD5Hex(byte[] datas) {
        return toHexString(toMD5(datas));
    }

    private String toHexString(byte[] datas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            String hex = Integer.toHexString(datas[i] & 0xFF);
            if (hex.length() <= 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private byte[] toMD5(byte[] datas) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");
            return md.digest(datas);
        } catch (NoSuchAlgorithmException e) {
            // throw new BCMSException();
            return null;
        }
    }

    private String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(url);
        }
    }
    private String createSignature(String uri) {
        TreeMap<String, String> sortMap = new TreeMap<String, String>();
        sortMap.put("apikey", apiKey);
        // 去掉URI前的版本信息
        // 例如api/v1/resource/vm=>resource/vm

        if (uri != null && uri.startsWith("api/v")) {
            int n = uri.indexOf("/", 5);
            if (n > 0) {
                uri = uri.substring(n + 1);
            }
        }
        sortMap.put("uri", uri);
        System.out.println("uri="+uri);
        String signature = createSignature(sortMap, secretKey);
        return signature;
    }

    protected void signature(HttpRequestBase httpRequest, String uri) {
        httpRequest.addHeader("apikey", apiKey);
        httpRequest.addHeader("signature", createSignature(uri));
    }

    protected JSONObject checkUserId(JSONObject jsonParams){
        if(jsonParams==null){
            jsonParams = new JSONObject();
        }
        try{
            if(jsonParams.containsKey("userid")) {

            };
        }catch(Exception e){

        }
        return jsonParams;
    }


    public void checkConfig() {

        if (baseUrl == null) {


        }
    }
}
