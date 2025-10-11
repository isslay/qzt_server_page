package com.qzt.common.pay.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class WxPayBackTools {


    /**
     * 微信支付下单
     *
     * @param cno         客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)
     * @param orderNo     订单编号
     * @param payMoney    支付金额
     * @param appid       应用ID
     * @param mch_id      商户号
     * @param sign        签名
     * @param openid      openid
     * @param time_expire 交易结束时间
     * @param notify_url  回调地址
     * @param request     request
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-06-18
     */
    public Map getWxMaptoXml(String cno, String orderNo, Double payMoney, String appid, String mch_id, String sign, String openid, int time_expire, String notify_url, HttpServletRequest request) throws Exception {
        Map params = new TreeMap();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        String randStr = this.getRandStr(10);//随机字符串
        params.put("nonce_str", randStr);
        params.put("body", "专孝堂订单[" + orderNo + "]");
        params.put("out_trade_no", orderNo);//商户订单号
        params.put("total_fee", this.toFen(payMoney));//标价金额(支付金额)
        params.put("spbill_create_ip", request.getRemoteAddr());//终端IP
        params.put("notify_url", notify_url);//回调地址
//        params.put("attach", userId);//附加数据userId
        if (time_expire > 0) {//计算订单交易结束时间
            Date d = new Date();
            Date time2 = new Date(d.getTime() + 1000 * 60 * time_expire);
            params.put("time_expire", new SimpleDateFormat("yyyyMMddHHmmss").format(time2));
        }
        if ("1".equals(cno)) {//PC
            params.put("trade_type", "NATIVE");
            params.put("product_id", "pc_" + orderNo);
        } else if ("2".equals(cno) || "3".equals(cno)) {//APP
            params.put("trade_type", "APP");
        } else if ("4".equals(cno)) {//H5
            params.put("trade_type", "MWEB");
            params.put("openid", openid);
        } else if ("5".equals(cno)) {//小程序
            params.put("trade_type", "JSAPI");
            params.put("openid", openid);
        }
        String wxsign = WeixinUtil.createSign(params, sign);
        log.info(params.toString());
        params.put("sign", wxsign);
        String xml = WeixinUtil.mapToXml(params);//
        Document resultDoc = WeixinUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        Element rootEl = resultDoc.getRootElement();
        String return_code = rootEl.element("return_code").getText(); // 返回码
        Map retmap = new TreeMap();//自动排序-有序map
        if ("SUCCESS".equals(return_code)) {
            String result_code = rootEl.element("result_code").getText();
//            String result_sign = rootEl.element("sign").getText();// 业务码
            if ("SUCCESS".equals(result_code)) {
                log.info("SUCCESS____WX____PAY____SIGN");
                String prepay_id = rootEl.element("prepay_id").getText(); // 预支付订单id
                String time = String.valueOf(System.currentTimeMillis() / 1000);//根据System.currentTimeMillis获取当前时间
                String signk = "";

                if ("1".equals(cno) || "4".equals(cno) || "5".equals(cno)) {//PC、小程序、H5
                    retmap.put("appId", appid);//应用ID
                    retmap.put("nonceStr", randStr);
                    retmap.put("package", "prepay_id=" + prepay_id);
                    retmap.put("signType", "MD5");
                    retmap.put("timeStamp", time);//时间戳
                    signk = WeixinUtil.createSign(retmap, sign);
                    if ("1".equals(cno)) {//PC
                        retmap.put("codeUrl", rootEl.element("code_url").getText());
                    }
                } else if ("2".equals(cno) || "3".equals(cno)) {//IOS、Android
                    retmap.put("appid", appid);//应用ID
                    retmap.put("partnerid", mch_id);//商户号
                    retmap.put("prepayid", prepay_id);//预支付交易会话ID
                    retmap.put("noncestr", randStr);//随机字符串
                    retmap.put("timestamp", time);//时间戳
                    retmap.put("package", "Sign=WXPay");//扩展字段
                    signk = WeixinUtil.createSign(retmap, sign);
                }
                retmap.put("paySign", signk);//加密后签名
                retmap.put("orderNo", orderNo);
            } else {
                throw new Exception("result_code非SUCCESS，支付出现意外");
            }
        } else {
            if ("FAIL".equals(return_code)) {
                String return_msg = resultDoc.getRootElement().element("return_msg").getText(); // 错误信息
                log.info("微信端返回错误" + return_code + "[" + return_msg + "]");
                log.info("-----------post xml-----------");
                log.info(xml);
                throw new Exception("微信支付出现意外");
            }
        }
        return retmap;
    }

    /**
     * 回调处理
     *
     * @param params 回调返回参数结果集
     * @param sign   商户秘钥
     * @return java.util.Map  code 0201回调参数return_code失败、0202验签失败
     * @author Xiaofei
     * @date 2019/6/18
     */
    public Map backTools(Map<String, String> params, String sign) throws IOException, DocumentException {
//        SAXReader saxReadr = new SAXReader();
        Map remap = new HashMap();
//        Document document = saxReadr.read(request.getInputStream());
//        Map<String, String> params = WeixinUtil.xmlToMap(document);
        String return_code = params.get("return_code");
        String result_code = params.get("result_code");
        //String payType = params.get("trade_type");//交易类型JSAPI	JSAPI、NATIVE、APP
        String wxsign = "";
        wxsign = WeixinUtil.createSign(params, sign);//生成签名
        if (wxsign.equals(params.get("sign"))) {//验证sign
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                log.info("签名校验成功");
                remap.put("busId", params.get("out_trade_no"));
                remap.put("tradeId", params.get("transaction_id"));
                remap.put("param", params.toString());
                remap.put("attach", params.get("attach"));
            } else {
                remap.put("code", "0201");
            }
        } else {
            remap.put("code", "0202");
        }
        return remap;
    }

    /**
     * 获取用户openid
     *
     * @param url
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-06-20
     */
    public String getOpenId(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        String openid = "";
        HttpGet httpget = new HttpGet(url);
        Map<String, String> map = new HashMap<String, String>();
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            JSONObject json = JSONObject.fromObject(EntityUtils.toString(entity, "utf-8"));
            openid = json.getString("openid");
            // String access_token = json.getString("access_token");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return openid;
    }


    //    微信支付通知微信

    public void writeMessageToResponse(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }


    /**
     * Map转xml数据
     */
    public static String GetMapToXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[SUCCESS]]></return_code>");
        sb.append("<return_msg><![CDATA[OK]]></return_msg>");
        sb.append("</xml>");
        return sb.toString();
    }

    public static String getRandStr(int n) {
        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

    private static String toFen(Double money) {
        String str = String.valueOf(mul(money, 100));
        return str;
    }

    public static Long mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).longValue();
    }

}
