package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmOrderRefund;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-11-06
 */
public interface DgmOrderRefundMapper {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-11-06
     */
    List<DgmOrderRefund> find(Map<String, Object> map);

    int insertFK(DgmOrderRefund dgmOrderRefund);

}
