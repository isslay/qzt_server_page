package com.qzt.common.tools;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.util.RandCodeUtil;
import com.qzt.common.redis.util.CacheUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * 订单处理相关工具类
 *
 * @author Xiaofei
 * @date 2019-09-27
 */
public final class OrderUtil {

    /**
     * 生成核销码
     *
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-12-19
     */
    public static String generateChargeOff() {
        Date date = new Date();
        String ytd = DateTime.getCurDateTime(date, "yyMMdd");
        String time = date.getTime() + "";
        Long chargeOffCode = CacheUtil.getCache().incr(SysConstant.CacheNamespaceEnum.CHARGEOFFNUM.value() + ytd, 1);
        //年月日 + time后4位  + redis计数  + 2位随机数  191219111100156
        return ytd + time.substring(time.length() - 4) + FuncUtils.formateOrderNo(chargeOffCode, 3) + RandCodeUtil.buildRandom(2);
    }

    /**
     * 生成订单编号
     *
     * @param theOrderId 订单编号开头字母
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-09-27
     */
    public static String generateOrderNo(String theOrderId) {
        //订单编号
        String formateDate = DateTime.getCurDateTime(new Date(), "yyyyMMdd");
        Long orderNo = CacheUtil.getCache().incr(SysConstant.CacheNamespaceEnum.ORDERNUM.value() + formateDate, 1);
        return theOrderId + formateDate + FuncUtils.formateOrderNo(orderNo, 5);
    }

    /**
     * 订单编号前缀枚举
     *
     * @author Xiaofei
     * @return
     * @date 2019-11-18
     */
    public enum OrderNoEnum {
        BUSORDER("SA", "服务站申请订单", 1),
        GOODS("GS", "商品订单", 1),
        SERVER("SO", "服务订单", 1),
        APPLYTRY("SY", "试用订单", 1),
        USER("USER", "注册用户", 1);

        /**
         * 值
         */
        private String value;
        /**
         * 描述
         */
        private String message;
        private int key;

        OrderNoEnum(String value, String message, int key) {
            this.value = value;
            this.message = message;
            this.key = key;
        }

        public String value() {
            return this.value;
        }

        public String getMessage() {
            return this.message;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public static String getMessageByValue(String values) {
            String messag = "";
            OrderNoEnum[] businessModeEnums = values();
            for (OrderNoEnum businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value().equals(values)) {
                    messag = businessModeEnum.message;
                }
            }
            return messag;
        }

    }


}
