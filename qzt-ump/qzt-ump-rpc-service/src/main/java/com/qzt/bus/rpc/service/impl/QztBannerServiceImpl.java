package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztBannerMapper;
import com.qzt.bus.model.QztBanner;
import com.qzt.bus.rpc.api.IQztBannerService;
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
 * @since 2019-11-07
 */
@Service("qztBannerService")
public class QztBannerServiceImpl extends BaseServiceImpl<QztBannerMapper, QztBanner> implements IQztBannerService {

    @Autowired
    private QztBannerMapper qztBannerMapper;

    @Override
    public Page<QztBanner> find(Page<QztBanner> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztBanner> rb = this.qztBannerMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
