package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.ErpGoodsInfo;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2023-08-28
 */
public interface ErpGoodsInfoMapper extends BaseMapper<ErpGoodsInfo> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2023-08-28
     */
    List<ErpGoodsInfo> find(Map<String, Object> map);

    List<ErpGoodsInfo> findAllErpGoodsInfo(Map<String, Object> params);

}
