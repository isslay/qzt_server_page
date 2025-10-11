package com.qzt.ump.rpc.api;

import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysRoleMenuModel;

/**
 * <p>
 * 角色授权表 服务类
 * </p>
 *
 * @author cgw
 * @since 2017-12-17
 */
public interface SysRoleMenuService extends BaseService<SysRoleMenuModel> {

    /**
     * 根据角色ID批量删除角色和菜单的关系
     *
     * @param roleId
     * @return boolean
     * @author cgw
     * @date 2017-12-24 15:01
     */
    boolean delBatchByRoleId(Long roleId);
}
