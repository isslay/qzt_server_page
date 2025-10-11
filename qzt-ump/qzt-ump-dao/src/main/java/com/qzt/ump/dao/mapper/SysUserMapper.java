package com.qzt.ump.dao.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseMapper;
import com.qzt.ump.model.SysUserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表 Mapper 接口
 *
 * @author cgw
 * @date 2017/11/17 15:51
 */
public interface SysUserMapper extends BaseMapper<SysUserModel> {

    /**
     * 根据关键字分页查询
     *
     * @param page      分页对象
     * @param searchKey 关键字
     * @return List<SysUserModel>
     * @author cgw
     * @date 2017-12-27 12:06
     */
    List<SysUserModel> selectPage(Page<SysUserModel> page, @Param("searchKey") String searchKey);

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return SysUserModel
     * @author cgw
     * @date 2017-12-27 12:07
     */
    SysUserModel selectOne(@Param("id") Long id);

    /**
     * 查询用户信息
     *
     * @return List<SysUserModel>
     * @author Xiaofei
     * @date 2018-08-02
     */
    List<SysUserModel> selectUserPage(Map map);

    /**
     * 查询某一角色的用户信息
     * @param map
     * @return
     */
    List<SysUserModel> selectUserByRoleId(Map<String,Object> map);
}
