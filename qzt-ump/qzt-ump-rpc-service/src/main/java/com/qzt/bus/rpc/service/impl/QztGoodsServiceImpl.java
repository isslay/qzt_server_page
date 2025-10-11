package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztGoodsMapper;
import com.qzt.bus.model.QztGoods;
import com.qzt.bus.rpc.api.IQztGoodsService;
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
 * @author snow
 * @since 2019-11-05
 */
@Service("qztGoodsService")
public class QztGoodsServiceImpl extends BaseServiceImpl<QztGoodsMapper, QztGoods> implements IQztGoodsService {

    @Autowired
    private QztGoodsMapper qztGoodsMapper;

    @Override
    public Page<QztGoods> find(Page<QztGoods> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztGoods> rb = this.qztGoodsMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public Integer reduceGoodsRepertory(Long goodsId, Integer buyNum) {
        return this.qztGoodsMapper.reduceGoodsRepertory(goodsId, buyNum);
    }

    @Override
    public Integer addGoodsRepertory(Long goodsId, Integer buyNum) {
        return this.qztGoodsMapper.addGoodsRepertory(goodsId, buyNum);
    }

    @Override
    public Integer addGoodsSalesVolume(Long goodsId, Integer buyNum) {
        return this.qztGoodsMapper.addGoodsSalesVolume(goodsId, buyNum);
    }

}
