package com.qzt.ump.dao.mapper;


import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.SysDic;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
public interface SysDicMapper extends BaseMapper<SysDic> {

    /**
     * 分页查询字典主表
     * @param map
     * @return
     */
    List<SysDic> find(Map<String,Object> map);

    List<SysDic> test();

    /**
     * 分页查询字典子表
     * @param map
     * @return
     */
    List<Map> queryListSublistPage(Map<String, Object> map);

}
