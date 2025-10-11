package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztApplyBusorder;
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
public interface QztApplyBusorderMapper extends BaseMapper<QztApplyBusorder> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztApplyBusorder> find(Map<String, Object> map);

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
     * 根据用户ID与订单编号查询订单
     *
     * @param orderNo
     * @param userId
     * @return com.qzt.bus.model.QztApplyBusorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztApplyBusorder queryByOrederNoUserId(@Param("orderNo") String orderNo, @Param("userId") Long userId);

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
     * 支付更新信息
     *
     * @param qztApplyBusorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer applyOrderPayUpdate(QztApplyBusorder qztApplyBusorder);

    /**
     * 支付回调更新订单
     *
     * @param qztApplyBusorder
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer applyOrderPayBack(QztApplyBusorder qztApplyBusorder);

    /**
     * 自提
     *
     * @param entity
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-28
     */
    Integer offlineDistribution(QztApplyBusorder entity);
}
