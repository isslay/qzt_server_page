package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztPageBanner;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-25
 */
public interface QztPageBannerMapper extends BaseMapper<QztPageBanner> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2020-03-25
     */
    List<QztPageBanner> find(Map<String, Object> map);

}
