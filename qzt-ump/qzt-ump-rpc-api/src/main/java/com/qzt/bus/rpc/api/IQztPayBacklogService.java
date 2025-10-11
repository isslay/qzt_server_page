package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztPayBacklog;
import com.qzt.common.core.base.BaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztPayBacklogService extends BaseService<QztPayBacklog> {

    Page<QztPayBacklog> find(Page<QztPayBacklog> pageModel);

    /**
     * 添加支付回调日志
     *
     * @param backSource 回调来源（支付宝z、微信w）
     * @param payNo      支付单号
     * @param backPayNo  第三方支付单号
     * @param payMoney   支付金额
     * @param message    报文
     * @return void
     * @author Xiaofei
     * @date 2019-11-12
     */
    void addPayBacklog(String backSource, String payNo, String backPayNo, String payMoney, String message);

    /**
     * 查询订单回调日志
     *
     * @param busType
     * @param orderNo
     * @return java.util.List<com.qzt.bus.model.QztPayBacklog>
     * @author Xiaofei
     * @date 2019-11-23
     */
    List<QztPayBacklog> findByOrderNoType(String busType, String orderNo);

}
