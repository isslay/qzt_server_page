package com.qzt.common.pay.util;

import com.qzt.common.tools.Sha1;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;


@SuppressWarnings("deprecation")
public class WeixinUtil {


    /**
     * 进行sha1签名
     *
     * @param params
     * @return
     */
    public static String sha1Sign(Map params) {
        String url = createLinkString(params);
        String sign = Sha1.encode(url);
        return sign;
    }


    /**
     * 生成签名
     *
     * @param params 参数map
     * @param key    支付key(API密钥)
     * @return
     */
    public static String createSign(Map params, String key) {
        String url = createLinkString(params);
        url += "&key=" + key;
        System.out.println("生成签名url：" + url);
        String sign = "";
        try {
            sign = DigestUtils.md5Hex(url.getBytes("UTF-8")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 根据Map参与拼接URL
     *
     * @param params
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if ("sign".equals(key)) {
                continue;
            } else if ("".equals(prestr)) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + "&" + key + "=" + value;
            }
        }
        return prestr;
    }

    public static void main(String[] args) {
    }

    public static String mapToXml(Map map) {
        Document document = DocumentHelper.createDocument();

        Element nodeElement = document.addElement("xml");
        for (Object obj : map.keySet()) {
            Element keyElement = nodeElement.addElement(String.valueOf(obj));
            String text = String.valueOf(map.get(obj));
            if (!"total_fee".equals(obj)) {
                keyElement.addCDATA(text);
            } else {
                keyElement.setText(text);
            }

        }
        return doc2String(document);
    }

    public static String doc2String(Document document) {
        String s = "";
        try {
            // 使用输出流来进行转化
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用UTF-8编码

            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            s = out.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

    public static Document post(String wsPart, String doc_str) {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(wsPart);
            StringEntity requestEntity = new StringEntity(doc_str, "UTF-8");

            httppost.setEntity(requestEntity);
            httppost.setHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
            HttpResponse httpresponse = httpclient.execute(httppost);
            HttpEntity rentity = httpresponse.getEntity();
            InputStream in = rentity.getContent();
            // String res= StringUtil.inputStream2String(in);
            // System.out.println(res);
            if (in != null) {
                SAXReader reader = new SAXReader();
                Document doc = reader.read(in);
                return doc;
            } else {
                throw new RuntimeException("post uri[" + wsPart
                        + "]发生错误:[stream 返回Null]");
            }
        } catch (Throwable e) {

            throw new RuntimeException("post uri [" + wsPart + "]发生错误", e);

        }
    }

    public static Map xmlToMap(Document document) {
        Map map = new HashMap();

        Element rootEl = document.getRootElement();
        List<Element> elList = rootEl.elements();
        for (Element el : elList) {
            String name = el.getName();
            String text = el.getText();
            map.put(name, text);
        }
        return map;
    }

}
