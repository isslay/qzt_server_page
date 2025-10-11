package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztUserBankMapper;
import com.qzt.bus.model.QztUserBank;
import com.qzt.bus.rpc.api.IQztUserBankService;
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
 * @author Xiaofei
 * @since 2019-11-11
 */
@Service("qztUserBankService")
public class QztUserBankServiceImpl extends BaseServiceImpl<QztUserBankMapper, QztUserBank> implements IQztUserBankService {

    @Autowired
    private QztUserBankMapper qztUserBankMapper;

    @Override
    public Page<QztUserBank> find(Page<QztUserBank> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUserBank> rb = this.qztUserBankMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztUserBank> findList(Map map) {
        return this.qztUserBankMapper.find(map);
    }

    @Override
    public QztUserBank selectByIdAndUserId(QztUserBank qztUserBank) {
        return this.qztUserBankMapper.selectByIdAndUserId(qztUserBank);
    }

    @Override
    public Long selectUserBankSize(Long userId) {
        Map map = this.qztUserBankMapper.selectUserBankSize(userId);
        Long ailSize = (Long) map.get("ailSize");
        Long bankSize = (Long) map.get("bankSize");
        return ailSize + bankSize;
    }

    @Override
    public void defaultUserBank(QztUserBank entity) {
        this.qztUserBankMapper.defaultUserBank(entity);
    }

}
