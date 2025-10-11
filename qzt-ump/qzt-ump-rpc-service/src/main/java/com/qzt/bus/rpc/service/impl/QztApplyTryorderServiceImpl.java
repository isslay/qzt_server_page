package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztApplyTryorderMapper;
import com.qzt.bus.model.QztApplyTryorder;
import com.qzt.bus.rpc.api.IQztApplyTryorderService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
@Service("qztApplyTryorderService")
public class QztApplyTryorderServiceImpl extends BaseServiceImpl<QztApplyTryorderMapper, QztApplyTryorder> implements IQztApplyTryorderService {

    @Autowired
    private QztApplyTryorderMapper qztApplyTryorderMapper;

    @Override
    public Page<QztApplyTryorder> find(Page<QztApplyTryorder> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztApplyTryorder> rb = this.qztApplyTryorderMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public boolean updateTryOrder(Map<String, Object> params) {
        int rs = this.qztApplyTryorderMapper.updateTryOrder(params);
        return rs > 0 ? true : false;

    }

    @Override
    public List<QztApplyTryorder> queryOrderByCradId(String cardId, String disType) {
        return this.qztApplyTryorderMapper.queryOrderByCradId(cardId, disType);
    }

    @Override
    public boolean checkTryOrder(Map<String, Object> params) {
        int rs = this.qztApplyTryorderMapper.checkTryOrder(params);
        if (rs > 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> syorderExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztApplyTryorder> rb = this.qztApplyTryorderMapper.find(map);
        for (QztApplyTryorder qztApplyTryorder : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("orderNo", qztApplyTryorder.getOrderNo());
            linkmaps.put("createTime", DateUtil.dateTimeFormat.format(qztApplyTryorder.getCreateTime()));
            linkmaps.put("userId", qztApplyTryorder.getApplyUserId());
            linkmaps.put("referrerUserId", qztApplyTryorder.getReferrerUserId());
            linkmaps.put("storeType", qztApplyTryorder.getStoreType());
            linkmaps.put("storeName", qztApplyTryorder.getStoreName());
            linkmaps.put("diseaseName", qztApplyTryorder.getDiseaseName());
            linkmaps.put("busName", qztApplyTryorder.getBusName());
            linkmaps.put("orderState", qztApplyTryorder.getOrderState() == null ? "" : DicParamUtil.getDicCodeByType("TRY_STATE", qztApplyTryorder.getOrderState()));
            linkmaps.put("idCardno", qztApplyTryorder.getIdCardno());
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

}
