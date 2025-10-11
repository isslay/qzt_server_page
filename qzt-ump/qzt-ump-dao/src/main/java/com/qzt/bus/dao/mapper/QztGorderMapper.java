package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztGorder;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztGorderMapper extends BaseMapper<QztGorder> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztGorder> find(Map<String, Object> map);

    /**
     * 根据订单编号与用户ID查询订单
     *
     * @param qztGorder
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztGorder queryByOrederNoUserId(QztGorder qztGorder);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztGorder queryByOrederNo(String orderNo);

    /**
     * 商品订单支付更新
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer orderPayUpdate(QztGorder qztGorder);

    /**
     * 支付回调更新订单状态
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer orderPayBack(QztGorder qztGorder);

    /**
     * 取消订单
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author
     * @date
     */
    Integer cancellationOfOrder(QztGorder qztGorder);
    /**
     * 取消订单（已支付待发货）
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author
     * @date
     */
    Integer cancellationOfOrder1(QztGorder qztGorder);

    /**
     * 用户确认收货
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-14
     */
    Integer confirmReceipt(QztGorder qztGorder);

    /**
     * 更新是否可申请服务状态
     *
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-20
     */
    Integer updateIsServe(QztGorder qztGorder);

    /**
     * 自提
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-23
     */
    Integer offlineDistribution(QztGorder qztGorder);

    /**
     * 查询可申请服务订单
     *
     * @param pmap
     * @return java.util.List<com.qzt.bus.model.QztGorder>
     * @author Xiaofei
     * @date 2019-11-28
     */
    List<QztGorder> getCanServiceOrderList(Map pmap);

    /**
     * 商家确认核销
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-12-13
     */
    Integer confirmTheSettlement(QztGorder qztGorder);

    /**
     * 根据商家ID与核销码查询订单
     *
     * @param qztGorder
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-12-13
     */
    QztGorder findChargeOffOrderInfo(QztGorder qztGorder);

}
