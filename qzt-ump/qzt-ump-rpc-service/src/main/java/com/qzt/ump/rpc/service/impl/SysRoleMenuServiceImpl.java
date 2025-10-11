package com.qzt.ump.rpc.service.impl;

import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.ump.dao.mapper.SysRoleMenuMapper;
import com.qzt.ump.model.SysRoleMenuModel;
import com.qzt.ump.rpc.api.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色授权表 服务实现类
 * </p>
 *
 * @author cgw
 * @since 2017-12-17
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenuModel> implements SysRoleMenuService {

    @Override
    public boolean delBatchByRoleId(Long roleId) {
        return false;
    }
}
