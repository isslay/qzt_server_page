package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmReduceIntegralDetailMapper;
import com.qzt.bus.model.DgmReduceIntegralDetail;
import com.qzt.bus.rpc.api.IDgmReduceIntegralDetailService;
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
 * @since 2024-02-19
 */
@Service("dgmReduceIntegralDetailService")
public class DgmReduceIntegralDetailServiceImpl extends BaseServiceImpl<DgmReduceIntegralDetailMapper, DgmReduceIntegralDetail> implements IDgmReduceIntegralDetailService {

    @Autowired
    private DgmReduceIntegralDetailMapper dgmReduceIntegralDetailMapper;

    @Override
    public Page<DgmReduceIntegralDetail> find(Page<DgmReduceIntegralDetail> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<DgmReduceIntegralDetail> rb = this.dgmReduceIntegralDetailMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
