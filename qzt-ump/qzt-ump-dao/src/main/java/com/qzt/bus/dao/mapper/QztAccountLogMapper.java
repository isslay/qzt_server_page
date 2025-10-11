package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztAccountLog;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztAccountLogMapper extends BaseMapper<QztAccountLog> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztAccountLog> find(Map<String, Object> map);


    int findAccountByBusId(Map<String, Object> params);


    Long queryUserAccount(Map<String, Object> params);

    /**
     * 查询某业务分润记录
     *
     * @param busId
     * @return java.util.List=
     * @author Xiaofei
     * @date 2019-11-23
     */
    List<QztAccountLog> findShareList(String busId);

    List<Map> findShareMapList(String busId);

}
