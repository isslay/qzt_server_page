package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmCoupon;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
public interface DgmCouponMapper extends BaseMapper<DgmCoupon> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-09-06
     */
    List<DgmCoupon> find(Map<String, Object> map);

}
