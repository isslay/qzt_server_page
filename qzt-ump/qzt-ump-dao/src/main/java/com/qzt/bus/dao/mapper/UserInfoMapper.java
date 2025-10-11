package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.UserInfo;
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
 * @since 2023-08-29
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     *  通用查询方法
     * @param map
     * @author snow
     * @date 2023-08-29
     */
    List<UserInfo> find(Map<String, Object> map);

    UserInfo findById(@Param("id")Integer id);

}
