package com.qzt.common.tools;

import javafx.scene.text.FontSmoothingType;

import java.math.BigDecimal;

/**
 * 价格处理工具类
 *
 * @author Xiaofei
 * @return
 * @date 2019-11-12
 */
public final class PriceUtil {

    /**
     * 金额格式.00 分转元
     *
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-11-12
     */
    public static String exactlyTwoDecimalPlaces(Long money) {
        String result = "0.00";
        if (money != null && money > 0) {
            BigDecimal moneyb = new BigDecimal(money);
            BigDecimal resultb = moneyb.divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_DOWN);
            result = resultb.toString();
        }
        return result;
    }

    /**
     * 金额格式.00 分转元 返回小数
     *
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-11-12
     */
    public static BigDecimal moneyLongToDecimal(Long money) {
        BigDecimal result = BigDecimal.ZERO;
        if (money != null && money > 0) {
            BigDecimal moneyb = new BigDecimal(money);
            result = moneyb.divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_DOWN);
        }
        return result;
    }


    public static void main(String[] agrs) {

    }

}
