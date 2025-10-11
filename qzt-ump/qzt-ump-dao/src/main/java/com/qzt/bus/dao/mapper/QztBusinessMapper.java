package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztBusiness;
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
public interface QztBusinessMapper extends BaseMapper<QztBusiness> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztBusiness> find(Map<String, Object> map);

    /**
     * 根据条件查询所有服务站
     *
     * @param params
     * @return
     */
    List<QztBusiness> findAllBussiness(Map<String, Object> params);

    /**
     * 根据userId查询服务站信息
     *
     * @param userId
     * @return com.qzt.bus.model.QztBusiness
     * @author Xiaofei
     * @date 2019-11-17
     */
    QztBusiness queryByUserId(Long userId);

    /**
     * 查询指定区的服务站数量
     *
     * @param area
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-12-12
     */
    Long findAreaBussinessSize(String area);

}
