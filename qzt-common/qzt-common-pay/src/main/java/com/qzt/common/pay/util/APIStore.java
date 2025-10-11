package com.qzt.common.pay.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//用户请使用UTF-8作为源码文件的保存格式，避免出现乱码问题
@Component
public class APIStore {
    /**
     * HTTP的Post请求方式
     * @param strUrl 访问地址
     * @param param 参数字符串
     * */
    public static String doPost(String strUrl, String param) {
        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;

        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // post方式
            httpURLConnection.connect();
            //System.out.println("ResponseCode:" + httpURLConnection.getResponseCode());
            //POST方法时使用
            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


    //将map型转为请求参数型
    public static String urlencode(Map<String,Object> data) {
        StringBuilder apistore = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                apistore.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return apistore.toString();
    }

    // 发起请求,获取内容
    public Boolean  selectMember(String name,String code) {
//        //请求地址
//        String url="https://v.apistore.cn/api/a1";
//        //您申请的key
//        String APPKEY = "3ebb683ac798260165928cd163aa5f51";
//        //请求参数
//        Map params = new HashMap();
//        params.put("key",APPKEY);
//        params.put("cardNo",code);
//        params.put("realName",name);
//        params.put("information",1);
//        String result = doPost(url, urlencode(params));
//        JSONObject object = JSONObject.fromObject(result);
//        if (object.getInt("error_code") == 0){
//            return true;
//        }else{
//            return false;
//        }
        String host = "https://idenauthen.market.alicloudapi.com";
        String path = "/idenAuthentication";
        String method = "POST";
        String appcode = "d66890457ddc4c9b9aec0c568fa7477e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("idNo", code);
        bodys.put("name", name);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtilsAli.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            //获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));
            // 转为 Json 对象
            JSONObject json = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
            //获取到 respCode 代码
            String respCode = String.valueOf(json.get("respCode"));

            if (respCode.equals("0000")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}