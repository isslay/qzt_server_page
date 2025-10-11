package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztOrderCommenMapper;
import com.qzt.bus.model.QztOrderCommen;
import com.qzt.bus.rpc.api.IQztOrderCommenService;
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
 * @since 2019-11-11
 */
@Service("qztOrderCommenService")
public class QztOrderCommenServiceImpl extends BaseServiceImpl<QztOrderCommenMapper, QztOrderCommen> implements IQztOrderCommenService {

    @Autowired
    private QztOrderCommenMapper qztOrderCommenMapper;

    @Override
    public Page<QztOrderCommen> find(Page<QztOrderCommen> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztOrderCommen> rb = this.qztOrderCommenMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
