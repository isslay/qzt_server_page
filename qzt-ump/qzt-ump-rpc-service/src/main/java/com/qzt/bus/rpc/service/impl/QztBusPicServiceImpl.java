package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztBusPicMapper;
import com.qzt.bus.model.QztBusPic;
import com.qzt.bus.rpc.api.IQztBusPicService;
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
 * @author Xiaofei
 * @since 2019-11-11
 */
@Service("qztBusPicService")
public class QztBusPicServiceImpl extends BaseServiceImpl<QztBusPicMapper, QztBusPic> implements IQztBusPicService {

    @Autowired
    private QztBusPicMapper qztBusPicMapper;

    @Override
    public Page<QztBusPic> find(Page<QztBusPic> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztBusPic> rb = this.qztBusPicMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public boolean saveBusPics(List<QztBusPic> busPicList) {
        int rs = this.qztBusPicMapper.saveBusPics(busPicList);
        if(rs>0){
            return  true;
        }
        return false;
    }

    @Override
    public List<QztBusPic> getBusPics(Map<String, Object> params) {
        return this.qztBusPicMapper.getBusPics(params);
    }

    @Override
    public int delBusPics(String busId) {
        return this.qztBusPicMapper.delBusPics(busId);
    }


}
