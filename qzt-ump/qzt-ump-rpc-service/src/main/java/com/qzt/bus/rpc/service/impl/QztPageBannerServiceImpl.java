package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztPageBannerMapper;
import com.qzt.bus.model.QztPageBanner;
import com.qzt.bus.rpc.api.IQztPageBannerService;
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
 * @since 2020-03-25
 */
@Service("qztPageBannerService")
public class QztPageBannerServiceImpl extends BaseServiceImpl<QztPageBannerMapper, QztPageBanner> implements IQztPageBannerService {

    @Autowired
    private QztPageBannerMapper qztPageBannerMapper;

    @Override
    public Page<QztPageBanner> find(Page<QztPageBanner> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztPageBanner> rb = this.qztPageBannerMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
