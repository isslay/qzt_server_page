package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztAccountLogMapper;
import com.qzt.bus.model.QztAccountLog;
import com.qzt.bus.rpc.api.IQztAccountLogService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztAccountLogService")
public class QztAccountLogServiceImpl extends BaseServiceImpl<QztAccountLogMapper, QztAccountLog> implements IQztAccountLogService {

    @Autowired
    private QztAccountLogMapper qztAccountLogMapper;

    @Override
    public Page<QztAccountLog> find(Page<QztAccountLog> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztAccountLog> rb = this.qztAccountLogMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void addAccountLog(String busId, Long userId, Long accountId, String changeType, Long changeMoney, String changeSource, Long moneyBalanceEnd, Long moneyBalance, String remark,int logState) {
        this.add(new QztAccountLog(busId, userId, accountId, changeType, changeMoney, changeSource, moneyBalanceEnd, moneyBalance, remark,logState));
    }

    @Override
    public int findAccountByBusId(Map<String, Object> params) {
        return this.qztAccountLogMapper.findAccountByBusId(params);
    }

    @Override
    public Long queryUserAccount(Map<String, Object> params) {
        return this.qztAccountLogMapper.queryUserAccount(params);
    }

    @Override
    public List<QztAccountLog> findShareList(String busId) {
        return this.qztAccountLogMapper.findShareList(busId);
    }

    @Override
    public List<Map> findShareMapList(String busId) {
        return this.qztAccountLogMapper.findShareMapList(busId);
    }

}
