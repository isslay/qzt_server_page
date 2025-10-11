package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztStoGoodsMapper;
import com.qzt.bus.model.QztStoGoods;
import com.qzt.bus.rpc.api.IQztGoodsService;
import com.qzt.bus.rpc.api.IQztStoGoodsService;
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
 * @since 2019-12-24
 */
@Service("qztStoGoodsService")
public class QztStoGoodsServiceImpl extends BaseServiceImpl<QztStoGoodsMapper, QztStoGoods> implements IQztStoGoodsService {

    @Autowired
    private QztStoGoodsMapper qztStoGoodsMapper;

    @Autowired
    private IQztGoodsService qztGoodsService;

    @Override
    public Page<QztStoGoods> find(Page<QztStoGoods> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztStoGoods> rb = this.qztStoGoodsMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztStoGoods> findList(Map pmap) {
        return this.qztStoGoodsMapper.find(pmap);
    }

    @Override
    public QztStoGoods queryByGoodsId(Long userId, Long goodsId) {
        return this.qztStoGoodsMapper.queryByGoodsId(new QztStoGoods(userId, goodsId, null));
    }

    @Override
    public boolean updateStoGoodsList(List<QztStoGoods> stoGoodsList) {
        Integer result = this.qztStoGoodsMapper.updateStoGoodsList(stoGoodsList);
        return result > 0;
    }

    @Override
    public boolean updateStoGoodsList1(Long userId) {
        Integer result = this.qztStoGoodsMapper.updateStoGoodsList1(userId);
        return result > 0;
    }

    @Override
    public boolean addGoods(QztStoGoods qztStoGoods) {
        boolean resulr = true;
        qztStoGoods.setBuyNum(qztStoGoods.getBuyNum() == null || qztStoGoods.getBuyNum() < 1 ? 1 : qztStoGoods.getBuyNum());
        QztStoGoods qztStoGoodsid = this.queryByGoodsId(qztStoGoods.getUserId(), qztStoGoods.getGoodsId());
        if (qztStoGoodsid == null) {
            QztStoGoods qztStoGoodsre = this.add(qztStoGoods);
            if (qztStoGoodsre == null) {
                resulr = false;
            }
        } else {
            qztStoGoodsid.setBuyNum(qztStoGoodsid.getBuyNum() + qztStoGoods.getBuyNum());
            QztStoGoods qztStoGoodsrs = this.modifyById(qztStoGoodsid);
            if (qztStoGoodsrs == null) {
                resulr = false;
            }
        }
        return resulr;
    }

    @Override
    public QztStoGoods addGoods1(QztStoGoods qztStoGoods) {
        qztStoGoods.setBuyNum(qztStoGoods.getBuyNum() == null || qztStoGoods.getBuyNum() < 1 ? 1 : qztStoGoods.getBuyNum());
        QztStoGoods qztStoGoodsid = this.queryByGoodsId(qztStoGoods.getUserId(), qztStoGoods.getGoodsId());
        if (qztStoGoodsid == null) {
            QztStoGoods qztStoGoodsre = this.add(qztStoGoods);
            return qztStoGoodsre;
        } else {
            qztStoGoodsid.setBuyNum(qztStoGoodsid.getBuyNum() + qztStoGoods.getBuyNum());
            QztStoGoods qztStoGoodsrs = this.modifyById(qztStoGoodsid);
            return qztStoGoodsrs;
        }
    }

    @Override
    public List<QztStoGoods> getSettlementGoodsList(Long userId, String isSingleCommodity, String orderNo) {
        Map pmap = new HashMap();
        pmap.put("orderNo", "Y".equals(isSingleCommodity) ? orderNo : null);
        pmap.put("userId", userId);
        pmap.put("isSingleCommodity", isSingleCommodity);
        pmap.put("isDel", "N");
        pmap.put("isCart", "Y".equals(isSingleCommodity) ? "N" : "Y");
        pmap.put("isPitchon", "Y");
        return this.qztStoGoodsMapper.find(pmap);
    }

    @Override
    public List<QztStoGoods> getOrderGoodsList(Long userId, String orderNo) {
        Map pmap = new HashMap();
        pmap.put("userId", userId);
        pmap.put("orderNo", orderNo);
        return this.qztStoGoodsMapper.find(pmap);
    }

}
