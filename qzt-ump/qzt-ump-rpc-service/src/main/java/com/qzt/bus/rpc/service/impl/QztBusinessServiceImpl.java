package com.qzt.bus.rpc.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztBusinessMapper;
import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.rpc.api.SysAreaService;
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
 * @since 2019-11-11
 */
@Service("qztBusinessService")
public class QztBusinessServiceImpl extends BaseServiceImpl<QztBusinessMapper, QztBusiness> implements IQztBusinessService {

    @Autowired
    private QztBusinessMapper qztBusinessMapper;

    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public Page find(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztBusiness> rb = this.qztBusinessMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public QztBusiness addBusiness(QztApplyBusorder qztApplyBusorder) {
        String province = qztApplyBusorder.getContext().substring(0, 2);
        String city = qztApplyBusorder.getContext().substring(2, 7);
        String area = qztApplyBusorder.getContext().substring(7, 12);
        String address = sysAreaService.selectAreaName(province, city, area);
        address += qztApplyBusorder.getStateMark();
        QztBusiness qztBusiness = new QztBusiness(qztApplyBusorder.getApplyUserId(), address, qztApplyBusorder.getContactTel(),
                qztApplyBusorder.getReferrerUserId() == null ? "" : qztApplyBusorder.getReferrerUserId().toString(), province, city, area, qztApplyBusorder.getContactName());
        return this.add(qztBusiness);
    }

    @Override
    public List<QztBusiness> findAllBussiness(Map<String, Object> params) {
        return this.qztBusinessMapper.findAllBussiness(params);
    }

    @Override
    public QztBusiness queryByUserId(Long userId) {
        return this.qztBusinessMapper.queryByUserId(userId);
    }

    @Override
    public Long findAreaBussinessSize(String areaCode) {
        return this.qztBusinessMapper.findAreaBussinessSize(areaCode.substring(7));
    }

    @Override
    public Integer findScopeBussinessSize(String busLong, String busLat, Long kilometer) {
        Map pmap = new HashMap();
        pmap.put("kilometer", kilometer);
        pmap.put("busState", "0");
        pmap.put("busLong", busLong);
        pmap.put("busLat", busLat);
        List<QztBusiness> qztBusinessList = this.qztBusinessMapper.find(pmap);
        return qztBusinessList == null ? 0 : qztBusinessList.size();
    }

}
