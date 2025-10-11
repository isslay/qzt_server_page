package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztGoodsBannerMapper;
import com.qzt.bus.model.QztGoodsBanner;
import com.qzt.bus.rpc.api.IQztGoodsBannerService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author snow
 * @since 2019-11-06
 */
@Service("qztGoodsBannerService")
public class QztGoodsBannerServiceImpl extends BaseServiceImpl<QztGoodsBannerMapper, QztGoodsBanner> implements IQztGoodsBannerService {

    @Autowired
    private QztGoodsBannerMapper qztGoodsBannerMapper;

    @Override
    public Page<QztGoodsBanner> find(Page<QztGoodsBanner> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztGoodsBanner> rb = this.qztGoodsBannerMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public List<QztGoodsBanner> findList(Map<String, Object> map) {
        return this.qztGoodsBannerMapper.find(map);
    }
}
