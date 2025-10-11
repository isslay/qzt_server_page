package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztUserCouponService extends BaseService<QztUserCoupon> {

    Page<QztUserCoupon> find(Page<QztUserCoupon> pageModel);

    List<QztUserCoupon> findList(Map<String, Object> map);

    /**
     * 查询有效抵扣券 传入 券ID 用户ID
     *
     * @return com.qzt.bus.model.QztUserCoupon
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztUserCoupon queryEffectiveVouchers(QztUserCoupon qztUserCoupon);

    /**
     * 下单锁券更新
     *
     * @param state       使用状态(待使用1、已使用2)
     * @param orderNo      订单编号
     * @param cashCouponId 券ID
     * @param userId       使用人ID
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-12
     */
    boolean kockStampsUpdate(String state, String orderNo, Long cashCouponId, Long userId);

    /**
     * 查询用户可用券
     */
    List<Map> queryCouponByUserId(Long userId);
    /**
     * 新用户登录-折扣券
     */
    List<Map> queryCouponByUserId(Long userId,Long num);
    /**
     * 查询用户新手券
     */
    List<QztUserCoupon> queryStorePromotion(Long userId);
    /**
     * 查询用户商品券
     */
    List<Map> queryGoodsPromotion(Long userId,String goodsIds);
    /**
     * 查询用户活动券
     */
    List<Map> queryActivityPromotion(Long userId);
    /**
     * 生成优惠券
     * @param params
     * @return
     * @throws Exception
     */
    String saveUserCoupon(Map<String,Object> params) throws Exception;

    /**(
     * 激活个人优惠券
     * @param params
     * @return
     */
    boolean activeCoupon(Map<String,Object> params);

    /**
     * 批量更新已过期的优惠券
     * @param params
     * @return
     */
    int updatePassCoupon(Map<String, Object> params);

}
