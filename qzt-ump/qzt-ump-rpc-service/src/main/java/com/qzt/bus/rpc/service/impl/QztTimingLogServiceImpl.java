package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztTimingLogMapper;
import com.qzt.bus.model.QztTimingLog;
import com.qzt.bus.rpc.api.IQztTimingLogService;
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
@Service("qztTimingLogService")
public class QztTimingLogServiceImpl extends BaseServiceImpl<QztTimingLogMapper, QztTimingLog> implements IQztTimingLogService {

    @Autowired
    private QztTimingLogMapper qztTimingLogMapper;

    @Override
    public Page<QztTimingLog> find(Page<QztTimingLog> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztTimingLog> rb = this.qztTimingLogMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void addTimingLog(String busType, String busId, String resultEnforcement, String busRemark) {
        this.add(new QztTimingLog(busType, busId, resultEnforcement, busRemark));
    }

}
