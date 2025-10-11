package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztBusPic;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztBusPicMapper extends BaseMapper<QztBusPic> {

    /**
     *  通用查询方法
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztBusPic> find(Map<String,Object> map);

    int saveBusPics(List<QztBusPic> busPicList);

    List<QztBusPic> getBusPics(Map<String,Object> params);


    int delBusPics(@Param("busId") String busId);

}
