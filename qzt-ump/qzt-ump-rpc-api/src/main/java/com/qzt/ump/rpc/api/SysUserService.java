package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysRoleModel;
import com.qzt.ump.model.SysUserModel;
import com.qzt.ump.model.SysUserRoleModel;

import java.util.List;

/**
 * 用户管理服务
 *
 * @author cgw
 * @date 2017/11/17 16:43
 */
public interface SysUserService extends BaseService<SysUserModel> {

    /**
     * 根据账号查找用户
     *
     * @param account 账号
     * @return SysUserModel
     * @author cgw
     * @date 2017-12-05 12:48
     */
    SysUserModel queryByAccount(String account);

    /**
     * 分页查找所有用户
     *
     * @param page 分页对象
     * @return Page<SysUserModel>
     * @author cgw
     * @date 2017/12/4 14:45
     */
    Page<SysUserModel> queryListPage(Page<SysUserModel> page);

    /**
     * 根据ID集合批量删除
     *
     * @param ids 用户ID集合
     * @return boolean
     * @author cgw
     * @date 2017-12-05 19:50
     */
    boolean delBatchByIds(List<Long> ids);

    /**
     * 根据部门ID查询角色
     *
     * @param deptId 部门ID
     * @return java.util.List<SysRoleModel>
     * @author RickyWang
     * @date 17/12/25 15:47:20
     */
    List<SysRoleModel> queryRoles(Long deptId);

    /**
     * 根据用户查询用户角色关系
     *
     * @param userId 用户ID
     * @return java.util.List<SysUserRoleModel>
     * @author RickyWang
     * @date 17/12/25 21:20:55
     */
    List<SysUserRoleModel> queryUserRoles(Long userId);

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return SysUserModel
     * @author cgw
     * @date 2017-12-27 12:09
     */
    SysUserModel queryOne(Long id);

    /**
     * 修改
     *
     * @param sysUserModel 用户实体
     * @return boolean
     * @author cgw
     * @date 2017-12-27 12:09
     */
    boolean modifyUser(SysUserModel sysUserModel);

    boolean modifyUser1(SysUserModel sysUserModel);

    /**
     * 用户 停用 与 启用
     * @param sysUserModel
     * @return
     */
    boolean modifyStopUp(SysUserModel sysUserModel);

    SysUserRoleModel queryUserRole(Long userId);

    /**
     * 根据角色ID查找用户
     *
     * @param roleId 角色ID
     * @return List<SysUserModel>
     * @author dzz
     * @date 2018-10-07 12:48
     */
    List<SysUserModel> queryByRoleId(Long roleId);
}
