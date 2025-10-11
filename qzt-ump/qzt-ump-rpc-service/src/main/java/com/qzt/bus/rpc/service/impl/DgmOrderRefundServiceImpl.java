package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmOrderRefundMapper;
import com.qzt.bus.model.DgmOrderRefund;
import com.qzt.bus.rpc.api.IDgmOrderRefundService;
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
 * @since 2023-11-06
 */
@Service("dgmOrderRefundService")
public class DgmOrderRefundServiceImpl implements IDgmOrderRefundService {

    @Autowired
    private DgmOrderRefundMapper dgmOrderRefundMapper;

    @Override
    public Page<DgmOrderRefund> find(Page<DgmOrderRefund> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<DgmOrderRefund> rb = this.dgmOrderRefundMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public int insert(DgmOrderRefund dgmOrderRefund) {
        return this.dgmOrderRefundMapper.insertFK(dgmOrderRefund);
    }
}
