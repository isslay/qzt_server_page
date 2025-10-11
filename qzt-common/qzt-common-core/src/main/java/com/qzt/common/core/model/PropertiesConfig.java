package com.qzt.common.core.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by cgw on 2018/5/30.
 */
@Component
@ConfigurationProperties(prefix ="sms")
public class PropertiesConfig {

    public int appid;

    public String appkey;

    public  String testPhone;

    public  String testFlage;

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getTestPhone() {
        return testPhone;
    }

    public void setTestPhone(String testPhone) {
        this.testPhone = testPhone;
    }

    public String getTestFlage() {
        return testFlage;
    }

    public void setTestFlage(String testFlage) {
        this.testFlage = testFlage;
    }
}
