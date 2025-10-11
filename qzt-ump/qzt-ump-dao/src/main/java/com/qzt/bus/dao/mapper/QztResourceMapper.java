package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztResource;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-26
 */
public interface QztResourceMapper extends BaseMapper<QztResource> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2020-03-26
     */
    List<QztResource> find(Map<String, Object> map);

}
