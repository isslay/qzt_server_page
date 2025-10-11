package com.qzt.common.pay.util;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.pay.config.PayWxConfig;
import com.qzt.common.redis.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Configuration
@EnableConfigurationProperties(PayWxConfig.class)
public class WeChatTools {

    @Autowired
    public PayWxConfig payWxConfig;

    private static DefaultHttpClient httpClient = new DefaultHttpClient();

    public static String SHA1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");//选择SHA-1，也可以选择MD5
            byte[] digest = md.digest(inStr.getBytes());//返回的是byet[]，要转化为String存储比较方便
            outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
    }

    public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }

    public String getAccessToken() {
        String token = (String) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value());
        if (token == null || token.equals("")) {
            WechatConstants wc = getWechatConstants();
            try {
                HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wc.appId + "&secret=" + wc.appSecret);
                HttpResponse response = httpClient.execute(get);
                String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                JSONObject demoJson = new JSONObject(jsonStr);
                token = demoJson.getString("access_token");
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value(), token);
                CacheUtil.getCache().expire(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value(), 3600);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //  log.info("access_token==end "+token);
        return token;
    }


    public String getJsApiTicket() {
        String ticket = (String) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.TICKET.value());
        if (ticket == null || ticket.equals("")) {
            try {
                HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getAccessToken());
                HttpResponse response = httpClient.execute(get);
                String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                JSONObject demoJson = new JSONObject(jsonStr);
                if (!demoJson.get("errcode").equals("0")) {
                    CacheUtil.getCache().del(SysConstant.CacheNamespaceEnum.TICKET.value());
                    get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getAccessToken());
                    response = httpClient.execute(get);
                    jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                    demoJson = new JSONObject(jsonStr);
                    ticket = demoJson.getString("ticket");
                    CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.TICKET.value(), ticket);
                    CacheUtil.getCache().expire(SysConstant.CacheNamespaceEnum.TICKET.value(), 3600);
                } else {
                    ticket = demoJson.getString("ticket");
                    CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.TICKET.value(), ticket);
                    CacheUtil.getCache().expire(SysConstant.CacheNamespaceEnum.TICKET.value(), 3600);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ticket;
    }

    public WechatConstants getWechatConstants() {
        WechatConstants wc = new WechatConstants();
        wc.setAppId(this.payWxConfig.getWeChatAppId());
        wc.setAppSecret(this.payWxConfig.getWeChatSecret());
        return wc;
    }

    public WxJsApiConfig getWxJsApiConfig(String url) {
        WechatConstants wc = getWechatConstants();
        WxJsApiConfig config = new WxJsApiConfig();
        config.appId = wc.getAppId();
        config.url = url;
        String jsApiTicket = getJsApiTicket();
        log.info(jsApiTicket);
        String str = "jsapi_ticket=" + jsApiTicket +
                "&noncestr=" + config.nonceStr + "&timestamp=" + config.timestamp + "&url=" + url;
        log.info(str);
        config.signature = SHA1(str);
        return config;
    }

    public String getUserInfo(String code) throws IOException {
        WechatConstants wc = getWechatConstants();
        WxJsApiConfig config = new WxJsApiConfig();
        return getOpenId(code);
    }

    public String getWXUserInfo(String openid, String accessToken) throws IOException {
        log.info("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN");
        HttpGet get = HttpClientConnectionManager
                .getGetMethod("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN");
        HttpResponse response = httpClient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(),
                "utf-8");
        //   JSONObject demoJson = new JSONObject(jsonStr);
        log.info(jsonStr);
        return jsonStr;
    }

    public String getOpenId(String code) throws IOException {
        WechatConstants wc = getWechatConstants();
        String openid = "";
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&appid=" + wc.appId + "&secret=" + wc.appSecret + "&code=" + code;
        net.sf.json.JSONObject json = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(tokenUrl);
        HttpResponse hresponse;
        Map<String, String> map = new HashMap<String, String>();
        try {
            hresponse = httpclient.execute(httpget);
            HttpEntity entity = hresponse.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            json = net.sf.json.JSONObject.fromObject(content);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        openid = json.getString("openid");
        String access_token = json.getString("access_token");
        return getWXUserInfo(openid, access_token);
    }


}