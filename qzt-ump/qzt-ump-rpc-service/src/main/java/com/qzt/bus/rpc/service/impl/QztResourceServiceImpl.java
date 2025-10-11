package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztResourceMapper;
import com.qzt.bus.model.QztResource;
import com.qzt.bus.rpc.api.IQztResourceService;
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
 * @author Xiaofei
 * @since 2020-03-26
 */
@Service("qztResourceService")
public class QztResourceServiceImpl extends BaseServiceImpl<QztResourceMapper, QztResource> implements IQztResourceService {

    @Autowired
    private QztResourceMapper qztResourceMapper;

    @Override
    public Page<QztResource> find(Page<QztResource> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztResource> rb = this.qztResourceMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
