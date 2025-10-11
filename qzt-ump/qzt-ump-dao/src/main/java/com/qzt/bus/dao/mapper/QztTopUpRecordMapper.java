package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztTopUpRecord;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztTopUpRecordMapper extends BaseMapper<QztTopUpRecord> {

    /**
     *  通用查询方法
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztTopUpRecord> find(Map<String,Object> map);

}
