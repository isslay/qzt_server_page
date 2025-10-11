package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztHealthInfoMapper;
import com.qzt.bus.model.QztHealthInfo;
import com.qzt.bus.rpc.api.IQztHealthInfoService;
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
@Service("qztHealthInfoService")
public class QztHealthInfoServiceImpl extends BaseServiceImpl<QztHealthInfoMapper, QztHealthInfo> implements IQztHealthInfoService {

    @Autowired
    private QztHealthInfoMapper qztHealthInfoMapper;

    @Override
    public Page<QztHealthInfo> find(Page<QztHealthInfo> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztHealthInfo> rb = this.qztHealthInfoMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
