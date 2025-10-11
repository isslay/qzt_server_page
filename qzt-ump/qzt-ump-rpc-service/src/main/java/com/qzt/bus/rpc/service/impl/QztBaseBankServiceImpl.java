package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztBaseBankMapper;
import com.qzt.bus.model.QztBaseBank;
import com.qzt.bus.rpc.api.IQztBaseBankService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@Service("qztBaseBankService")
public class QztBaseBankServiceImpl extends BaseServiceImpl<QztBaseBankMapper, QztBaseBank> implements IQztBaseBankService {

    @Autowired
    private QztBaseBankMapper qztBaseBankMapper;

    @Override
    public Page<QztBaseBank> find(Page<QztBaseBank> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztBaseBank> rb = this.qztBaseBankMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztBaseBank> findList() {
        return this.qztBaseBankMapper.find(null);
    }

}
