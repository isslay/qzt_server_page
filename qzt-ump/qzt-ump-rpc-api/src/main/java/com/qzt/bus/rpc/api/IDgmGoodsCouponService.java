package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmGoodsCoupon;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-10-18
 */
public interface IDgmGoodsCouponService extends BaseService<DgmGoodsCoupon> {

    Page<DgmGoodsCoupon> find(Page<DgmGoodsCoupon> pageModel);

    List<DgmGoodsCoupon> findList(Map<String, Object> map);
}
