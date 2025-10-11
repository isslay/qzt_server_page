package com.qzt.ump.dao.mapper;


import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.SysDicSublist;
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
public interface SysDicSublistMapper extends BaseMapper<SysDicSublist> {

    List<SysDicSublist> find(Map<String,Object> map);

    List<SysDicSublist> findByDicType(Map<String,Object> map);

}
