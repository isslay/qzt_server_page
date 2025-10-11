package com.qzt.ump.dao.mapper;


import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.AppVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-26
 */
public interface AppVersionMapper extends BaseMapper<AppVersion> {

    List<AppVersion> find(Map<String, Object> map);

    /**
     * 获取最新版本信息
     *
     * @param sourceMode
     * @return com.qzt.ump.model.AppVersion
     * @author Xiaofei
     * @date 2019-06-26
     */
    AppVersion findMaxVersion(@Param("sourceMode") String sourceMode);

}
