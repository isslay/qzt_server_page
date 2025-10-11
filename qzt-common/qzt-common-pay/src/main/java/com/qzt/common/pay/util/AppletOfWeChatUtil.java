package com.qzt.common.pay.util;

import com.alibaba.fastjson.JSON;
import com.qzt.common.pay.config.PayWxConfig;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Log4j
@Component
@Configuration
@EnableConfigurationProperties(PayWxConfig.class)
public class AppletOfWeChatUtil {

    @Autowired
    public PayWxConfig payWxConfig;

    private static DefaultHttpClient httpClient = new DefaultHttpClient();

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param wxCode 调用微信登陆返回的Code
     * @return
     */
    public JSONObject getAppletOpenId(String wxCode) {
        //微信端登录code值
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + payWxConfig.getAppletAppid() + "&secret=" + payWxConfig.getAppletAppSecret() + "&js_code=" + wxCode + "&grant_type=authorization_code";  //请求地址
        log.info("requestUrl: " + requestUrl);
        HttpGet getMethod = HttpClientConnectionManager.getGetMethod(requestUrl);
        HttpResponse hresponse;
        JSONObject json = null;
        try {
            hresponse = httpClient.execute(getMethod);
            HttpEntity entity = hresponse.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            json = JSONObject.fromObject(content);
            log.info("*********************" + json);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;

    }

    /**
     * 获取 access_token
     * @return
     */
    public String getAccessToken(){

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ payWxConfig.getAppletAppid() +"&secret="+ payWxConfig.getAppletAppSecret() ;
        //log.info("requestUrl: " + requestUrl);
        HttpGet getMethod = HttpClientConnectionManager.getGetMethod(requestUrl);
        HttpResponse hresponse;
        JSONObject json = null;
        try {
            hresponse = httpClient.execute(getMethod);
            HttpEntity entity = hresponse.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            json = JSONObject.fromObject(content);
            log.info("+++++++++++++************" + json);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String access_token=json.getString("access_token");
        return access_token;
    }

    /**
     * 获取小程序二维码
     * @return
     */
    public Map getUnlimited(String access_token, String scene, String page, Integer width) throws IOException {

        Map map = new HashMap();

        BASE64Encoder encode = new BASE64Encoder();
        String requestUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+access_token;
        //String json = "{\"scene\":\"name=jerry\",\"width\":300}";
        Map<String, Object> params1 = new HashMap<String,Object>();
        params1.put("scene", scene);  //参数  这个参数是自己来识别的  可以随意  但我们写的程序他是唯一识别
        params1.put("page", page); //位置
        params1.put("width", width);
        //log.info("------------------------------OLD:params1: " + params1);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(requestUrl);

        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(params1);
        System.err.println(body);//必须是json模式的 post
        StringEntity entity;
        entity = new StringEntity(body);
        entity.setContentType("image/png");

        httpPost.setEntity(entity);
        HttpResponse response;

        response = httpClient.execute(httpPost);
        InputStream inputStream = response.getEntity().getContent();
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        try {
            while ((ch = inputStream.read()) != -1) {
                bytestream.write(ch);
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] program = bytestream.toByteArray();

        InputStream buffin = new ByteArrayInputStream(program);

        BufferedImage img = ImageIO.read(buffin);

        String encode1 = com.xiaoleilu.hutool.lang.Base64.encode(program);
        BASE64Encoder encoder = new BASE64Encoder();
        String  binary = encoder.encodeBuffer(program).trim();
        map.put("jsonData", encode1);
        map.put("jsonData1", binary);
        map.put("program", program);
        map.put("img", img);


        return map;
    }



}
