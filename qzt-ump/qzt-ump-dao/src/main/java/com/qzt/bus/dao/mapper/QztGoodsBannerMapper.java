package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztGoodsBanner;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2019-11-06
 */
public interface QztGoodsBannerMapper extends BaseMapper<QztGoodsBanner> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2019-11-06
     */
    List<QztGoodsBanner> find(Map<String, Object> map);

}
