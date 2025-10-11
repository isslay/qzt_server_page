package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmGoodsClass;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
public interface DgmGoodsClassMapper extends BaseMapper<DgmGoodsClass> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-09-25
     */
    List<DgmGoodsClass> find(Map<String, Object> map);

}
