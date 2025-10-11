package com.qzt.common.tools;

import com.alibaba.fastjson.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 高德地图相关工具类
 *
 * @author Xiaofei
 * @date 2019-09-25
 */
public class AutonaviUtil {

    private static final String URL_ = "https://restapi.amap.com/v3/geocode/regeo";
    private static final String KEY_ = "99f32a55c593403267b6a24db36f9c0e";//高德key
    private static final String POITYPE_ = "190100|190101|190102|190103|190104|190105|190106";//附近POI类型

    /**
     * 根据经纬度坐标获取地址信息
     *
     * @param location 纬度坐标
     * @return Map
     * @author Xiaofei
     * @date 2019-09-25
     */
    public static Map getAddressByLongitudeAndLatitude(String location) {
        Map remap = new HashMap();
        String urls = URL_ + "?key=" + KEY_ + "&location=" + location + "&poitype=" + POITYPE_ + "&radius=0&extensions=base&batch=false&roadlevel=0&output=JSON";
        try {
            URL url = new URL(urls);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String res = "";
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
            Map jsonObject = (Map) JSONObject.parse(res);
            if (jsonObject != null && "1".equals(jsonObject.get("status").toString())) {//status状态值 0 表示请求失败；1 表示请求成功
                Object regeocodeobj = jsonObject.get("regeocode");
                if (regeocodeobj != null) {
                    Map regeocodemap = (Map) JSONObject.parse(regeocodeobj.toString());
                    remap.put("formattedAddress", regeocodemap.get("formatted_address") == null ? "" : regeocodemap.get("formatted_address"));//结构化地址信息
                    if (regeocodemap.get("addressComponent") != null) {
                        Map addressComponentmap = (Map) JSONObject.parse(regeocodemap.get("addressComponent").toString());//地址元素列表
                        remap.put("country", infoDispose(addressComponentmap.get("country")));//国家 国内地址默认返回中国
                        remap.put("province", infoDispose(addressComponentmap.get("province")));//坐标点所在省名称
                        remap.put("city", infoDispose(addressComponentmap.get("city")));//坐标点所在城市名称
                        remap.put("citycode", infoDispose(addressComponentmap.get("citycode")));//坐标点所在城市编码
                        remap.put("district", infoDispose(addressComponentmap.get("district")));//坐标点所在区
                        remap.put("adcode", infoDispose(addressComponentmap.get("adcode")));//坐标点所在行政区编码
                        remap.put("township", infoDispose(addressComponentmap.get("township")));//坐标点所在乡镇/街道（此街道为社区街道，不是道路信息）
                        remap.put("towncode", infoDispose(addressComponentmap.get("towncode")));//乡镇街道编码
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remap;
    }

    private static String infoDispose(Object object) {
        return object == null || "[]".equals(object.toString()) ? "" : object.toString();
    }

}
