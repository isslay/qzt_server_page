package com.qzt.common.tools;

import com.github.qcloudsms.SmsSingleSender;

import java.io.IOException;

public class SmsTools {
    public void sendSms(String phoneNumber, int templateId, String[] params) throws IOException {

        SmsSingleSender ssender = new SmsSingleSender(1400057682, "5df31e529354c82e8aa2c4dfc214998f");
        try {
            ssender.sendWithParam("86", phoneNumber, templateId, params, "仁和正通", "", "");
        } catch (com.github.qcloudsms.httpclient.HTTPException e) {
            e.printStackTrace();
        }
    }


}
