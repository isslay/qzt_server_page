package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztServiceOrderMapper;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztServiceOrder;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.bus.rpc.api.IQztServiceOrderService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
@Service("qztServiceOrderService")
public class QztServiceOrderServiceImpl extends BaseServiceImpl<QztServiceOrderMapper, QztServiceOrder> implements IQztServiceOrderService {

    @Autowired
    private QztServiceOrderMapper qztServiceOrderMapper;

    @Autowired
    private IQztBusinessService qztBusiness;

    @Override
    public Page<QztServiceOrder> find(Page<QztServiceOrder> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztServiceOrder> rb = this.qztServiceOrderMapper.find(page.getCondition());
        for (QztServiceOrder qso : rb) {
            QztBusiness qztBusiness = this.qztBusiness.queryById(qso.getBusniessId());
            if (qztBusiness != null) {
                qso.setBusniessName(qztBusiness.getBusName());
            }
        }
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<Map<String, Object>> soorderExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztServiceOrder> rb = this.qztServiceOrderMapper.find(map);
        for (QztServiceOrder qztServiceOrder : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("userId", qztServiceOrder.getUserId());
            String busniessName = "";
            QztBusiness qztBusiness = this.qztBusiness.queryById(qztServiceOrder.getBusniessId());
            if (qztBusiness != null) {
                busniessName = qztBusiness.getBusName();
            }
            linkmaps.put("busniessName", busniessName);
            linkmaps.put("orderNo", qztServiceOrder.getOrderNo());
            linkmaps.put("orderState", qztServiceOrder.getOrderState() == null ? "" : DicParamUtil.getDicCodeByType("SERVER_ORDER_STATE", qztServiceOrder.getOrderState()));
            linkmaps.put("contactsName", qztServiceOrder.getContactsName());
            linkmaps.put("contactsTel", qztServiceOrder.getContactsTel());
            linkmaps.put("disease", qztServiceOrder.getDisease());
            linkmaps.put("shareMoney", PriceUtil.moneyLongToDecimal(qztServiceOrder.getShareMoney()));
            linkmaps.put("finishTime", qztServiceOrder.getFinishTime() == null ? "" : DateUtil.dateTimeFormat.format(qztServiceOrder.getFinishTime()));
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

}
