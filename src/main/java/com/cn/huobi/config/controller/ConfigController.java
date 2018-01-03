package com.cn.huobi.config.controller;

import com.cn.huobi.email.EmailSend;
import com.cn.huobi.redis.service.RedisHashService;
import com.cn.huobi.redis.service.RedisListService;
import com.cn.huobi.redis.service.RedisStrService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoliping
 * date：2017/12/29
 * time：22:47
 */
@Controller
public class ConfigController {

    @Autowired
    private RedisStrService redisStrService;
    @Autowired
    private RedisHashService redisHashService;
    @Autowired
    private RedisListService redisListService;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private EmailSend emailSend;

    @RequestMapping("/config")
    public String index(ModelMap map) {
        return "welcome";
    }
    /**
     * @return 当前系统关注的货币
     */
    @ResponseBody
    @RequestMapping("/currency")
    public String getCurrencyList(){
        String currencyList = redisStrService.getKey("currencyList").toString();
        return currencyList;
    }

    /**HttpServletRequest req
     * @return  修改当前系统关注的货币
     */
    @ResponseBody
    @RequestMapping("/updateCurrency")
    public void updateCurrency(HttpServletRequest req){
        String currency = req.getParameter("currency");
        redisStrService.setKey("currencyList",currency);
    }
    /**
     * @return 当前系统用户
     */
    @ResponseBody
    @RequestMapping("/user")
    public List<String> getUser(){
        List<String> user = redisListService.range("userEmail");
        return user;
    }
    /**
     * @return 添加当前系统用户
     */
    @ResponseBody
    @RequestMapping("/addUser")
    public String addUser(HttpServletRequest req){
        String user = req.getParameter("user");
        String currency = req.getParameter("currency");

        String email = req.getParameter("email");
        String start = req.getParameter("user_start");
        String end = req.getParameter("user_end");
        JSONObject userJson = new JSONObject();
        userJson.put("user",user);
        userJson.put("currency",currency);
        userJson.put("email",email);
        userJson.put("start",start);
        userJson.put("end",end);
        redisListService.listSet("userEmail",String.valueOf(userJson));
        return "success";
    }

    /**
     * @return 删除当前系统用户
     */
    @ResponseBody
    @RequestMapping("/removeUser")
    public String removeUser(HttpServletRequest req){
        String user = req.getParameter("user");
        String email = req.getParameter("currency");
        String currency = req.getParameter("email");
        String start = req.getParameter("user_start");
        String end = req.getParameter("user_end");

        JSONObject userJson = new JSONObject();
        userJson.put("user",user);
        userJson.put("currency",currency);
        userJson.put("email",email);
        userJson.put("start",start);
        userJson.put("end",end);
        List<String> lis = template.opsForList().range("userEmail",0,template.opsForList().size("userEmail"));
        Long count = Long.MIN_VALUE;
        for(int i=0;i< lis.size();i++){
            String str = lis.get(i);
            JSONObject strJsonData = JSONObject.fromObject(str);
            if(strJsonData.has("user")){
                String userEmail = strJsonData.getString("user");
                if(userEmail.equals(user)){
                    count = template.opsForList().remove("userEmail",i+1,String.valueOf(userJson));
                    break;
                }
            }
        }
        if( count > 0 ){
            return "success";
        }else{
            return "error";
        }
    }

    /**
     *  用户发送测试邮件
     */
    @ResponseBody
    @RequestMapping("/testEmail")
    public String testEmail(HttpServletRequest req){
        String userEmail = req.getParameter("user");
        String currency = req.getParameter("currency");
        String[] arr = currency.split(",");
        List list = java.util.Arrays.asList(arr);
        return emailSend.testSendEmailToUser(list,userEmail);
    }

}
