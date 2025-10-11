package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztUserAddress;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztUserAddressMapper extends BaseMapper<QztUserAddress> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztUserAddress> find(Map<String, Object> map);

    /**
     * 修改默认地址 0
     *
     * @param entity
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-11
     */
    Integer defaultArea(QztUserAddress entity);

}
