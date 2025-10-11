package com.qzt.common.tools;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 * 金钱处理
 * </p>
 *
 * @author Xiaofei
 * @since 2019-05-15
 */
public class BigDecimalUtil {

    /**
     * 小数点精确N位
     * 四舍五入
     *
     * @param money
     * @param places N
     * @return
     */
    public static BigDecimal exactDecimalPointf(BigDecimal money, Integer places) {
        return money == null ? BigDecimal.ZERO : money.setScale(places, ROUND_HALF_UP);
    }

    /**
     * 小数点精确N位
     * 向上取整
     *
     * @param money
     * @param places N
     * @return
     */
    public static BigDecimal exactDecimalPointUp(BigDecimal money, Integer places) {
        return money == null ? BigDecimal.ZERO : money.setScale(places, BigDecimal.ROUND_UP);
    }


    /**
     * 精确余额小数位2位，多余截断
     *
     * @param money
     * @return
     */
    public static BigDecimal balancePrecision(BigDecimal money) {
        return money == null ? BigDecimal.ZERO : money.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 精确宝石花小数位2位，多余截断
     *
     * @param money
     * @return
     */
    public static BigDecimal gemPrecision(BigDecimal money) {
        return money == null ? BigDecimal.ZERO : money.setScale(2, BigDecimal.ROUND_DOWN);
    }


    /**
     * 计算RMB保留小数位2位，四舍五入
     *
     * @param money
     * @return
     */
    public static BigDecimal countRmb(BigDecimal money) {
        return money == null ? BigDecimal.ZERO : money.setScale(2, ROUND_HALF_UP);
    }

    /**
     * 宝石花换算处理四舍五入2位
     *
     * @param money
     * @return
     */
    public static BigDecimal gemConversionProcess(BigDecimal money, BigDecimal dmoney) {
        BigDecimal zero = BigDecimal.ZERO;
        money = money == null ? zero : money;
        dmoney = dmoney == null ? zero : dmoney;
        if (dmoney.compareTo(zero) == -1) {
            return zero;
        }
        return money.divide(dmoney, 2, ROUND_HALF_UP);//换算后四舍五入2位
    }

    /**
     * Long金额 * Long百分比（为整数）
     *
     * @param money      Long金额
     * @param percentage Long百分比
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-11-24
     */
    public static Long longPercentage(Long money, Long percentage) {
        BigDecimal bze = BigDecimal.valueOf(100);
        BigDecimal bpercentage = percentage == null || percentage < 0 ? BigDecimal.ZERO : BigDecimal.valueOf(percentage).divide(bze);//比例%
        BigDecimal bmoney = money == null || money < 0 ? BigDecimal.ZERO : BigDecimal.valueOf(money).divide(bze);//金额%
        bmoney = bmoney.multiply(bpercentage).multiply(bze).setScale(0, BigDecimal.ROUND_DOWN);
        return Long.valueOf(bmoney.toString());
    }

    /**
     * BigDecimal算法
     *
     * @param param
     * @param param1
     * @param type   ( 1.加法 2.减法 3.乘法 4.除法 5.绝对值(只取第一个参数) )
     * @return
     */
    public static BigDecimal operation(Object param, Object param1, int type) {
        BigDecimal result = null;
        if (type == 1) {
            result = new BigDecimal(param + "").add(new BigDecimal(param1 + ""));
        }
        if (type == 2) {
            result = new BigDecimal(param + "").subtract(new BigDecimal(param1 + ""));
        }
        if (type == 3) {
            result = new BigDecimal(param + "").multiply(new BigDecimal(param1 + ""));
        }
        if (type == 4) {
            result = new BigDecimal(param + "").divide(new BigDecimal(param1 + ""));
        }
        if (type == 5) {
            result = new BigDecimal(param + "").abs();
        }
        return result;
    }

}
