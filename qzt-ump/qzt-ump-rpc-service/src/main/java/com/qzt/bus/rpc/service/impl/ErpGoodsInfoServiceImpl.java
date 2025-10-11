package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.ErpGoodsInfoMapper;
import com.qzt.bus.model.ErpGoodsInfo;
import com.qzt.bus.rpc.api.IErpGoodsInfoService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2023-08-28
 */
@Service("erpGoodsInfoService")
public class ErpGoodsInfoServiceImpl extends BaseServiceImpl<ErpGoodsInfoMapper, ErpGoodsInfo> implements IErpGoodsInfoService {

    @Autowired
    private ErpGoodsInfoMapper erpGoodsInfoMapper;

    @Override
    public Page<ErpGoodsInfo> find(Page<ErpGoodsInfo> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
//        List<ErpGoodsInfo> rb = this.erpGoodsInfoMapper.find(page.getCondition());
        List<ErpGoodsInfo> rb = this.erpGoodsInfoMapper.findAllErpGoodsInfo(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public List<ErpGoodsInfo> findAll(ErpGoodsInfo pageModel) {
        Map dataMap = new HashMap();
        dataMap.put("goodsName", pageModel.getGoodsName());
        List<ErpGoodsInfo> rb = this.erpGoodsInfoMapper.findAllErpGoodsInfo(dataMap);
        return rb;
    }
}
