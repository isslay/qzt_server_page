package com.qzt.ump.rpc.api;

import java.util.Map;

/**
 * 三方支付订单相关处理
 *
 * @author Xiaofei
 * @return
 * @date 2019-11-18
 */
public interface ProcessingPayOrderService {

    /**
     * 三方支付金额处理
     *
     * @param orderNo   订单编号
     * @param orderType 订单类型 （申请服务站01、商品订单03）
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    Map<String, String> getOrderPayAmount(String orderNo, String orderType);

    /**
     * 三方支付回调方法处理
     *
     * @param orderNo 三方回调的本平台订单编号
     * @param tradeNo 三方平台订单编号
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-18
     */
    void payBackDispose(String orderNo, String tradeNo);

}
