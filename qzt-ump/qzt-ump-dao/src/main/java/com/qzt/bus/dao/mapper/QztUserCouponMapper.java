package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztUserCoupon;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztUserCouponMapper extends BaseMapper<QztUserCoupon> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztUserCoupon> find(Map<String, Object> map);

    /**
     * 查询有效抵扣券
     *
     * @return com.qzt.bus.model.QztUserCoupon
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztUserCoupon queryEffectiveVouchers(QztUserCoupon qztUserCoupon);

    List<QztUserCoupon> queryEffectiveVouchersListAll(QztUserCoupon qztUserCoupon);

    List<QztUserCoupon> queryEffectiveVouchersList(QztUserCoupon qztUserCoupon);

    List<QztUserCoupon> queryEffectiveVouchersListToday(QztUserCoupon qztUserCoupon);

    /**
     * 下单锁券更新
     *
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer kockStampsUpdate(QztUserCoupon qztUserCoupon);

    /**
     * 激活个人优惠券
     *
     * @param params
     * @return
     */
    int updateActiveCoupon(Map<String, Object> params);

    /**
     *讲未使用的优惠券修改为已过期
     * @param params
     * @return
     */
     int updatePassCoupon(Map<String, Object> params);

}
