package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztOrderCommen;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztOrderCommenMapper extends BaseMapper<QztOrderCommen> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztOrderCommen> find(Map<String,Object> map);

}
