package com.qzt.ump.dao.mapper;


import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2018-10-17
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    List<SysArea> find(Map<String, Object> map);

    List<Map> findListMap(Map map);

    /**
     * 查询单条地区信息
     *
     * @param map
     * @return com.qzt.ump.model.SysArea
     * @author Xiaofei
     * @date 2019-09-26
     */
    SysArea findOne(Map<String, Object> map);

    Map findMapOne(@Param("id") String id);

    Map findAreaInfo(@Param("code") String code);

    Map test(Map fmap);

    Map test2(Map fmap);

}
