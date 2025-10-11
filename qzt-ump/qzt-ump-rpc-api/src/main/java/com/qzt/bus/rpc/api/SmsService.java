package com.qzt.bus.rpc.api;

public interface SmsService {

    /**
     *
     * @param smsOpen 是否开启短信
     * @param megOpen 是否开启站内信
     * @param jPushOpen 是否开启极光推送
     * @param phoneNumber 接收号码
     * @param messObj templateId 发送短信编号，参考模板（com.qzt.common.web.configuration.ContentTemplet）templateContent 发送推送和站内信内容，参考模板（com.qzt.common.web.configuration.ContentTemplet）
     * @param params 根据发送模板，传递参数。（注意顺序！！！）
     */
    void sendSMS(Boolean smsOpen,Boolean megOpen,Boolean jPushOpen,String phoneNumber,Object [] messObj,String[] params );
}
