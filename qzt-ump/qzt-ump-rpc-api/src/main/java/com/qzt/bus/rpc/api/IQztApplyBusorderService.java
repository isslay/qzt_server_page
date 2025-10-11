package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztApplyBusorder;
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
public interface IQztApplyBusorderService extends BaseService<QztApplyBusorder> {

    Page find(Page pageModel);

    /**
     * 根据用户ID查询订单
     *
     * @param userId
     * @return com.qzt.bus.model.QztApplyBusorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztApplyBusorder queryByUserId(Long userId);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo
     * @return com.qzt.bus.model.QztApplyBusorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztApplyBusorder queryByOrederNo(String orderNo);

    /**
     * 根据用户ID与订单编号查询订单
     *
     * @param orderNo
     * @param userId
     * @return com.qzt.bus.model.QztApplyBusorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztApplyBusorder queryByOrederNoUserId(String orderNo, Long userId);

    /**
     * 服务站申请订单支付
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map payApplay0rder(String orderNo, Long addressId, String payType, long balanceMoney, String payPwd, Long userId) throws Exception;

    /**
     * 支付回调
     *
     * @param orderNo
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-12
     */
    boolean applyOrderPayBack(String orderNo);

    /**
     * 自提
     *
     * @param entity
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-28
     */
    boolean offlineDistribution(QztApplyBusorder entity);

    /**
     * 服务站申请订单导出集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Xiaofei
     * @date 2019-12-04
     */
    List<Map<String, Object>> saorderExcel(Map map);

}
