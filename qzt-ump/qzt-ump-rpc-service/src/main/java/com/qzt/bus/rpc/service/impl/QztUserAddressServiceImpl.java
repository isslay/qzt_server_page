package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztUserAddressMapper;
import com.qzt.bus.model.QztUserAddress;
import com.qzt.bus.rpc.api.IQztUserAddressService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.rpc.api.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Service("qztUserAddressService")
public class QztUserAddressServiceImpl extends BaseServiceImpl<QztUserAddressMapper, QztUserAddress> implements IQztUserAddressService {

    @Autowired
    private QztUserAddressMapper qztUserAddressMapper;

    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public Page<QztUserAddress> find(Page<QztUserAddress> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUserAddress> rb = this.qztUserAddressMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztUserAddress> findList(Map map) {
        return this.qztUserAddressMapper.find(map);
    }

    @Override
    public List<Map> findListByUserId(Long userId) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("isDel", "N");
        List<QztUserAddress> qztUserAddressList = this.qztUserAddressMapper.find(map);
        List<Map> data = new ArrayList<>();
        for (QztUserAddress qztUserAddresss : qztUserAddressList) {
            Map mapr = new HashMap();
            mapr.put("id", qztUserAddresss.getId());
            mapr.put("recipientName", qztUserAddresss.getRecipientName());
            mapr.put("phone", qztUserAddresss.getPhone());
            mapr.put("isDefault", qztUserAddresss.getIsDefault());
            mapr.put("zipCode", qztUserAddresss.getZipCode());
            //翻译省市区
            String areaName = this.sysAreaService.selectAreaName(qztUserAddresss.getProvince(), qztUserAddresss.getCity(), qztUserAddresss.getArea());
            mapr.put("address", areaName + qztUserAddresss.getDetailAddress());
            data.add(mapr);
        }
        return data;
    }

    @Override
    public Integer defaultArea(QztUserAddress entity) {
        return this.qztUserAddressMapper.defaultArea(entity);
    }

    @Override
    public QztUserAddress selectDefaultaccAddress(Long userId) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("isDefault", "Y");
        map.put("isDel", "N");
        QztUserAddress qztUserAddress = null;
        List<QztUserAddress> qztUserAddresses = this.qztUserAddressMapper.find(map);
        if (qztUserAddresses != null && qztUserAddresses.size() > 0) {
            qztUserAddress = qztUserAddresses.get(0);
        }
        return qztUserAddress;
    }

    @Override
    public Map<String, String> disposeAddressInfo(Long addressId) {
        Map<String, String> remap = null;
        QztUserAddress qztUserAddress = this.queryById(addressId);
        if (qztUserAddress != null) {
            remap = new HashMap<>();
            //翻译省市区
//            String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
//            remap.put("address", areaName + qztUserAddress.getDetailAddress());
            remap.put("address", qztUserAddress.getAllAddress()+qztUserAddress.getDetailAddress());
            remap.put("name", qztUserAddress.getRecipientName());
            remap.put("tel", qztUserAddress.getPhone());
            remap.put("zipCode", qztUserAddress.getZipCode());
        }
        return remap;
    }

}
