package com.qzt.ump.rpc.service.impl;

import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.rpc.api.IQztApplyBusorderService;
import com.qzt.bus.rpc.api.IQztGorderService;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.ump.rpc.api.ProcessingPayOrderService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 三方支付订单相关处理实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-18
 */
@Log4j
@Service("processingPayOrder")
public class ProcessingPayOrderServiceImpl implements ProcessingPayOrderService {

    @Autowired
    private IQztApplyBusorderService qztApplyBusorderService;

    @Autowired
    private IQztGorderService qztGorderService;

    @Override
    public Map<String, String> getOrderPayAmount(String orderNo, String orderType) {
        Map<String, String> remap = new HashMap<>();
        if (StringUtil.isEmpty(orderNo) || StringUtil.isEmpty(orderType)) {
            remap = null;
        }
        String outTradeNo = ""; //请保证OutTradeNo值每次保证唯一
        String totalAmount = "";//订单三方支付金额

        if (OrderUtil.OrderNoEnum.BUSORDER.value().equals(orderType)) {//申请服务站订单
            QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderService.queryByOrederNo(orderNo);
            outTradeNo = orderNo;
            totalAmount = PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getThreeSidesMoney());
        } else if (OrderUtil.OrderNoEnum.GOODS.value().equals(orderType)) {//商品订单
            QztGorder qztGorder = this.qztGorderService.queryByOrederNo(orderNo);
            outTradeNo = orderNo;
            totalAmount = PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getActuaPayment());
        }
        remap.put("outTradeNo", outTradeNo);
        remap.put("totalAmount", totalAmount);
        if ("".equals(outTradeNo) || "".equals(totalAmount)) {
            remap = null;
        }
        return remap;
    }

    @Override
    public void payBackDispose(String orderNo, String tradeNo) {
        try {
            String orderNoHead = orderNo.substring(0, 2);
            if (OrderUtil.OrderNoEnum.BUSORDER.value().equals(orderNoHead)) {//服务站申请订单
                this.qztApplyBusorderService.applyOrderPayBack(orderNo);
            } else if (OrderUtil.OrderNoEnum.GOODS.value().equals(orderNoHead)) {//商品订单
                this.qztGorderService.orderPayBack(orderNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }


}
