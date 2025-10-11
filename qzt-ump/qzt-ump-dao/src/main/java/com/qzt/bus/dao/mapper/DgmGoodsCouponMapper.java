package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmGoodsCoupon;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-10-18
 */
public interface DgmGoodsCouponMapper extends BaseMapper<DgmGoodsCoupon> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-10-18
     */
    List<DgmGoodsCoupon> find(Map<String, Object> map);

}
