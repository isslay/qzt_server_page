package com.qzt.common.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgUtil {

    /**
     * 获得图片大小
     */
    public static BufferedImage getBufferedImage(String imgurl) {
        URL url = null;
        InputStream is = null;
        BufferedImage img = null;
        try {
            url = new URL(imgurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 设置请求头信息
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            con.setRequestProperty("Connection", "keep-alive");
            is = con.getInputStream();
            img = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            img = null;
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}
