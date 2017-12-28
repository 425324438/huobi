package com.cn.huobi.message.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * @author 425324438@qq.com
 * @date 2017/12/8 19:47
 */
public interface SendSms {
    /**
     *
     * @param msg 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
     *             request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
     * @param phone 手机号
     * @param templateCode 短信模板-可在短信控制台中找到
     * @throws ClientException
     */
    SendSmsResponse sendSms(String phone,String msg,String templateCode) throws ClientException;

    QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException;

}
