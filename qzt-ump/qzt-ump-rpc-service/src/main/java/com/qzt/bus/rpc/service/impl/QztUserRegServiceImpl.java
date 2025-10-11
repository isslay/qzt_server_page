package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztUserRegMapper;
import com.qzt.bus.model.QztUserReg;
import com.qzt.bus.rpc.api.IQztUserRegService;
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
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztUserRegService")
public class QztUserRegServiceImpl extends BaseServiceImpl<QztUserRegMapper, QztUserReg> implements IQztUserRegService {

    @Autowired
    private QztUserRegMapper qztUserRegMapper;

    @Override
    public Page<QztUserReg> find(Page<QztUserReg> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUserReg> rb = this.qztUserRegMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public boolean createRelationshipTree(String progressType, Integer type, Long userId, Long referrerId) {
        boolean reb = true;
        if (userId.equals(referrerId)) {
            return false;
        }
        Long resultrut = this.qztUserRegMapper.queryByPuserIdAndRuserId(new QztUserReg(referrerId, userId, type));
        if (resultrut == 0) {
            this.add(new QztUserReg(type, referrerId, userId, 1));
            this.qztUserRegMapper.createRelationshipTree(new QztUserReg("01", type, referrerId, userId));
            if ("03".equals(progressType)) {//逆向需添加子级下级为顶级父级的子级
                this.qztUserRegMapper.createRelationshipTree(new QztUserReg(progressType, type, referrerId, userId));
            }
        } else {
            reb = false;
        }
        return reb;
    }


}
