package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
public interface DgmUsableIntegralMapper extends BaseMapper<DgmUsableIntegral> {

    /**
     *  通用查询方法123
     * @param map
     * @author snow
     * @date 2024-02-19
     */
    List<DgmUsableIntegral> find(Map<String, Object> map);

    DgmUsableIntegral findById(@Param("id")Long id);

    int sumAll(@Param("userId")int userId);

    int monthOver(@Param("userId")int userId,@Param("overTime")String overTime);

}
