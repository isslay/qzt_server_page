package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmCoupon;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
public interface IDgmCouponService extends BaseService<DgmCoupon> {

    Page<DgmCoupon> find(Page<DgmCoupon> pageModel);

    List<DgmCoupon> findList(Map<String, Object> map);
    /**
     * 折扣优惠券
     * @param userId
     * @return
     */
    Object creatUserCoupon(Long userId);
    /**
     * 新手优惠券
     * @param userId
     * @return
     */
    void creatNewUserCoupon(Long userId);
    /**
     * 商品优惠券
     * @param userId
     * @return
     */
    void creatGoodsUserCoupon(Long userId,Long couponId);

    /**
     * 活动优惠券
     * @param userId
     * @return
     */
    Object creatActivityCoupon(Long userId);
}
