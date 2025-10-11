package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmZxtMapper;
import com.qzt.bus.model.DgmZxt;
import com.qzt.bus.rpc.api.IDgmZxtService;
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
 * @since 2023-09-27
 */
@Service("dgmZxtService")
public class DgmZxtServiceImpl extends BaseServiceImpl<DgmZxtMapper, DgmZxt> implements IDgmZxtService {

    @Autowired
    private DgmZxtMapper dgmZxtMapper;

    @Override
    public Page<DgmZxt> find(Page<DgmZxt> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<DgmZxt> rb = this.dgmZxtMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
