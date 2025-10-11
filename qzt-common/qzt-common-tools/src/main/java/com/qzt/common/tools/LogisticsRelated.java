package com.qzt.common.tools;//package trackingMore;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 物流信息相关工具类
 *  trackingmore
 * @author Xiaofei
 * @date 2019-10-22
 */
public class LogisticsRelated {

    private final String API_KEY = "d7b282ba-edc4-48c7-bc42-d322726fb381";
    //private String API_KEY="14716a2f-a1f9-4dd6-9e0d-ba5f483024d0";

    /**
     * 获取物流信息
     *
     * @param trackingNumber 快递单号
     * @param carrierCode    快递code
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-10-22
     */
    public static Map selectCode(String trackingNumber, String carrierCode) {
        Map remap = new HashMap();
        remap.put("code", "0201");
        remap.put("message", "包裹目前查询不到");
        try {
            String urlStr = "/trackings/realtime";//获取单个运单号实时物流信息
            String requestData = "{\"tracking_number\": \"" + trackingNumber + "\",\"carrier_code\":\"" + carrierCode + "\",\"destination_code\":\"US\"," +
                    "\"tracking_ship_date\": \"20180226\",\"tracking_postal_code\":\"13ES20\",\"specialNumberDestination\":\"US\",\"order\":\"#123123\"," +
                    "\"order_create_time\":\"2018/3/27 16:51\",\"lang\":\"en\"}";
            String result = new LogisticsRelated().orderOnlineByJson(requestData, urlStr, "realtime");
            Map resultmap = JSONObject.parseObject(result);
            System.out.println("物流接口信息：" + resultmap);
            //判断三方接口返回状态
            Map metamap = JSONObject.parseObject(resultmap.get("meta").toString());
            if (metamap.get("code") != null && "200".equals(metamap.get("code").toString())) {
                Map datamap = JSONObject.parseObject(resultmap.get("data").toString());
                List itemslist = (List) datamap.get("items");
                if (itemslist != null && itemslist.size() > 0) {
                    Map item = JSONObject.parseObject(itemslist.get(0).toString());//物流信息
                    Map origin_info = JSONObject.parseObject(item.get("origin_info").toString());//发件国信息
                    if (origin_info.get("trackinfo") != null && !"".equals(item.get("lastUpdateTime").toString())) {
                        Map redatamap = new HashMap();
                        redatamap.put("lastEvent", item.get("lastEvent"));//最新一条物流信息
                        redatamap.put("lastUpdateTime", item.get("lastUpdateTime"));//最新一条物流信息扫描时间
                        redatamap.put("status", item.get("status"));//包裹物流状态定义
                        List<Map> trackinfos = new ArrayList();
                        List trackinfolist = (List) origin_info.get("trackinfo");
                        for (int i = 0; i < trackinfolist.size(); i++) {
                            Map map = new HashMap();
                            Map track = JSONObject.parseObject(trackinfolist.get(i).toString());//发件国信息
                            map.put("checkpointStatus", track.get("checkpoint_status"));//每一条物流包裹状态
                            //map.put("details", track.get("Details"));//每一条物流信息扫描地点
                            map.put("statusDescription", track.get("StatusDescription"));//每一条物流信息内容
                            map.put("scanningTime", track.get("Date"));//每一条物流信息扫描时间
                            trackinfos.add(map);
                        }
                        redatamap.put("trackinfo", trackinfos);//详细物流信息
                        remap.put("code", "200");
                        remap.put("data", redatamap);
                        remap.put("message", "查询成功");
                    }
                }
            } else {
                remap.put("code", metamap.get("code"));
                remap.put("message", metamap.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remap;
    }

    public static void main(String[] args) {
//        Map map = selectCode("75305328329846", "zto");
        /*Long t = new Date().getTime();
        Map map = selectCode("12321", "sto");
        Long tt = new Date().getTime();
        System.out.println(map);
        System.out.println("耗时：" + ((tt - t) / 1000) + "S");*/
    }


    public String orderOnlineByJson(String requestData, String urlStr, String type) throws Exception {
        //---headerParams
        Map<String, String> headerparams = new HashMap<String, String>();
        headerparams.put("Trackingmore-Api-Key", API_KEY);
        headerparams.put("Content-Type", "application/json");
        //---bodyParams
        List<String> bodyParams = new ArrayList<String>();
        String result = null;
        if (type.equals("post")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/post";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("get")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/get";
            String RelUrl = ReqURL + urlStr;
            result = sendPost(RelUrl, headerparams, bodyParams, "GET");
        } else if (type.equals("batch")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/batch";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("codeNumberGet")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            String RelUrl = ReqURL + urlStr;
            result = sendGet(RelUrl, headerparams, "GET");
        } else if (type.equals("codeNumberPut")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            bodyParams.add(requestData);
            String RelUrl = ReqURL + urlStr;
            result = sendPost(RelUrl, headerparams, bodyParams, "PUT");
        } else if (type.equals("codeNumberDelete")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            String RelUrl = ReqURL + urlStr;
            result = sendGet(RelUrl, headerparams, "DELETE");
        } else if (type.equals("realtime")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/realtime";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("carriers")) {
            String ReqURL = "http://api.trackingmore.com/v2/carriers";
            result = sendGet(ReqURL, headerparams, "GET");
        } else if (type.equals("carriers/detect")) {
            String ReqURL = "http://api.trackingmore.com/v2/carriers/detect";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("update")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/update";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("getuserinfo")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/getuserinfo";
            result = sendGet(ReqURL, headerparams, "GET");
        } else if (type.equals("getstatusnumber")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/getstatusnumber";
            result = sendGet(ReqURL, headerparams, "GET");
        } else if (type.equals("notupdate")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/notupdate";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("remote")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/remote";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("costtime")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/costtime";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("delete")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/delete";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("updatemore")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/updatemore";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        }
        return result;
    }


    private String sendPost(String url, Map<String, String> headerParams, List<String> bodyParams, String mothod) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(mothod);
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            StringBuffer sbBody = new StringBuffer();
            for (String str : bodyParams) {
                sbBody.append(str);
            }
            out.write(sbBody.toString());
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String sendGet(String url, Map<String, String> headerParams, String mothod) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod(mothod);
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}