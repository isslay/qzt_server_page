package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.UserInfoMapper;
import com.qzt.bus.model.UserInfo;
import com.qzt.bus.rpc.api.IUserInfoService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author snow
 * @since 2023-08-29
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Page<UserInfo> find(Page<UserInfo> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<UserInfo> rb = this.userInfoMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public UserInfo findById(Integer id) {
        return this.userInfoMapper.findById(id);
    }
}
