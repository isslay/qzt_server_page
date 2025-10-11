package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztPayBacklogMapper;
import com.qzt.bus.model.QztPayBacklog;
import com.qzt.bus.rpc.api.IQztPayBacklogService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.OrderUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
@Service("qztPayBacklogService")
public class QztPayBacklogServiceImpl extends BaseServiceImpl<QztPayBacklogMapper, QztPayBacklog> implements IQztPayBacklogService {

    @Autowired
    private QztPayBacklogMapper qztPayBacklogMapper;

    @Override
    public Page<QztPayBacklog> find(Page<QztPayBacklog> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztPayBacklog> rb = this.qztPayBacklogMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void addPayBacklog(String backSource, String payNo, String backPayNo, String payMoney, String message) {
        this.add(new QztPayBacklog(backSource, payNo, payNo, backPayNo, new BigDecimal(payMoney), payNo.substring(0, 2), message));
    }

    @Override
    public List<QztPayBacklog> findByOrderNoType(String busType, String orderNo) {
        Map map = new HashMap<>();
        map.put("busType", busType);
        map.put("orderNo", orderNo);
        return this.qztPayBacklogMapper.find(map);
    }

}
