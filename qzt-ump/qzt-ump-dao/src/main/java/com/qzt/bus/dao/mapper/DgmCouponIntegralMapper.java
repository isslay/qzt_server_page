package com.qzt.bus.dao.mapper;

import com.qzt.bus.model.DgmCouponIntegral;
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
public interface DgmCouponIntegralMapper extends BaseMapper<DgmCouponIntegral> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-09-06
     */
    List<DgmCouponIntegral> find(Map<String, Object> map);

}
