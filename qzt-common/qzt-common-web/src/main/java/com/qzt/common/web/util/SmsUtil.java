package com.qzt.common.web.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import com.qzt.common.core.model.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * Created by cgw on 2018/5/30.
 */
@Slf4j
@Component
@Configuration
@EnableConfigurationProperties(PropertiesConfig.class)
public class SmsUtil {

    @Autowired
    private PropertiesConfig pc;


    public void sendSms(String phoneNumber, int templateId, String[] params) {

        SmsSingleSender ssender = new SmsSingleSender(pc.appid, pc.appkey);
        if (pc.testFlage.equals("1")) {
            phoneNumber = pc.testPhone;
        }
        try {
            ssender.sendWithParam("86", phoneNumber, templateId, params, "七足堂", "", "");
            log.info("phoneNumber=" + phoneNumber + "^templateId=" + templateId + "^params=" + params);
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] agrs) {
        String[] params = new String[3];
        params[0] = "1234";
        params[1] = "注册";
        params[2] = "10";
        //sendSms("13701337948",71464,params);
    }
}
