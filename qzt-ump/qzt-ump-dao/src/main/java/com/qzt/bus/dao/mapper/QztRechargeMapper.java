package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztRecharge;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-22
 */
public interface QztRechargeMapper extends BaseMapper<QztRecharge> {

    /**
     *  通用查询方法
     * @param map
     * @author Cgw
     * @date 2019-11-22
     */
    List<QztRecharge> find(Map<String,Object> map);

}
