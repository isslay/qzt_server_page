package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztBanner;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2019-11-07
 */
public interface QztBannerMapper extends BaseMapper<QztBanner> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2019-11-07
     */
    List<QztBanner> find(Map<String, Object> map);

}
