package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztApplyTryorder;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
public interface QztApplyTryorderMapper extends BaseMapper<QztApplyTryorder> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztApplyTryorder> find(Map<String, Object> map);

    int updateTryOrder(Map<String, Object> params);

    List<QztApplyTryorder> queryOrderByCradId(@Param("cardId") String cardId, @Param("disType") String disType);

    int checkTryOrder(Map<String, Object> params);

}
