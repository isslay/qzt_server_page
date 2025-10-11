package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztRechargeMapper;
import com.qzt.bus.model.QztRecharge;
import com.qzt.bus.rpc.api.IQztRechargeService;
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
 * @author Cgw
 * @since 2019-11-22
 */
@Service("qztRechargeService")
public class QztRechargeServiceImpl extends BaseServiceImpl<QztRechargeMapper, QztRecharge> implements IQztRechargeService {

    @Autowired
    private QztRechargeMapper qztRechargeMapper;

    @Override
    public Page<QztRecharge> find(Page<QztRecharge> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztRecharge> rb = this.qztRechargeMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<Map<String, Object>> rechargeExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztRecharge> rb = this.qztRechargeMapper.find(map);
        for (QztRecharge qztRecharge : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("createTime", qztRecharge.getCreateTime() == null ? "" : DateUtil.dateTimeFormat.format(qztRecharge.getCreateTime()));
            linkmaps.put("userId", qztRecharge.getUserId());
            linkmaps.put("userTel", qztRecharge.getUserTel());
            linkmaps.put("topUpMoney", PriceUtil.moneyLongToDecimal(qztRecharge.getTopUpMoney()));
            linkmaps.put("auditState", qztRecharge.getAuditState() == null ? "" : DicParamUtil.getDicCodeByType("RECHARGE_STATE", qztRecharge.getAuditState()));
            linkmaps.put("auditUserName", qztRecharge.getAuditUserName());
            linkmaps.put("auditTime", qztRecharge.getAuditTime() == null ? "" : DateUtil.dateTimeFormat.format(qztRecharge.getAuditTime()));
            linkmaps.put("auditRemark", qztRecharge.getAuditRemark());
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

}
