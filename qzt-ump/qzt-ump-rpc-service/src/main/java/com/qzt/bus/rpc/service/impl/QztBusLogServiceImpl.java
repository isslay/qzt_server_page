package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztBusLogMapper;
import com.qzt.bus.model.QztBusLog;
import com.qzt.bus.rpc.api.IQztBusLogService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@Service("qztBusLogService")
public class QztBusLogServiceImpl extends BaseServiceImpl<QztBusLogMapper, QztBusLog> implements IQztBusLogService {

    @Autowired
    private QztBusLogMapper qztBusLogMapper;

    @Override
    public Page<QztBusLog> find(Page<QztBusLog> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztBusLog> rb = this.qztBusLogMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void addBusLog(String businessType, String operNode, String businessId, String busRemark, Long operatorId) {
        this.add(new QztBusLog(businessType, operNode, businessId, busRemark, operatorId));
    }

    @Override
    public List<QztBusLog> findByBusinessId(String businessType, String businessId) {
        Map map = new HashMap();
        map.put("businessType", businessType);
        map.put("businessId", businessId);
        return this.qztBusLogMapper.find(map);
    }

}
