package com.qzt.common.core.util;

import com.xiaoleilu.hutool.date.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by cgw on 2018/5/30.
 */
public final class RandCodeUtil {
    /**
     * 获取随机字符串
     *
     * @return
     */
    public static String initOrderCode() {
        // ---------------生成订单号 开始------------------------
        // 当前时间 yyyyMMddHHmmss
        String currTime = new DateTime().toString("yyyyMMddHHmmss");
        // 8位日期
        String strTime = currTime.substring(0, 12);
        // 四位随机数
        String strRandom = buildRandom(10) + "";
        // 10位序列号,可以自行调整。
        String strReq = strTime + strRandom;
        // 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
        return strReq;
        // ---------------生成订单号 结束------------------------
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 生成tokenId 随机生成字符串sum 位字符串 + 时间戳
     *
     * @param sum
     * @param salt 盐
     * @return
     */
    public static String getRandomStrTokenId(final int sum, String salt) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sum; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString() + salt + getCurrTime();
    }

    /**
     * <B>功能简述</B><br>
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return
     * @date 2015年6月10日 上午10:13:46
     * @author DaiLu
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 6位不重复随机数
     */
    public static String generateShortUuid(int num) {
        String[] chars = new String[]{/*"a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1",*/ "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", /*"I",*/
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < num; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            //shortBuffer.append(chars[x % 0x3E]);0x3E=62
            shortBuffer.append(chars[x % 33]);
        }
        return shortBuffer.toString();
    }

}
