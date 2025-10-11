package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmZxt;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2023-09-27
 */
public interface DgmZxtMapper extends BaseMapper<DgmZxt> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-09-27
     */
    List<DgmZxt> find(Map<String, Object> map);

}
