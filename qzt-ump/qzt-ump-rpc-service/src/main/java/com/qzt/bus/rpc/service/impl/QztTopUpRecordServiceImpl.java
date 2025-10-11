package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztTopUpRecordMapper;
import com.qzt.bus.model.QztTopUpRecord;
import com.qzt.bus.rpc.api.IQztTopUpRecordService;
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
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztTopUpRecordService")
public class QztTopUpRecordServiceImpl extends BaseServiceImpl<QztTopUpRecordMapper, QztTopUpRecord> implements IQztTopUpRecordService {

    @Autowired
    private QztTopUpRecordMapper qztTopUpRecordMapper;

    @Override
    public Page<QztTopUpRecord> find(Page<QztTopUpRecord> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztTopUpRecord> rb = this.qztTopUpRecordMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }
}
