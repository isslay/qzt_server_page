package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztServiceOrder;
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
public interface QztServiceOrderMapper extends BaseMapper<QztServiceOrder> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztServiceOrder> find(Map<String,Object> map);

}
