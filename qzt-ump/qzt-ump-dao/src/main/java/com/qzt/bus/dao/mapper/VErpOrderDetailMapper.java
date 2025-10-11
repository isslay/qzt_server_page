package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.VErpOrderDetail;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-10-23
 */
public interface VErpOrderDetailMapper extends BaseMapper<VErpOrderDetail> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-10-23
     */
    List<VErpOrderDetail> find(Map<String, Object> map);

}
