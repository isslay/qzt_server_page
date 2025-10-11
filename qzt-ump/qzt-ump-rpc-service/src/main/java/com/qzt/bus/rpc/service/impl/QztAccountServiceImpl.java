package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztAccountMapper;
import com.qzt.bus.dao.mapper.QztUserMapper;
import com.qzt.bus.model.QztAccount;
import com.qzt.bus.model.QztAccountLog;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztAccountLogService;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.bus.rpc.api.IQztAccountService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.PriceUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztAccountService")
public class QztAccountServiceImpl extends BaseServiceImpl<QztAccountMapper, QztAccount> implements IQztAccountService {

    @Autowired
    private QztAccountMapper qztAccountMapper;

    @Autowired
    private QztUserMapper qztUserMapper;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Override
    public Page<QztAccount> find(Page<QztAccount> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztAccount> rb = this.qztAccountMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public Page findBack(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<Map> rb = this.qztAccountMapper.findBack(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public boolean updateQztAccount(String busId, Long userId, Long changeMoney, String changeSource, String changeType, String remark) {
        QztAccount qztAccount = this.qztAccountMapper.selectByUserId(userId);
        Integer result = this.qztAccountMapper.changeAccount(userId, changeMoney, changeType);
        if (result != 1) {
            return false;
        }
        Long moneyBalanceEnd = 0L;
        //处理变动后余额
        if ("01".equals(changeType)) {//增加余额
            moneyBalanceEnd = qztAccount.getAccountMoney() + changeMoney;
        } else if ("11".equals(changeType)) {//减少余额
            moneyBalanceEnd = qztAccount.getAccountMoney() - changeMoney;
        }
        //添加资金日志
//        QztUser qztUser = qztUserMapper.findUserById(userId+"");
//        this.qztAccountLogService.addAccountLog(busId, userId, qztAccount.getId(), changeType, changeMoney, changeSource, moneyBalanceEnd, qztAccount.getAccountMoney(), remark,qztUser.getUserType());
        return true;
    }

    @Override
    public QztAccount selectByUserId(Long userId) {
        return this.qztAccountMapper.selectByUserId(userId);
    }

}
