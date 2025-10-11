package com.qzt.common.tools;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;



public class QRCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;


    /**
     * 生成二维码
     * @param content	源内容
     * @param imgPath	生成二维码保存的路径
     * @param needCompress	是否要压缩
     * @return		返回二维码图片
     * @throws Exception
     */
    public static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 在生成的二维码中插入图片
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file1 = new File(imgPath);
        if (!file1.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成带logo二维码，并保存到磁盘
     * @param content
     * @param imgPath	logo图片
     * @param destPath
     * @param needCompress
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress,String file) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);

        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir。(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String imgPath, String destPath,String file) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false ,file);
    }

    public static void encode(String content, String destPath, boolean needCompress,String file) throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress,file);
    }

    public static void encode(String content, String destPath,String file) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false,file);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }


    /**
     * 从二维码中，解析数据
     * @param file	二维码图片文件
     * @return	 返回从二维码中解析到的数据值
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

/*=================================生成邀请卡========================================*/

    /**
     * 图片转字节流
     * @param bImage
     * @param format
     * @return
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private static Font font = null;
    private static Graphics2D g = null;

    /**
     * 图片上拼接文字
     * @param img
     * @param content
     * @param x
     * @param y
     * @param color
     * @return
     */
    public static BufferedImage modifyImage(BufferedImage img, Object content, int x, int y, Color color) {
        try {
            g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.setColor(color);// 设置字体颜色
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// 抗锯齿
            if (font != null)
                g.setFont(font);
            if (content != null) {
                String str = content.toString();
                g.drawString(str, x, y);
            }
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void setFont(String name, int style, int fontSize) {
        font = new Font(name, style, fontSize);
    }

    public static BufferedImage modifyImagetogeter(BufferedImage addImage, BufferedImage sourceImg, int x,
                                            int y) {
        int width = addImage.getWidth();
        int height = addImage.getHeight();
        try {
            g = sourceImg.createGraphics();
            g.drawImage(addImage, x, y, width, height, null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceImg;
    }

    /**
     * 获取网络图片流
     *
     * @param url
     * @return
     */
    public InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
     *
     * @param inputImage
     * @param newWidth ：压缩后宽度
     * @param newHeight ：压缩后高度
     * @throws java.io.IOException
     *             return
     */
    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight) throws Exception {
        // 获取原始图像透明度类型
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        // 开启抗锯齿
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 使用高质量压缩
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHints(renderingHints);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;

    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     *
     * @param bi1
     *            用户头像
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage bi1) throws IOException {

//		BufferedImage bi1 = ImageIO.read(new File(url));

        // 这种是黑色底的
//		BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }



}
