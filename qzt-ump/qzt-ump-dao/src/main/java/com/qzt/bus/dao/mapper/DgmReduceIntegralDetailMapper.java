package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmReduceIntegralDetail;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
public interface DgmReduceIntegralDetailMapper extends BaseMapper<DgmReduceIntegralDetail> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2024-02-19
     */
    List<DgmReduceIntegralDetail> find(Map<String, Object> map);

}
