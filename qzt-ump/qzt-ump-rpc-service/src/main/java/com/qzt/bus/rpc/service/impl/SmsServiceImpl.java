package com.qzt.bus.rpc.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.qzt.bus.rpc.api.SmsService;
import com.qzt.common.core.model.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@Configuration
@EnableConfigurationProperties(PropertiesConfig.class)
@Service("smsService")
public class SmsServiceImpl implements SmsService {

    @Autowired
    private PropertiesConfig pc;

    @Override
    public void sendSMS(Boolean smsOpen, Boolean megOpen, Boolean jPushOpen, String phoneNumber, Object[] messObj, String[] params) {
        try {
            //短信开启
            if (smsOpen) {
                int templateId = Integer.parseInt(messObj[0].toString());
                SmsSingleSender ssender = new SmsSingleSender(pc.appid, pc.appkey);
                String sendNum = phoneNumber;
                if (pc.testFlage.equals("1")) {
                    sendNum = pc.testPhone;
                }
                SmsSingleSenderResult smsSingleSenderResult = ssender.sendWithParam("86", sendNum, templateId, params, "仁和", "", "");
                log.info(smsSingleSenderResult.errMsg+"  "+smsSingleSenderResult.result+"  phoneNumber=" + sendNum + "^templateId=" + templateId + "^params=" + StringUtils.join(params, "-"));
            }
            //站内信开启
            if (megOpen) {


            }
            //极光推送开启
            if (jPushOpen) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
