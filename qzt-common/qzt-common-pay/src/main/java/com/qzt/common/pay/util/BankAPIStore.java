package com.qzt.common.pay.util;

import com.xiaoleilu.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

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
public class BankAPIStore {

    public static String getBank(String code)throws Exception{
        //银行代码请求接口 url
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo="+code+"&cardBinCheck=true";
        //发送请求，得到 josn 类型的字符串
        String result = HttpUtil.get(url);
        // 转为 Json 对象
        JSONObject json = JSONObject.fromObject(result);
        //获取到 bank 代码
        String bank = String.valueOf(json.get("bank"));
        return bank;
    }

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
    public static String urlencode(Map<String,Object>data) {
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
    public static int isMemberAndCode(String realName,String code,String userCode) {
        //请求地址
        String url="https://v.apistore.cn/api/bank/v2";
        //您申请的key
        String APPKEY = "27b8e2665a5abfeda2bd757f9c5bdd7b";
        //请求参数
        Map params = new HashMap();
        params.put("key",APPKEY);
        params.put("bankcard",code);
        params.put("realName",realName);
        params.put("cardNo",userCode);
        params.put("cardtype","DC");
        params.put("information","1");

        String result = doPost(url, urlencode(params));
        //输出结果
        System.out.println(result);
        //JSON
        JSONObject object = JSONObject.fromObject(result);
        if (object.getInt("error_code") == 0){
            return 0;
        }else{
            return object.getInt("error_code");
        }

    }

    public static JSONObject isCheckoutBank(String realName,String cardNo,String idCard,String mobile) {
        String host = "https://yunyidata.market.alicloudapi.com";
        String path = "/bankAuthenticate4";
        String method = "POST";
        String appcode = "d66890457ddc4c9b9aec0c568fa7477e";
        JSONObject json  = null;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("ReturnBankInfo", "YES");
        bodys.put("cardNo", cardNo);
        bodys.put("idNo", idCard);
        bodys.put("name", realName);
        bodys.put("phoneNo", mobile);


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
            System.out.println(response.toString());
            //获取response的body
            json  = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  json;
    }



}