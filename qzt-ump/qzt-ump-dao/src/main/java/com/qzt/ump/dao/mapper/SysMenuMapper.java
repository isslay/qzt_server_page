package com.qzt.ump.dao.mapper;

import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.SysMenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author cgw
 * @since 2017-11-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenuModel> {

    /**
     * 根据用户ID查询菜单树
     *
     * @param userId
     * @return List<SysMenuModel>
     * @author cgw
     * @date 2017-12-03 00:51
     */
    List<SysMenuModel> selectMenuTreeByUserId(@Param("userId") Long userId);

    List<SysMenuModel> find(Map<String, Object> condition);
}
