package com.cn.huobi.https;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 425324438@qq.com
 */
public class TestHttpsMain {

	 private String url = "https://master.ond6.com/service/config/get";
	    private String charset = "utf-8";  
	    private HttpsClientUtil httpsClientUtil = null;
	      
	    public TestHttpsMain(){
	        httpsClientUtil = new HttpsClientUtil();
	    }
	      
	    public void test(){  
	        String httpOrgCreateTestURL = url + "httpOrg/create";  
	        Map<String,String> createMap = new HashMap<String,String>();  
	        createMap.put("authuser","*****");  
	        createMap.put("authpass","*****");  
	        createMap.put("orgkey","****");  
	        createMap.put("orgname","****");  
	        String httpOrgCreateTestRtn = httpsClientUtil.doPost(httpOrgCreateTestURL,createMap,charset);
	        System.out.println("result:"+httpOrgCreateTestRtn);  
	    }
        public void testGet(){
        String httpOrgCreateTestURL = url + "httpOrg/create";
        Map<String,String> createMap = new HashMap<String,String>();
        createMap.put("authuser","*****");
        createMap.put("authpass","*****");
        createMap.put("orgkey","****");
        createMap.put("orgname","****");
        String httpOrgCreateTestRtn = httpsClientUtil.doGet(url,createMap,charset);
        System.out.println("result:"+httpOrgCreateTestRtn);
    }
	      
	    public static void main(String[] args){  
	    	TestHttpsMain main = new TestHttpsMain();  
	        main.testGet();
	    }  
	    
}
