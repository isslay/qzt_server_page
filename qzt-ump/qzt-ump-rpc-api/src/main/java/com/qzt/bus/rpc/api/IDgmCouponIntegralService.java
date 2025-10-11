package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmCoupon;
import com.qzt.bus.model.DgmCouponIntegral;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
public interface IDgmCouponIntegralService extends BaseService<DgmCouponIntegral> {

    Page<DgmCouponIntegral> find(Page<DgmCouponIntegral> pageModel);

    List<DgmCouponIntegral> findList(Map<String, Object> map);

    /**
     * 积分优惠券
     *
     * @param userId
     * @return
     */
    void creatCouponIntegral(Long userId, Long couponId, int num);

}
